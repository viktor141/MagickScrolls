package ru.vixtor141.MagickScrolls.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import ru.vixtor141.MagickScrolls.scrolls.AirTrap;

public class AirTrapScroll implements Listener {

    @EventHandler
    public void hit(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof Player))return;
        if(!event.getEntity().hasMetadata("magickscrolls_Air_Trapped"))return;

        event.setDamage(event.getDamage() * 2);

        ((AirTrap)event.getEntity().getMetadata("magickscrolls_Air_Trapped").get(0).value()).stop();

    }

}
