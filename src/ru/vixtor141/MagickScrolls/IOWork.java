package ru.vixtor141.MagickScrolls;

import com.google.common.base.Charsets;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.vixtor141.MagickScrolls.Misc.RitualEnum;
import ru.vixtor141.MagickScrolls.Misc.RitualsRecipesStorage;
import ru.vixtor141.MagickScrolls.Misc.UpdateConfig;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;
import ru.vixtor141.MagickScrolls.crafts.AltarCraftsStorage;
import ru.vixtor141.MagickScrolls.crafts.CauldronCraftsStorage;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class IOWork {
    private final Main plugin = Main.getPlugin();
    private FileConfiguration recipesCF, lanfCF, ritualsCF, cauldronCF;
    private CauldronCraftsStorage cauldronCraftsStorage;
    private AltarCraftsStorage altarCraftsStorage;
    private RitualsRecipesStorage ritualsRecipesStorage;

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

    public FileConfiguration getRecipesCF() {
        return recipesCF;
    }

    public FileConfiguration getLangCF(){
        return lanfCF;
    }

    public FileConfiguration getRitualsCF(){
        return ritualsCF;
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

    public FileConfiguration loadPlayerStats(String playerUUID){
        if(!new File(plugin.getDataFolder() + File.separator + "Players").exists()) {
            new File(plugin.getDataFolder() + File.separator + "Players").mkdir();
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
        Mana playerMana = plugin.getPlayerMap().get(player);
        playerMana.cancelTask();

        File playerSF = new File(plugin.getDataFolder() + File.separator + "Players" + File.separator + player.getUniqueId().toString());
        FileConfiguration playerStats = YamlConfiguration.loadConfiguration(playerSF);

        playerStats.set("CurrentMana", playerMana.getCurrentMana());
        playerStats.set("MaxMana", playerMana.getMaxMana());
        playerStats.set("SpectralShield", playerMana.getSpectralShield().get());
        playerStats.set("SpectralShieldSeconds", playerMana.getSpectralShieldSeconds());

        playerStats.set("CDSystem", playerMana.getCdSystem().getCDs());
        playerStats.set("Research", playerMana.getPlayerResearch().getResearches());

        plugin.getPlayerMap().remove(player);
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
            new File(plugin.getDataFolder() + File.separator + "lang").mkdir();
        }

        File langFile = new File(plugin.getDataFolder() + File.separator + "lang" + File.separator + plugin.getConfig().getString("lang") + ".yml");
        if(!langFile.exists()){
            try {
                langFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }        }
        lanfCF = loadConf(langFile, "lang" + File.separator + "en_US.yml");
    }

    private void loadCauldronRecipes(){
        int max = 0, current;
        for(ACCrafts.ItemsCauldronCrafts craft : ACCrafts.ItemsCauldronCrafts.values()){
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

        for(RitualEnum.Rituals ritual : RitualEnum.Rituals.values()){
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
