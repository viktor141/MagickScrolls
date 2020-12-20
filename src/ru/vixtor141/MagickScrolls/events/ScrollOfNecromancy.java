package ru.vixtor141.MagickScrolls.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
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
            Player player = (Player) event.getEntity();
            if(!player.hasMetadata("MagickScrollsMana")){
                player.sendMessage(ChatColor.RED + "WARNING!!! Player: " + player.getDisplayName() + " lost a plugin meta.");
                return;
            }
            Mana playerMana =(Mana) player.getMetadata("MagickScrollsMana").get(0).value();
            playerMana.getExistMobs().parallelStream().forEach(Entity::remove);
            playerMana.getExistMobs().clear();
        }
    }
}
