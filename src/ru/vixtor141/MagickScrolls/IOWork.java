package ru.vixtor141.MagickScrolls;

import com.google.common.base.Charsets;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.vixtor141.MagickScrolls.Misc.RitualsRecipesStorage;
import ru.vixtor141.MagickScrolls.Misc.UpdateConfig;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;
import ru.vixtor141.MagickScrolls.crafts.AltarCraftsStorage;
import ru.vixtor141.MagickScrolls.crafts.CauldronCraftsStorage;
import ru.vixtor141.MagickScrolls.interfaces.ResearchI;
import ru.vixtor141.MagickScrolls.levels.ManaShieldLevel;
import ru.vixtor141.MagickScrolls.research.Research;
import ru.vixtor141.MagickScrolls.research.ResearchDataSaver;
import ru.vixtor141.MagickScrolls.ritual.RitualE;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.getPlayerMana;

public class IOWork {
    private final Main plugin = Main.getPlugin();
    private FileConfiguration recipesCF, lanfCF, ritualsCF, cauldronCF;
    private CauldronCraftsStorage cauldronCraftsStorage;
    private AltarCraftsStorage altarCraftsStorage;
    private RitualsRecipesStorage ritualsRecipesStorage;
    private ResearchDataSaver researchDataSaver;

    public IOWork() {
        File config = new File(plugin.getDataFolder() + File.separator + "config.yml");
        if (!config.exists()) {
            plugin.getConfig().options().copyDefaults(true);
            plugin.saveDefaultConfig();
        }

        loadConf(config, "config.yml");

        plugin.setManaRegenUnit(plugin.getConfig().getDouble("manaregenunit"));

        loadLangConfiguration();

        loadRecipes();

        loadRituals();

        loadCauldronCrafting();
        loadCauldronRecipes();
        loadAltarRecipes();
        loadRitualRecipes();
    }

    public void loadResidualData(){
        LoadResearchData();
        loadShieldManaLevels();
    }

    public FileConfiguration getLangCF(){
        return lanfCF;
    }

    public CauldronCraftsStorage getCauldronCraftsStorage(){
        return cauldronCraftsStorage;
    }

    public AltarCraftsStorage getAltarCraftsStorage(){
        return altarCraftsStorage;
    }

    public RitualsRecipesStorage getRitualsRecipesStorage(){
        return ritualsRecipesStorage;
    }

    public FileConfiguration getCauldronCF() {
        return cauldronCF;
    }

    public ResearchDataSaver getResearchDataSaver() {
        return researchDataSaver;
    }

    private void loadShieldManaLevels(){
        for(ManaShieldLevel shieldManaLevel : ManaShieldLevel.values()){
            shieldManaLevel.setCountNumber(plugin.getConfig().getInt("ManaShield.count." + shieldManaLevel.name()));
        }
        for(ManaShieldLevel shieldManaLevel : ManaShieldLevel.values()){
            shieldManaLevel.setDamageAndMana(plugin.getConfig().getDouble("ManaShield.damage." + shieldManaLevel.name()), ManaShieldLevel.TypeDamageOrMana.DAMAGE);
            shieldManaLevel.setDamageAndMana(plugin.getConfig().getDouble("ManaShield.regen." + shieldManaLevel.name()), ManaShieldLevel.TypeDamageOrMana.MANA);
            shieldManaLevel.setXpLevels(plugin.getConfig().getInt("ManaShield.xplvl." + shieldManaLevel.name()));
            shieldManaLevel.setItemForLevel(plugin.getLangCF().getString("ManaShield." + shieldManaLevel.name()));
        }
    }

