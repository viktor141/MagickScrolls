package ru.vixtor141.MagickScrolls;

import com.google.common.base.Charsets;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ru.vixtor141.MagickScrolls.commands.Commands;
import ru.vixtor141.MagickScrolls.events.*;
import ru.vixtor141.MagickScrolls.includeAPI.PAPI;
import ru.vixtor141.MagickScrolls.lang.ReadingLangFile;
import ru.vixtor141.MagickScrolls.tasks.CleanUpTask;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main extends JavaPlugin {

    private Map<Player, Mana> PlayerMap = new HashMap<>();
    private static Main plugin;
    private ReadingLangFile readingLangFile;
    private List<LivingEntity> existMobs = new ArrayList<>();
    private List<CleanUpTask> cleanUpTasks = new ArrayList<>();
    private List<DefaultEffect> defaultEffectList = new ArrayList<>();
    private FileConfiguration recipesCF;

    public Main (){
        plugin = this;
    }

    public static Main getPlugin(){
        return plugin;
    }

    public FileConfiguration getRecipesCF() {
        return recipesCF;
    }

    public ReadingLangFile getReadingLangFile() {
        return readingLangFile;
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

    @Override
    public void onEnable() {
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new PAPI(this).register();
        }

        File config = new File(getDataFolder() + File.separator + "config.yml");
        if(!config.exists()){
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }

        File lang = new File(getDataFolder() + File.separator + "lang" + File.separator + plugin.getConfig().getString("lang") + ".yml");
        FileConfiguration lanfCF  = YamlConfiguration.loadConfiguration(lang);
        if(!lang.exists()){
            lanfCF.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(this.getResource("lang/en_US.yml"), Charsets.UTF_8)));
            lanfCF.options().copyDefaults(true);
            this.saveResource("lang/en_US.yml", false);
        }
        if(lanfCF.getString("MAGIC_STAFF.name") == null){
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[MagickScrolls]" + ChatColor.RED + "WARNING! SOME LINES IN YOUR LANG FILE ARE MISSING!");
        }

        readingLangFile = new ReadingLangFile(lanfCF);

        this.getCommand("magickScrolls").setExecutor(new Commands());


        File recipes = new File(getDataFolder() + File.separator + "recipes.yml");
        recipesCF  = YamlConfiguration.loadConfiguration(recipes);
        if(!recipes.exists()){
            recipesCF.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(this.getResource("recipes.yml"), Charsets.UTF_8)));
            recipesCF.options().copyDefaults(true);
            this.saveResource("recipes.yml", false);
        }

        registerEventsListenersOfScrolls();

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
                    List<Integer> list = new ArrayList<>(CDSystem.Scrolls.values().length);
                    for(int i = 0; i < CDSystem.Scrolls.values().length; i++){
                        list.add(i,0);
                    }
                    playerStats.set("CDSystem", list);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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

        playerStats.set("CDSystem", playerMana.getCdSystem().getCDs());

        getPlayerMap().remove(player);
        try {
            playerStats.save(playerSF);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerEventsListenersOfScrolls(){
        Bukkit.getPluginManager().registerEvents(new LightningScroll(), this);
        Bukkit.getPluginManager().registerEvents(new TeleportationScroll(), this);
        Bukkit.getPluginManager().registerEvents(new VampiricScroll(), this);
        Bukkit.getPluginManager().registerEvents(new SaveAndLoad(), this);
        Bukkit.getPluginManager().registerEvents(new VortexScroll(), this);
        Bukkit.getPluginManager().registerEvents(new ArrowStormScroll(), this);
        Bukkit.getPluginManager().registerEvents(new ScrollOfNecromancy(), this);
        Bukkit.getPluginManager().registerEvents(new SpiderWebScroll(), this);
        Bukkit.getPluginManager().registerEvents(new CraftStartEvent(), this);
    }
}
