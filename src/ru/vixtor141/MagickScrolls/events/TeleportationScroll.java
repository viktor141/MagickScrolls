package ru.vixtor141.MagickScrolls.events;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static java.lang.Math.*;

public class TeleportationScroll implements Listener {

    @EventHandler
    public void use(PlayerInteractEvent event){
        if(event.getAction() != Action.RIGHT_CLICK_AIR) return;
        if(event.getPlayer().getInventory().getItemInMainHand().getType() != Material.PAPER) return;
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if(!item.getItemMeta().hasLore())return;
        if(!item.getItemMeta().getLore().get(0).equals("Scroll for teleportation in dimension"))return;

        event.setCancelled(true);
        Player player = event.getPlayer();

        Location newLocation = checkForTeleportation(player);
        if(!checkBlock(newLocation)){
            player.sendMessage(ChatColor.RED + "Вы не можете туда попасть");
            return;
        }
        player.teleport(newLocation);

        if(!player.getGameMode().equals(GameMode.CREATIVE)) {
            item.setAmount(item.getAmount() - 1);
            event.getPlayer().getInventory().setItemInMainHand(item);
        }
    }

    private Location checkForTeleportation(Player player){
        Location locationOfPlayer = player.getLocation();
        double newX, newZ, newY;
        float yaw = locationOfPlayer.getYaw();
        float pitch = locationOfPlayer.getPitch();
        newX = 8 * sin(toRadians(yaw));
        newZ = 8 * cos(toRadians(yaw));
        newY = 8 * sin(toRadians(pitch));

        locationOfPlayer.setX(locationOfPlayer.getX() + newX * (-1));
        locationOfPlayer.setZ(locationOfPlayer.getZ() + newZ);
        locationOfPlayer.setY(locationOfPlayer.getY() + newY * (-1));
        return locationOfPlayer;
    }

    private boolean checkBlock(Location newLocation){
        if(newLocation.getWorld().getBlockAt(newLocation.getBlockX(), newLocation.getBlockY(), newLocation.getBlockZ()).getType() != Material.AIR && newLocation.getWorld().getBlockAt(newLocation.getBlockX(), newLocation.getBlockY() +1, newLocation.getBlockZ()).getType() != Material.AIR)return false;
        return true;
    }

}
