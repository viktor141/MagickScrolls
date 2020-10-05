package ru.vixtor141.MagickScrolls.events;

import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;

public class ScrollOfNecromancy implements Listener {

    @EventHandler
    public void targetCorrect(EntityTargetLivingEntityEvent event){
        if (event.getEntity().hasMetadata("magicscrolls")){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void mobDropRemove(EntityDeathEvent event){
        if (event.getEntity().hasMetadata("magicscrolls")){
            event.getDrops().clear();
            event.setDroppedExp(0);
        }
    }

    @EventHandler
    public void mobKillWhenOwnerDie(EntityDeathEvent event){
        if(event.getEntity() instanceof Player){
            Mana playerMana = Main.getPlugin().getPlayerMap().get(event.getEntity());
            playerMana.getExistMobs().parallelStream().forEach(Entity::remove);
            playerMana.getExistMobs().clear();
        }
    }
}
