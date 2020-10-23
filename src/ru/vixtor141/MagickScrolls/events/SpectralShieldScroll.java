package ru.vixtor141.MagickScrolls.events;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.tasks.BatKillTask;

public class SpectralShieldScroll implements Listener {

    @EventHandler
    public void impact(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof Projectile))return;
        if(!(event.getEntity() instanceof Player))return;
        Mana playerMana = Main.getPlugin().getPlayerMap().get((Player) event.getEntity());
        if(!playerMana.getSpectralShield().get())return;

        event.setCancelled(true);

        Location location = event.getDamager().getLocation();
        Entity bat = location.getWorld().spawnEntity(location, EntityType.BAT);
        event.getDamager().remove();
        new BatKillTask(bat);
    }
}
