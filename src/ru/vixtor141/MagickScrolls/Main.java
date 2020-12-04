package ru.vixtor141.MagickScrolls;

import com.google.common.base.Charsets;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import ru.vixtor141.MagickScrolls.Misc.BookCreator;
import ru.vixtor141.MagickScrolls.Misc.RitualEnum;
import ru.vixtor141.MagickScrolls.Misc.UpdateConfig;
import ru.vixtor141.MagickScrolls.commands.Commands;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;
import ru.vixtor141.MagickScrolls.crafts.CauldronCraftsStorage;
import ru.vixtor141.MagickScrolls.events.*;
import ru.vixtor141.MagickScrolls.includeAPI.PAPI;
import ru.vixtor141.MagickScrolls.tasks.CleanUpTask;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


public class Main extends JavaPlugin {

    private final Map<Player, Mana> PlayerMap = new HashMap<>();
    private static Main plugin;
    private final List<LivingEntity> existMobs = new ArrayList<>();
    private final List<CleanUpTask> cleanUpTasks = new ArrayList<>();
    private final List<DefaultEffect> defaultEffectList = new ArrayList<>();
    private FileConfiguration recipesCF, lanfCF, ritualsCF, cauldronCF;
    private ItemStack ritualBook;
    private boolean manaMessage;
    private CauldronCraftsStorage cauldronCraftsStorage;

    public Main (){
        plugin = this;
    }

    public static Main getPlugin(){
        return plugin;
    }

    public boolean getManaMessage(){
        return manaMessage;
    }

    public FileConfiguration getRecipesCF() {
        return recipesCF;
    }

    public Map<Player, Mana> getPlayerMap() {
        return PlayerMap;
    }

    public List<CleanUpTask> getCleanUpTasks(){
        return cleanUpTasks;
    }

    public List<LivingEntity> getExistMobs(){
        return existMobs;
    }

    public List<DefaultEffect> getDefaultEffectList() {
        return defaultEffectList;
    }

    public ItemStack getRitualBook(){
        return ritualBook;
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

    public FileConfiguration getCauldronCF() {
        return cauldronCF;
    }

    @Override
    public void onEnable() {
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new PAPI(this).register();
        }

        loadConfigStuff();

        checkCrafts();

        createBook();

        loadCauldronRecipes();

        this.getCommand("magickScrolls").setExecutor(new Commands());

        registerEventListeners();

        this.getLogger().info(ChatColor.YELLOW+"Plugin has been enabled");
    }


    @Override
    public void onDisable() {
        for (Player player: Bukkit.getOnlinePlayers()){
            savePlayerStats(player);
        }
        for(CleanUpTask cleanUpTask : getCleanUpTasks()){
            cleanUpTask.getsWebTask().cancel();
            cleanUpTask.sWebCleanUpOnDisable();
        }
        getCleanUpTasks().clear();
        for(DefaultEffect defaultEffect : getDefaultEffectList()){
            defaultEffect.getKillerItemTask().cancel();
            defaultEffect.getEntityItem().remove();
        }
        getDefaultEffectList().clear();
        for(LivingEntity livingEntity: getExistMobs()){
            if(!livingEntity.isDead()){
                livingEntity.remove();
            }
        }
        getExistMobs().clear();
        this.getLogger().info(ChatColor.GOLD+"Plugin has been disabled");
    }

    public void loadConfigStuff(){
        File config = new File(getDataFolder() + File.separator + "config.yml");
        if(!config.exists()){
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }

        loadConf(config, "config.yml");

        loadLangConfiguration();

        readManaMessageSetting();

        loadRecipes();

        loadRituals();

        loadCauldronCrafting();
    }

