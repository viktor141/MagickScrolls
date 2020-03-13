package ru.vixtor141.MagickScrolls.events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;

import java.util.Collection;

import static java.lang.Math.*;
import static java.lang.Math.toRadians;

public class VortexScroll implements Listener, Runnable {

    private Player player;
    private ItemStack item;

    @EventHandler
    public void use(PlayerInteractEvent event) {
        if(event.getHand().equals(EquipmentSlot.OFF_HAND))return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getPlayer().getInventory().getItemInMainHand().getType() != Material.PAPER) return;
        item = event.getPlayer().getInventory().getItemInMainHand();
        if (!item.getItemMeta().hasLore()) return;
        if (!item.getItemMeta().getLore().get(0).equals("Vortex scroll")) return;

        player = event.getPlayer();
        event.setCancelled(true);

        Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), this);
    }

    @Override
    public void run() {
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
                            entitySetVelocity(entity);
                            return;
                        }
                    }else{
                        entitySetVelocity(entity);
                        return;
                    }
                }
            }
        }
        entitySetVelocity(null);
    }

    private void entitySetVelocity(Entity targetEntity){
        if(targetEntity == null){
            player.sendMessage("Target not defined");
            return;
        }

        Mana playerMana = Mana.getPlayerMap().get(player);
        if(!playerMana.getCdSystem().CDStat(CDSystem.Scrolls.VORTEX, playerMana, 20, 30))return;

        targetEntity.setVelocity(new Vector(0,1.9,0));

        if(!player.getGameMode().equals(GameMode.CREATIVE)) {
            item.setAmount(item.getAmount() - 1);
            player.getInventory().setItemInMainHand(item);
        }
    }
}
