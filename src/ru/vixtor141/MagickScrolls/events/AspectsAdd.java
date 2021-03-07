package ru.vixtor141.MagickScrolls.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import ru.vixtor141.MagickScrolls.aspects.Aspect;

import java.util.Random;

public class AspectsAdd implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void xpChange(PlayerExpChangeEvent event){
        if(event.getAmount() > 1){
            Aspect.Experientia.aspectAdd(event.getPlayer(), event.getAmount()/2);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlock(EntityDamageByEntityEvent event){
        if(!(event.getEntity() instanceof Player))return;
        Player player =(Player) event.getEntity();
        if(!player.isBlocking())return;
        if(event.isCancelled())return;

        Aspect.Custodia.aspectAdd(player, new Random().nextInt(4));
    }

}
