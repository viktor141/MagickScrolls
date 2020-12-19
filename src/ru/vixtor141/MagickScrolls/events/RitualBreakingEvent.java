package ru.vixtor141.MagickScrolls.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import ru.vixtor141.MagickScrolls.Mana;

public class RitualBreakingEvent implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        if(event.getFrom().distance(event.getTo()) == 0)return;
        Player player = event.getPlayer();
        if(!player.hasMetadata("MagickScrollsMana")){
            player.sendMessage(ChatColor.RED + "WARNING!!! Player: " + player.getDisplayName() + " lost a plugin meta.");
            return;
        }
        Mana playerMana =(Mana) player.getMetadata("MagickScrollsMana").get(0).value();
        if(!playerMana.getInRitualChecker())return;

        playerMana.setInRitualChecker(false);
        playerMana.getRitual().getAltar().ritualBrake();
    }


}
