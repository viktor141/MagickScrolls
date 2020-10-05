package ru.vixtor141.MagickScrolls.events;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class EarthScroll implements Listener {

    @EventHandler
    public void onChange(EntityChangeBlockEvent event) {
        if(!event.getEntityType().equals(EntityType.FALLING_BLOCK))return;
        if(!event.getEntity().hasMetadata("magickscrolls_earthScoll"))return;
        event.setCancelled(true);
    }

}
