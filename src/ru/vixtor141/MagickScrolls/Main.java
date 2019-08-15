package ru.vixtor141.MagickScrolls;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import ru.vixtor141.MagickScrolls.events.LightningScroll;
import ru.vixtor141.MagickScrolls.events.TeleportationScroll;
import ru.vixtor141.MagickScrolls.events.VampiricScroll;

import static ru.vixtor141.MagickScrolls.Crafts.crafts;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new LightningScroll(), this);
        Bukkit.getPluginManager().registerEvents(new TeleportationScroll(), this);
        Bukkit.getPluginManager().registerEvents(new VampiricScroll(), this);
        this.getLogger().info(ChatColor.YELLOW+"Plugin was enabled");
        crafts();
    }

    @Override
    public void onDisable() {
        this.getLogger().info(ChatColor.GOLD+"Plugin was disabled");
    }



}
