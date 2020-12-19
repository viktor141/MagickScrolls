package ru.vixtor141.MagickScrolls.events;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.LazyMetadataValue;
import org.bukkit.scheduler.BukkitTask;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.research.Research;

import java.util.List;

public class SaveAndLoad implements Listener {

    private final Main plugin = Main.getPlugin();
    private Mana playerMana;

    @EventHandler
    public void load(PlayerJoinEvent event){
        Player player = event.getPlayer();
        FileConfiguration playerStats = plugin.getIoWork().loadPlayerStats(player.getUniqueId().toString());
        playerMana = new Mana(player);
        player.setMetadata("MagickScrollsMana", new LazyMetadataValue(Main.getPlugin(), this::getPlayerMana));
        playerMana.setCurrentMana(playerStats.getDouble("CurrentMana"));
        playerMana.setMaxMana(playerStats.getDouble("MaxMana"));
        playerMana.getSpectralShield().set(playerStats.getBoolean("SpectralShield"));
        playerMana.setSpectralShieldSeconds(playerStats.getInt("SpectralShieldSeconds"));
        List<Integer> CDsList = playerStats.getIntegerList("CDSystem");
        for(int i = 0; i < CDSystem.CDsValuesLength(); i++){
            if(CDsList.size() <= i){//fill up empty CD
                playerMana.getCdSystem().getCDs().add(i,0);
            }else {
                playerMana.getCdSystem().getCDs().add(i, CDsList.get(i));
            }
        }
        List<Boolean> ResearchList = playerStats.getBooleanList("Research");
        for(int i = 0; i < Research.values().length; i++){
            if(ResearchList.size() <= i){//fill up empty Research
                playerMana.getPlayerResearch().getResearches().add(i,false);
            }else {
                playerMana.getPlayerResearch().getResearches().add(i, ResearchList.get(i));
            }
        }
    }

    private Mana getPlayerMana(){
        return playerMana;
    }

    @EventHandler
    public void save(PlayerQuitEvent event){
        BukkitTask bukkitTask = playerMana.getSpectralShieldEffectTask();
        if(bukkitTask != null && !bukkitTask.isCancelled())bukkitTask.cancel();
        if(playerMana.getInRitualChecker()) {

            playerMana.setInRitualChecker(false);
            playerMana.getRitual().getAltar().ritualBrake();
        }
        plugin.getIoWork().savePlayerStats(event.getPlayer());
    }

}