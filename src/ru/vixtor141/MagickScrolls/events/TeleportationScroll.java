package ru.vixtor141.MagickScrolls.events;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.Mana;

import static java.lang.Math.*;

public class TeleportationScroll implements Listener {

    @EventHandler
    public void use(PlayerInteractEvent event) {
        if (event.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getPlayer().getInventory().getItemInMainHand().getType() != Material.PAPER) return;
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if (!item.getItemMeta().hasLore()) return;
        if (!item.getItemMeta().getLore().get(0).equals("Scroll for teleportation in dimension")) return;


        Player player = event.getPlayer();
        event.setCancelled(true);

        Location newLocation = checkForTeleportation(player);
        if (!checkBlock(newLocation)) {
            player.sendMessage(ChatColor.RED + "Вы не можете туда попасть");
            return;
        }

        Mana playerMana = Mana.getPlayerMap().get(player);
        if(System.currentTimeMillis() <= playerMana.getTupaFixCalledTwice()) return;

        if (!playerMana.consumeMana(2)) return;

        player.teleport(newLocation); //Из за этой хуеты двойное срабатывание https://hub.spigotmc.org/jira/browse/SPIGOT-2478

        playerMana.setTupaFixCalledTwice(System.currentTimeMillis() + 50);

        if(!player.getGameMode().equals(GameMode.CREATIVE)) {
            item.setAmount(item.getAmount() - 1);
            event.getPlayer().getInventory().setItemInMainHand(item);
        }

    }

    public Location checkForTeleportation(Player player){
        Location locationOfPlayer = player.getLocation();
        float yaw = locationOfPlayer.getYaw();
        float pitch = locationOfPlayer.getPitch();

        double newY = 8 * sin(toRadians(pitch));
        double newX = 8 * sin(toRadians(yaw)) * cos(toRadians(pitch));
        double newZ = 8 * cos(toRadians(yaw)) * cos(toRadians(pitch));

        locationOfPlayer.add(newX * (-1), newY * (-1), newZ);

        return locationOfPlayer;
    }

    public boolean checkBlock(Location newLocation){
        return newLocation.getWorld().getBlockAt(newLocation.getBlockX(), newLocation.getBlockY(), newLocation.getBlockZ()).getType() == Material.AIR || newLocation.getWorld().getBlockAt(newLocation.getBlockX(), newLocation.getBlockY() + 1, newLocation.getBlockZ()).getType() == Material.AIR;
    }


}
