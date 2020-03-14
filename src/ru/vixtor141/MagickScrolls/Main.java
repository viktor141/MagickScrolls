package ru.vixtor141.MagickScrolls;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ru.vixtor141.MagickScrolls.commands.GivingScrolls;
import ru.vixtor141.MagickScrolls.events.*;
import ru.vixtor141.MagickScrolls.tasks.CleanUpTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class Main extends JavaPlugin {

    private static Main plugin;

    public Main (){
        plugin = this;
    }

    public static Main getPlugin(){
        return plugin;
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new LightningScroll(), this);
        Bukkit.getPluginManager().registerEvents(new TeleportationScroll(), this);
        Bukkit.getPluginManager().registerEvents(new VampiricScroll(), this);
        Bukkit.getPluginManager().registerEvents(new SaveAndLoad(), this);
        Bukkit.getPluginManager().registerEvents(new VortexScroll(), this);
        Bukkit.getPluginManager().registerEvents(new ArrowStormScroll(), this);
        Bukkit.getPluginManager().registerEvents(new ScrollOfNecromancy(), this);

        this.getCommand("magickScrolls").setExecutor(new GivingScrolls());

        File config = new File(getDataFolder() + File.separator + "config.yml");
        if(!config.exists()){
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }

        for(Crafts.ScrollsCrafts scrollsCrafts : Crafts.ScrollsCrafts.values()){
            scrollsCrafts.craftScroll(true);
        }

        this.getLogger().info(ChatColor.YELLOW+"Plugin has been enabled");
    }


    @Override
    public void onDisable() {
        for (Player player: Bukkit.getOnlinePlayers()){
            savePlayerStats(player);
        }
        CleanUpTask cleanUpTask = new CleanUpTask();
        for(LivingEntity livingEntity: cleanUpTask.getExistMobs()){
            if(!livingEntity.isDead()){
                livingEntity.remove();
            }
        }
        cleanUpTask.getExistMobs().clear();
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
                    playerStats.set("CurrentMana", 50.0);
                    playerStats.set("MaxMana", 50.0);
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
        Mana playerMana = Mana.getPlayerMap().get(player);
        playerMana.cancelTask();

        File playerSF = new File(getDataFolder() + File.separator + "Players" + File.separator + player.getUniqueId().toString());
        FileConfiguration playerStats = YamlConfiguration.loadConfiguration(playerSF);

        playerStats.set("CurrentMana", playerMana.getCurrentMana());
        playerStats.set("MaxMana", playerMana.getMaxMana());

        playerStats.set("CDSystem", playerMana.getCdSystem().getCDs());

        Mana.getPlayerMap().remove(player);
        try {
            playerStats.save(playerSF);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