    public FileConfiguration loadPlayerStats(String playerUUID){
        if(!new File(getDataFolder() + File.separator + "Players").exists()) {
            new File(getDataFolder() + File.separator + "Players").mkdir();
        }
        File playerSF = new File(getDataFolder() + File.separator + "Players" + File.separator + playerUUID);
        FileConfiguration playerStats = YamlConfiguration.loadConfiguration(playerSF);
        if(!playerSF.exists()) {
            try {
                if(playerSF.createNewFile()){
                    playerStats.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(getResource("PlayerDefaultStats.yml"), Charsets.UTF_8)));
                    playerStats.options().copyDefaults(true);
                    List<Integer> list = new ArrayList<>();
                    for(int i = 0; i < CDSystem.CDsValuesLength(); i++){
                        list.add(i,0);
                    }
                    playerStats.set("CDSystem", list);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            playerStats.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(this.getResource("PlayerDefaultStats.yml"), Charsets.UTF_8)));
            playerStats.options().copyDefaults(false);
        }
        return playerStats;
    }

    public void savePlayerStats(Player player){
        Mana playerMana = getPlayerMap().get(player);
        playerMana.cancelTask();

        File playerSF = new File(getDataFolder() + File.separator + "Players" + File.separator + player.getUniqueId().toString());
        FileConfiguration playerStats = YamlConfiguration.loadConfiguration(playerSF);

        playerStats.set("CurrentMana", playerMana.getCurrentMana());
        playerStats.set("MaxMana", playerMana.getMaxMana());
        playerStats.set("SpectralShield", playerMana.getSpectralShield().get());
        playerStats.set("SpectralShieldSeconds", playerMana.getSpectralShieldSeconds());

        playerStats.set("CDSystem", playerMana.getCdSystem().getCDs());

        getPlayerMap().remove(player);
        try {
            playerStats.save(playerSF);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private FileConfiguration loadConf(File file, String  name){
            try {
                UpdateConfig updateConfig = new UpdateConfig();
                updateConfig.update(this, name, file, new ArrayList<>());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return YamlConfiguration.loadConfiguration(file);
    }

    private void loadRecipes(){
        String recPath = "recipes.yml";
        File file = new File(getDataFolder() + File.separator + recPath);
        recipesCF  = YamlConfiguration.loadConfiguration(file);
        if(!file.exists()){
            recipesCF.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(this.getResource(recPath), Charsets.UTF_8)));
            recipesCF.options().copyDefaults(true);
            this.saveResource(recPath, false);
        }else {
            recipesCF.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(this.getResource(recPath), Charsets.UTF_8)));
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
        File file = new File(getDataFolder() + File.separator + ritPath);
        ritualsCF  = YamlConfiguration.loadConfiguration(file);
        if(!file.exists()){
            ritualsCF.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(this.getResource(ritPath), Charsets.UTF_8)));
            ritualsCF.options().copyDefaults(true);
            this.saveResource(ritPath, false);
        }else {
            ritualsCF.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(this.getResource(ritPath), Charsets.UTF_8)));
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
        File file = new File(getDataFolder() + File.separator + ritPath);
        cauldronCF = YamlConfiguration.loadConfiguration(file);
        if(!file.exists()){
            cauldronCF.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(this.getResource(ritPath), Charsets.UTF_8)));
            cauldronCF.options().copyDefaults(true);
            this.saveResource(ritPath, false);
        }else {
            cauldronCF.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(this.getResource(ritPath), Charsets.UTF_8)));
            cauldronCF.options().copyDefaults(true);
            try {
                cauldronCF.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerEventListeners(){
        Bukkit.getPluginManager().registerEvents(new VampiricScroll(), this);
        Bukkit.getPluginManager().registerEvents(new SaveAndLoad(), this);
        Bukkit.getPluginManager().registerEvents(new ArrowStormScroll(), this);
        Bukkit.getPluginManager().registerEvents(new ScrollOfNecromancy(), this);
        Bukkit.getPluginManager().registerEvents(new SpiderWebScroll(), this);
        Bukkit.getPluginManager().registerEvents(new CraftStartEvent(), this);
        Bukkit.getPluginManager().registerEvents(new TrapScroll(), this);
        Bukkit.getPluginManager().registerEvents(new EarthScroll(), this);
        Bukkit.getPluginManager().registerEvents(new RitualStartEvent(), this);
        Bukkit.getPluginManager().registerEvents(new RitualBreakingEvent(), this);
        Bukkit.getPluginManager().registerEvents(new ScrollUseEvent(), this);
        Bukkit.getPluginManager().registerEvents(new WitchAdd(), this);
        Bukkit.getPluginManager().registerEvents(new StartBuildEvent(), this);
        Bukkit.getPluginManager().registerEvents(new SpectralShieldScroll(), this);
        Bukkit.getPluginManager().registerEvents(new AirTrapScroll(), this);
    }

    private void loadLangConfiguration(){
        if(!new File(getDataFolder() + File.separator + "lang").exists()) {
            new File(getDataFolder() + File.separator + "lang").mkdir();
        }

        File langFile = new File(getDataFolder() + File.separator + "lang" + File.separator + plugin.getConfig().getString("lang") + ".yml");
        if(!langFile.exists()){
            try {
                langFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }        }
        lanfCF = loadConf(langFile, "lang" + File.separator + "en_US.yml");
    }

    public void createBook(){
        ritualBook = new BookCreator().getBook();
    }

    private void readManaMessageSetting(){
        manaMessage = plugin.getConfig().getBoolean("messageAboutMana");
    }

    public void checkCrafts(){
        for(ACCrafts.CraftsOfScrolls scroll : ACCrafts.CraftsOfScrolls.values()){
            List<?> list = recipesCF.getList(scroll.name());
            if(list == null){
                this.getLogger().info(ChatColor.RED + "Recipe for " + scroll.name() + " is null. Test build?");
                continue;
            }
            if(list.isEmpty())this.getLogger().info(ChatColor.RED + "Recipe for " + scroll.name() + " is empty");
            if(!list.parallelStream().allMatch(o -> o instanceof ItemStack))this.getLogger().info(ChatColor.RED + "Something wrong in recipe for " + scroll.name());

        }

        for(RitualEnum.Rituals ritual : RitualEnum.Rituals.values()){
            List<?> list = ritualsCF.getList(ritual.name());
            if(list.isEmpty())this.getLogger().info(ChatColor.RED + "Recipe for " + ritual.name() + " is empty");
            if(!list.parallelStream().allMatch(o -> o instanceof ItemStack))this.getLogger().info(ChatColor.RED + "Something wrong in ritual recipe for " + ritual.name());

        }
    }

    public void loadCauldronRecipes(){
        int max = 0, current;
        for(ACCrafts.ItemsCauldronCrafts craft : ACCrafts.ItemsCauldronCrafts.values()){
            if(cauldronCF.getList(craft.name() + ".recipe") == null){
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "WARNING!!! Not Found " + craft.name() + " recipe. Check the cauldron.yml and fix that!");
                continue;
            }
            current = cauldronCF.getList(craft.name() + ".recipe").size();
            if(max < current)max = current;
        }
        cauldronCraftsStorage = new CauldronCraftsStorage(max);

        for(ACCrafts.ItemsCauldronCrafts craft : ACCrafts.ItemsCauldronCrafts.values()){
            List<ItemStack> list = new ArrayList<>();
            List<Map<String, String>> recipeList =((List<Map<String, String>>)cauldronCF.getList(craft.name() + ".recipe"));
            current = recipeList.size();
            for(int i = 0; i < current; i++){
                ItemStack itemStack = new ItemStack(Material.valueOf(recipeList.get(i).get("type")), Integer.parseInt(recipeList.get(i).get("amount")));
                if(recipeList.get(i).get("mstype") != null){
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.setLore(Collections.singletonList(recipeList.get(i).get("mstype")));
                    itemStack.setItemMeta(itemMeta);
                }
                list.add(itemStack);
            }
            cauldronCraftsStorage.filling(list, craft);
        }

    }

}
