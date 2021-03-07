package ru.vixtor141.MagickScrolls.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.getPlayerMana;

public class SaveAndLoad implements Listener {

    private final Main plugin = Main.getPlugin();

    @EventHandler
    public void load(PlayerJoinEvent event){
        Player player = event.getPlayer();
        Mana playerMana = new Mana(player);

        if(Main.getPlugin().getConfig().getBoolean("useDB")) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> Main.getPlugin().getDataBase().loadDataFromDB(playerMana));
        }else {
            Main.getPlugin().getIoWork().playerJoin(playerMana);
        }

    }

    @EventHandler
    public void save(PlayerQuitEvent event){//авто сохранение данных по интервалу и при выключении се5рвера
        Mana playerMana = getPlayerMana(event.getPlayer());
        BukkitTask bukkitTask = playerMana.getSpectralShieldEffectTask();
        if(bukkitTask != null && !bukkitTask.isCancelled())bukkitTask.cancel();
        if(playerMana.getInRitualChecker()) {
            playerMana.setInRitualChecker(false);
            playerMana.getRitual().getAltar().ritualBrake();
        }

        if(Main.getPlugin().getConfig().getBoolean("useDB")) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> Main.getPlugin().getDataBase().saveDataToDB(playerMana));
        }else {
            plugin.getIoWork().savePlayerStats(event.getPlayer());
        }
    }

}