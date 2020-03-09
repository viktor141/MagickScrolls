package ru.vixtor141.MagickScrolls.events;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;

import java.util.List;

public class SaveAndLoad implements Listener {

    private Main plugin = Main.getPlugin();

    @EventHandler
    public void load(PlayerJoinEvent event){
        Player player = event.getPlayer();
        FileConfiguration playerStats = plugin.loadPlayerStats(player.getUniqueId().toString());
        Mana playerMana = new Mana(player);
        playerMana.setCurrentMana(playerStats.getDouble("CurrentMana"));
        playerMana.setMaxMana(playerStats.getDouble("MaxMana"));
        List<Integer> CDsList = playerStats.getIntegerList("CDSystem");
        for(int i = 0; i < CDSystem.Scrolls.values().length; i++){
            playerMana.getCdSystem().getCDs().add(i, CDsList.get(i));
        }
    }

    @EventHandler
    public void save(PlayerQuitEvent event){
        plugin.savePlayerStats(event.getPlayer());
    }

}