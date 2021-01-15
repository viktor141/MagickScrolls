package ru.vixtor141.MagickScrolls.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import ru.vixtor141.MagickScrolls.Mana;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.getPlayerMana;

public class RitualBreakingEvent implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        if(event.getFrom().distance(event.getTo()) == 0)return;
        Player player = event.getPlayer();
        Mana playerMana = getPlayerMana(player);
        if(!playerMana.getInRitualChecker())return;

        playerMana.setInRitualChecker(false);
        playerMana.getRitual().getAltar().ritualBrake();
    }


}
