package ru.vixtor141.MagickScrolls.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;

public class RitualBreakingEvent implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        if(event.getFrom().distance(event.getTo()) == 0)return;
        Mana playerMana = Main.getPlugin().getPlayerMap().get(event.getPlayer());
        if(!playerMana.getInRitualChecker())return;

        playerMana.setInRitualChecker(false);
        playerMana.getRitual().getAltar().ritualBrake();
    }


}
