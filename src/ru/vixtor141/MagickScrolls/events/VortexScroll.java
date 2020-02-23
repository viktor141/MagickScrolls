package ru.vixtor141.MagickScrolls.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import ru.vixtor141.MagickScrolls.Mana;

import java.util.Collection;

import static java.lang.Math.*;
import static java.lang.Math.toRadians;

public class VortexScroll implements Listener {

    @EventHandler
    public void use(PlayerInteractEvent event) {
        if(event.getHand().equals(EquipmentSlot.OFF_HAND))return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getPlayer().getInventory().getItemInMainHand().getType() != Material.PAPER) return;
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if (!item.getItemMeta().hasLore()) return;
        if (!item.getItemMeta().getLore().get(0).equals("Vortex scroll")) return;

        Player player = event.getPlayer();

        Entity targetEntity = searchEntity(player);

        if(targetEntity == null){
            player.sendMessage("Target not defined");
            return;
        }

        Mana playerMana = Mana.getPlayerMap().get(player);
        if (!playerMana.consumeMana(2)) return;

        targetEntity.setVelocity(new Vector(0,2,0));
    }

    public void entityInteract(EntityInteractEvent event) {
        if(!(event.getEntity() instanceof Player))return;

        Player player =(Player)event.getEntity();

        if (player.getInventory().getItemInMainHand().getType() != Material.PAPER) return;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!item.getItemMeta().hasLore()) return;
        if (!item.getItemMeta().getLore().get(0).equals("Vortex scroll")) return;

        event.setCancelled(true);

        Entity targetEntity = searchEntity(player);

        if(targetEntity == null){
            player.sendMessage("Target not defined");
            return;
        }

        Mana playerMana = Mana.getPlayerMap().get(player);
        if (!playerMana.consumeMana(2)) return;

        targetEntity.setVelocity(new Vector(0,2,0));
    }


    public Entity searchEntity(Player player){
        Location locationOfPlayer = player.getLocation();
        Location newLocation;

        double newX, newZ, newY;
        float yaw = locationOfPlayer.getYaw();
        float pitch = locationOfPlayer.getPitch();
        for(int i = 0; i < 10; i++) {
            newY = i * sin(toRadians(pitch));
            newX = i * sin(toRadians(yaw)) * cos(toRadians(pitch));
            newZ = i * cos(toRadians(yaw)) * cos(toRadians(pitch));

            newLocation = new Location(player.getWorld(), locationOfPlayer.getX() + newX * ((double)-1), locationOfPlayer.getY() + newY * ((double)-1), locationOfPlayer.getZ() + newZ);
            Collection<Entity> collectionEntities = player.getWorld().getNearbyEntities(newLocation, 1,1,1);

            if(!collectionEntities.isEmpty()) {
                for(Entity entity : collectionEntities){
                    if(entity instanceof Player){
                        if((Player)entity != player){
                            return entity;
                        }
                    }else{
                        return entity;
                    }
                }
            }
        }
        return null;
    }


}
