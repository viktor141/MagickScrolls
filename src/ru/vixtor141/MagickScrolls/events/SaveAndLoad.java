package ru.vixtor141.MagickScrolls.events;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;

public class SaveAndLoad implements Listener {

    private Main plugin = Main.getPlugin();

    @EventHandler
    public void load(PlayerJoinEvent event){
        Player player = event.getPlayer();
        FileConfiguration playerStats = plugin.loadPlayerStats(player.getUniqueId().toString());
        Mana playerMana = new Mana(player);
        playerMana.setCurrentMana(Double.parseDouble(playerStats.get("CurrentMana").toString()));
        playerMana.setMaxMana(Double.parseDouble(playerStats.get("MaxMana").toString()));
    }

    @EventHandler
    public void save(PlayerQuitEvent event){
        plugin.savePlayerStats(event.getPlayer());
    }

}