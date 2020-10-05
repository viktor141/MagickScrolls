package ru.vixtor141.MagickScrolls.events;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class ArrowStormScroll implements Listener {

    @EventHandler
    public void hitsOfArrows(EntityDamageByEntityEvent event){
        if(!(event.getEntity() instanceof Player))return;
        if(!(event.getDamager() instanceof Arrow))return;
        Arrow arrow = (Arrow) event.getDamager();
        if(arrow.getCustomName() == null)return;
        if(arrow.getCustomName().equals(event.getEntity().getUniqueId().toString()+"magickscrolls")){
            event.setCancelled(true);
        }
    }
}