    private void LoadResearchData(){
        researchDataSaver = new ResearchDataSaver();
        for(Research research : Research.values()){
            if(plugin.getConfig().getConfigurationSection(research.name() + ".mobsToKill") == null)continue;
            Map<String, Object> maps = plugin.getConfig().getConfigurationSection(research.name() + ".mobsToKill").getValues(false);
            if(maps.isEmpty()){
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "WARNING!!! " + research.name() + " Kills Data is NULL fix that!");
                continue;
            }

            try {
                maps.forEach((string, integer) -> researchDataSaver.put(research, EntityType.valueOf(string),(Integer) integer));
            }catch (IllegalArgumentException exception){
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "WARNING!!! " + research.name() + " entity type is wrong");
            }
        }

    }

    public int getIntDataForResearch(Research research, String string){
        return plugin.getConfig().getInt(research.name() + "." + string);
    }

    public FileConfiguration loadPlayerStats(String playerUUID){
        if(!new File(plugin.getDataFolder() + File.separator + "Players").exists()) {
            if(!new File(plugin.getDataFolder() + File.separator + "Players").mkdir()){
                Bukkit.getConsoleSender().sendMessage("Can't load data of player " + playerUUID);
            }
        }
        File playerSF = new File(plugin.getDataFolder() + File.separator + "Players" + File.separator + playerUUID);
        FileConfiguration playerStats = YamlConfiguration.loadConfiguration(playerSF);
        if(!playerSF.exists()) {
            try {
                if(playerSF.createNewFile()){
                    playerStats.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource("PlayerDefaultStats.yml"), Charsets.UTF_8)));
                    playerStats.options().copyDefaults(true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            playerStats.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource("PlayerDefaultStats.yml"), Charsets.UTF_8)));
            playerStats.options().copyDefaults(false);
        }
        return playerStats;
    }

    public void savePlayerStats(Player player){
        Mana playerMana = getPlayerMana(player);

        File playerSF = new File(plugin.getDataFolder() + File.separator + "Players" + File.separator + player.getUniqueId().toString());
        FileConfiguration playerStats = YamlConfiguration.loadConfiguration(playerSF);

        playerStats.set("CurrentMana", playerMana.getCurrentMana());
        playerStats.set("MaxMana", playerMana.getMaxMana());
        playerStats.set("SpectralShieldSeconds", playerMana.getSpectralShieldSeconds());

        playerStats.set("CDSystem", playerMana.getCdSystem().getCDs());
        playerStats.set("Research", playerMana.getPlayerResearch().getResearches());
        List<String> activeResearch = new ArrayList<>();
        List<ResearchI> research = playerMana.getPlayerResearch().getActiveResearch();
        for(int i = 0; i < research.size(); i++){
            if(research.get(i) != null) {
                activeResearch.add(Research.values()[i].name());
                research.get(i).saveResearchData(playerStats);
            }
        }
        playerStats.set("ActiveResearch", activeResearch);
        playerStats.set("ShieldLevelCount", playerMana.getPlayerResearch().getShieldManaLevels().getCount());
        playerStats.set("ManaShieldLevel", playerMana.getPlayerResearch().getShieldManaLevels().getManaShieldLevel().name());
        playerStats.set("AccessoriesInventory", playerMana.getPlayerResearch().getAccessoriesInventory().getInventory().getContents());

        try {
            playerStats.save(playerSF);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private FileConfiguration loadConf(File file, String  name){
        try {
            UpdateConfig updateConfig = new UpdateConfig();
            updateConfig.update(plugin, name, file, new ArrayList<>());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    private void loadRecipes(){
        String recPath = "recipes.yml";
        File file = new File(plugin.getDataFolder() + File.separator + recPath);
        recipesCF  = YamlConfiguration.loadConfiguration(file);
        if(!file.exists()){
            recipesCF.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource(recPath), Charsets.UTF_8)));
            recipesCF.options().copyDefaults(true);
            plugin.saveResource(recPath, false);
        }else {
            recipesCF.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource(recPath), Charsets.UTF_8)));
            recipesCF.options().copyDefaults(true);
            try {
                recipesCF.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadRituals(){
        String ritPath = "rituals.yml";
        File file = new File(plugin.getDataFolder() + File.separator + ritPath);
        ritualsCF  = YamlConfiguration.loadConfiguration(file);
        if(!file.exists()){
            ritualsCF.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource(ritPath), Charsets.UTF_8)));
            ritualsCF.options().copyDefaults(true);
            plugin.saveResource(ritPath, false);
        }else {
            ritualsCF.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource(ritPath), Charsets.UTF_8)));
            ritualsCF.options().copyDefaults(true);
            try {
                ritualsCF.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadCauldronCrafting(){
        String ritPath = "cauldron.yml";
        File file = new File(plugin.getDataFolder() + File.separator + ritPath);
        cauldronCF = YamlConfiguration.loadConfiguration(file);
        if(!file.exists()){
            cauldronCF.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource(ritPath), Charsets.UTF_8)));
            cauldronCF.options().copyDefaults(true);
            plugin.saveResource(ritPath, false);
        }else {
            cauldronCF.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource(ritPath), Charsets.UTF_8)));
            cauldronCF.options().copyDefaults(true);
            try {
                cauldronCF.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadLangConfiguration(){
        if(!new File(plugin.getDataFolder() + File.separator + "lang").exists()) {
            if(!new File(plugin.getDataFolder() + File.separator + "lang").mkdir()){
                Bukkit.getConsoleSender().sendMessage("Can't load Lang file");
            }
        }

        File langFile = new File(plugin.getDataFolder() + File.separator + "lang" + File.separator + plugin.getConfig().getString("lang") + ".yml");
        if(!langFile.exists()){
            try {
                if(!langFile.createNewFile()){
                    Bukkit.getConsoleSender().sendMessage("Can't create lang file");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }        }
        lanfCF = loadConf(langFile, "lang" + File.separator + "en_US.yml");
    }

    private void loadCauldronRecipes(){
        int max = 0, current;
        for(ACCrafts.ItemsCauldronCrafts craft : ACCrafts.ItemsCauldronCrafts.values()){
            if(craft.isIgnoreRecipe())continue;
            if(cauldronCF.getList(craft.name() + ".recipe") == null){
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "WARNING!!! Not Found " + craft.name() + " recipe. Check the cauldron.yml and fix that!");
                continue;
            }
            List<List<Map<String, String>>> recipesList =(List<List<Map<String, String>>>)cauldronCF.getList(craft.name() + ".recipe");
            try {
                for (List<Map<String, String>> recipeList : recipesList) {
                    current = recipeList.size();
                    if (max < current) max = current;
                }
            }catch (IllegalArgumentException | NullPointerException | ClassCastException exception){
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "WARNING!!! Some problems with " + craft.name() + " Illegal Argument or Null argument. Fix that");
            }
        }
        cauldronCraftsStorage = new CauldronCraftsStorage(max);

        for(ACCrafts.ItemsCauldronCrafts craft : ACCrafts.ItemsCauldronCrafts.values()){
            if(craft.isIgnoreRecipe())continue;
            List<List<Map<String, String>>> recipesList =(List<List<Map<String, String>>>)cauldronCF.getList(craft.name() + ".recipe");
            if(recipesList == null){
                continue;
            }
            try {
                for (List<Map<String, String>> recipeList : recipesList) {
                    current = recipeList.size();
                    List<ItemStack> list = new ArrayList<>();
                    for (int i = 0; i < current; i++) {
                        ItemStack itemStack = new ItemStack(Material.valueOf(recipeList.get(i).get("type")), Integer.parseInt(recipeList.get(i).get("amount")), Short.parseShort(recipeList.get(i).get("data")));
                        if (recipeList.get(i).get("mstype") != null) {
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            itemMeta.setLore(Collections.singletonList(recipeList.get(i).get("mstype")));
                            itemStack.setItemMeta(itemMeta);
                        }
                        list.add(itemStack);
                    }
                    cauldronCraftsStorage.filling(list, craft);
                }
            }catch (IllegalArgumentException | NullPointerException | ClassCastException exception){
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "WARNING!!! Some problems with " + craft.name() + " Illegal Argument or Null argument. Fix that");
            }
        }

    }


    private void loadAltarRecipes(){
        altarCraftsStorage = new AltarCraftsStorage();

        for(ACCrafts.CraftsOfScrolls scroll : ACCrafts.CraftsOfScrolls.values()){
            try {
                List<Map<String, String>> recipesList = (List<Map<String, String>>) recipesCF.getList(scroll.name());
                List<ItemStack> list = new ArrayList<>();
                for (Map<String, String> ItemMap : recipesList) {
                    ItemStack itemStack = new ItemStack(Material.valueOf(ItemMap.get("type")), 1, Short.parseShort(ItemMap.get("data")));
                    if (ItemMap.get("mstype") != null) {
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        itemMeta.setLore(Collections.singletonList(ItemMap.get("mstype")));
                        itemStack.setItemMeta(itemMeta);
                    }
                    for (int i = 0; i < Integer.parseInt(ItemMap.get("amount")); i++) {
                        list.add(itemStack);
                    }
                }
                if (list.size() > 12) {
                    altarCraftsStorage.filling(list.subList(0, 12));
                } else {
                    altarCraftsStorage.filling(list);
                }
            }catch (IllegalArgumentException | NullPointerException | ClassCastException exception){
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "WARNING!!! Some problems with " + scroll.name() + " Illegal Argument or Null argument. Fix that");
            }
        }
    }

    private void loadRitualRecipes(){
        ritualsRecipesStorage = new RitualsRecipesStorage();

        for(RitualE ritual : RitualE.values()){
            try {
                List<Map<String, String>> recipesList = (List<Map<String, String>>) ritualsCF.getList(ritual.name());
                List<ItemStack> list = new ArrayList<>();
                for (Map<String, String> ItemMap : recipesList) {
                    ItemStack itemStack = new ItemStack(Material.valueOf(ItemMap.get("type")), Integer.parseInt(ItemMap.get("amount")), Short.parseShort(ItemMap.get("data")));
                    if (ItemMap.get("mstype") != null) {
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        itemMeta.setLore(Collections.singletonList(ItemMap.get("mstype")));
                        itemStack.setItemMeta(itemMeta);
                    }
                    list.add(itemStack);
                }
                ritualsRecipesStorage.filling(list);
            }catch (IllegalArgumentException | NullPointerException | ClassCastException exception){
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "WARNING!!! Some problems with " + ritual.name() + " Illegal Argument or Null argument or Class cast(wrong hierarchy). Fix that");
            }
        }
    }
}
