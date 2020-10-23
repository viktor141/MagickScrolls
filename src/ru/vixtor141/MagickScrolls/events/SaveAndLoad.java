package ru.vixtor141.MagickScrolls.events;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;

import java.util.List;

public class SaveAndLoad implements Listener {

    private final Main plugin = Main.getPlugin();

    @EventHandler
    public void load(PlayerJoinEvent event){
        Player player = event.getPlayer();
        FileConfiguration playerStats = plugin.loadPlayerStats(player.getUniqueId().toString());
        Mana playerMana = new Mana(player);
        playerMana.setCurrentMana(playerStats.getDouble("CurrentMana"));
        playerMana.setMaxMana(playerStats.getDouble("MaxMana"));
        playerMana.getSpectralShield().set(playerStats.getBoolean("SpectralShield"));
        playerMana.setSpectralShieldSeconds(playerStats.getInt("SpectralShieldSeconds"));
        List<Integer> CDsList = playerStats.getIntegerList("CDSystem");
        for(int i = 0; i < CDSystem.CDsValuesLength(); i++){
            if(CDsList.size() <= i){
                playerMana.getCdSystem().getCDs().add(i,0);
            }else {
                playerMana.getCdSystem().getCDs().add(i, CDsList.get(i));
            }
        }
    }

    @EventHandler
    public void save(PlayerQuitEvent event){
        Mana playerMana = Main.getPlugin().getPlayerMap().get(event.getPlayer());
        BukkitTask bukkitTask = playerMana.getSpectralShieldEffectTask();
        if(bukkitTask != null && !bukkitTask.isCancelled())bukkitTask.cancel();
        if(playerMana.getInRitualChecker()) {//ritual stop if player leave

            playerMana.setInRitualChecker(false);
            playerMana.getRitual().getAltar().ritualBrake();
        }
        plugin.savePlayerStats(event.getPlayer());
    }

}