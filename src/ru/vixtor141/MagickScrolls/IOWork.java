package ru.vixtor141.MagickScrolls;

import com.google.common.base.Charsets;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.vixtor141.MagickScrolls.Misc.RitualsRecipesStorage;
import ru.vixtor141.MagickScrolls.Misc.UpdateConfig;
import ru.vixtor141.MagickScrolls.aspects.Aspect;
import ru.vixtor141.MagickScrolls.aspects.AspectsInItems;
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
    private FileConfiguration recipesCF, langCF, ritualsCF, cauldronCF, recipeA;
    private CauldronCraftsStorage cauldronCraftsStorage;
    private AltarCraftsStorage altarCraftsStorage;
    private RitualsRecipesStorage ritualsRecipesStorage;
    private ResearchDataSaver researchDataSaver;
    private AspectsInItems aspectsInItems;

    public IOWork() {
        File config = new File(plugin.getDataFolder() + File.separator + "config.yml");
        if (!config.exists()) {
            plugin.getConfig().options().copyDefaults(true);
            plugin.saveDefaultConfig();
        }
        List<String> ignored = new ArrayList<>();
        for(String str : plugin.getConfig().getDefaults().getKeys(false)){
            if(plugin.getConfig().isConfigurationSection(str + ".neededAspects"))
            ignored.add(str + ".neededAspects");
        }

        loadConf(config, "config.yml", ignored);

        plugin.setManaRegenUnit(plugin.getConfig().getDouble("manaregenunit"));

        loadLangConfiguration();

        String recipesCFPath = "recipes.yml";
        File fileRecipesCF = new File(plugin.getDataFolder() + File.separator + recipesCFPath);
        recipesCF = YamlConfiguration.loadConfiguration(fileRecipesCF);
        loadOrUpdateRecipes(recipesCFPath, recipesCF, fileRecipesCF);

        String ritualsCFPath = "rituals.yml";
        File fileRitualsCF = new File(plugin.getDataFolder() + File.separator + ritualsCFPath);
        ritualsCF = YamlConfiguration.loadConfiguration(fileRitualsCF);
        loadOrUpdateRecipes(ritualsCFPath, ritualsCF, fileRitualsCF);

        String cauldronCFPath = "cauldron.yml";
        File fileCauldronCF = new File(plugin.getDataFolder() + File.separator + cauldronCFPath);
        cauldronCF = YamlConfiguration.loadConfiguration(fileCauldronCF);
        loadOrUpdateRecipes(cauldronCFPath, cauldronCF, fileCauldronCF);

        String recipeAPath = "aspects.yml";
        File fileRecipeA = new File(plugin.getDataFolder() + File.separator + recipeAPath);
        recipeA = YamlConfiguration.loadConfiguration(fileRecipeA);
        loadOrUpdateRecipes(recipeAPath, recipeA, fileRecipeA);

        loadCauldronRecipes();
        loadAltarRecipes();
        loadRitualRecipes();
        loadAspectRecipes();
    }

    public void loadResidualData(){
        LoadResearchData();
        loadShieldManaLevels();
    }

    public FileConfiguration getLangCF(){
        return langCF;
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

    public AspectsInItems getAspectsInItems() {
        return aspectsInItems;
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

    public void playerJoin(Mana playerMana){
        FileConfiguration playerStats = plugin.getIoWork().loadPlayerStats(playerMana.getPlayer().getUniqueId().toString());
        playerMana.setCurrentMana(playerStats.getDouble("CurrentMana"));
        playerMana.setMaxMana(playerStats.getDouble("MaxMana"));
        playerMana.setSpectralShieldSeconds(playerStats.getInt("SpectralShieldSeconds"));

        List<Integer> CDsList = playerStats.getIntegerList("CDSystem");
        for(int i = 0; i < CDSystem.CDsValuesLength(); i++){
            if(CDsList.size() > i){//fill up CD
                playerMana.getCdSystem().getCDs().add(i, CDsList.get(i));
            }
        }

        List<Boolean> researchList = playerStats.getBooleanList("Research");
        for(int i = 0; i < Research.values().length; i++){
            if(researchList.size() > i){//fill up empty Research
                playerMana.getPlayerResearch().getResearches().add(i, researchList.get(i));
            }
        }
        playerMana.getPlayerResearch().bookUpdate();

        List<String> activeResearch = playerStats.getStringList("ActiveResearch");
        if(!activeResearch.isEmpty()) {
            HashMap<String, Integer> map = new HashMap<>();
            for(String key : playerStats.getConfigurationSection("ActiveResearchData").getKeys(false)){
                map.put(key, playerStats.getInt("ActiveResearchData." + key));
            }
            for (String research : activeResearch) {
                Research.valueOf(research).startFromLoad(playerMana.getPlayerResearch(), map);
            }
        }

        playerMana.getPlayerResearch().getShieldManaLevels().setCount(playerStats.getInt("ShieldLevelCount", 0));
        playerMana.getPlayerResearch().getShieldManaLevels().setManaShieldLevel(playerStats.getString("ManaShieldLevel", "ZERO"));

        List<Boolean> wearingArtefacts = playerStats.getBooleanList("AccessoriesInventory");
        if(!wearingArtefacts.isEmpty()){
            for(int i = 0; i< wearingArtefacts.size(); i++){
                if(wearingArtefacts.get(i)){
                    playerMana.getPlayerResearch().getAccessoriesInventory().getInventory().addItem(ACCrafts.AccessoryArtefact.values()[i].getItem());
                    playerMana.getWearingArtefact().set(i, true);
                }
            }
        }

        List<Integer> aspects = playerStats.getIntegerList("Aspects");
        for(int i = 0; i < Aspect.values().length; i++){
            if(aspects.isEmpty()){
                break;
            }else if(aspects.size() > i){
                playerMana.getPlayerAspectsStorage().getAspects()[i] = aspects.get(i);
            }else {
                break;
            }
        }
        playerMana.getPlayerAspectsStorage().getAspectGui().inventoryFill();
    }


    private void LoadResearchData(){
        researchDataSaver = new ResearchDataSaver();
        for(Research research : Research.values()){
            if(plugin.getConfig().getConfigurationSection(research.name() + ".neededAspects") == null)continue;
            Map<String, Object> maps = plugin.getConfig().getConfigurationSection(research.name() + ".neededAspects").getValues(false);
            if(maps.isEmpty()){
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "WARNING!!! needed aspects is NULL for research: " + research.name() + " fix that!");
                continue;
            }

            try {

                maps.forEach((string, integer) -> researchDataSaver.put(research, Aspect.valueOf(string),(Integer) integer));
            }catch (IllegalArgumentException exception){
                String[] split = exception.getMessage().split("\\.");
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "WARNING!!! aspect name: " + split[split.length -1] + " isn't exist. In research: " + research.name() + " fix that!");
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
        List<ResearchI> researchI = playerMana.getPlayerResearch().getActiveResearch();
        HashMap<String, Integer> ActiveResearchData = new HashMap<>();
        for(int i = 0; i < researchI.size(); i++){
            if(researchI.get(i) != null) {
                activeResearch.add(Research.values()[i].name());
                ActiveResearchData.putAll(researchI.get(i).saveResearchData());
            }
        }
        playerStats.set("ActiveResearch", activeResearch);
        playerStats.set("ActiveResearchData", ActiveResearchData);
        playerStats.set("ShieldLevelCount", playerMana.getPlayerResearch().getShieldManaLevels().getCount());
        playerStats.set("ManaShieldLevel", playerMana.getPlayerResearch().getShieldManaLevels().getManaShieldLevel().name());
        playerStats.set("AccessoriesInventory", playerMana.getWearingArtefact());
        playerStats.set("Aspects", playerMana.getPlayerAspectsStorage().getAspects());

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

    private FileConfiguration loadConf(File file, String  name, List<String> ignored){
        try {
            UpdateConfig updateConfig = new UpdateConfig();
            updateConfig.update(plugin, name, file, ignored);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    private void loadOrUpdateRecipes(String ritPath, FileConfiguration fileConfiguration, File file){
        if(!file.exists()){
            fileConfiguration.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource(ritPath), Charsets.UTF_8)));
            fileConfiguration.options().copyDefaults(true);
            plugin.saveResource(ritPath, false);
        }else {
            fileConfiguration.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource(ritPath), Charsets.UTF_8)));
            fileConfiguration.options().copyDefaults(true);
            try {
                fileConfiguration.save(file);
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
            }
        }
        langCF = loadConf(langFile, "lang/en_US.yml");
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

    private void loadAspectRecipes(){
        aspectsInItems = new AspectsInItems();
        int counter = 0;
        for(String str : recipeA.getKeys(false)){
            ConfigurationSection configurationSection = recipeA.getConfigurationSection(str);
            if(configurationSection.contains("item") && configurationSection.contains("aspects")){
                try {
                    ItemStack itemStack;
                    List<Map<String, Integer>> aspectList = (List<Map<String, Integer>>) configurationSection.getList("aspects");
                    List<Map<String, String>> itemDataMapInList = (List<Map<String, String>>) configurationSection.getList("item");
                    Map<Aspect, Integer> aspectIntegerMap = new HashMap<>();

                    Map<String, String> itemDataMap = itemDataMapInList.get(0);

                    if (itemDataMap.get("mstype") != null) {
                        itemStack = new ItemStack(Material.STICK, 1);
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        itemMeta.setLore(Collections.singletonList(itemDataMap.get("mstype")));
                        itemStack.setItemMeta(itemMeta);
                    }else {
                        itemStack = new ItemStack(Material.valueOf(itemDataMap.get("type")), 1, Short.parseShort(itemDataMap.get("data")));
                    }

                    aspectList.get(0).forEach((string, integer) -> aspectIntegerMap.put(Aspect.valueOf(string), integer));

                    aspectsInItems.addNew(itemStack, aspectIntegerMap);
                    counter++;

                }catch (NumberFormatException exception){
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "WARNING!!! in " + str + " data isn't number! FixThat");
                }catch (IllegalArgumentException exception){
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "WARNING!!! in " + str + " Material or Aspect isn't exist! FixThat");
                } catch (NullPointerException | ClassCastException exception){
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "WARNING!!! in " + str + " something wrong FixThat");
                }

            }
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "Stored " + counter + " aspect recipes");
    }
}
