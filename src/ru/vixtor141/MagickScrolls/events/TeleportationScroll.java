package ru.vixtor141.MagickScrolls.events;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Crafts;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;

import static java.lang.Math.*;
import static ru.vixtor141.MagickScrolls.Main.readingLangFile;
import static ru.vixtor141.MagickScrolls.Misc.CheckUp.checkScrollEvent;

public class TeleportationScroll implements Listener {

    @EventHandler
    public void use(PlayerInteractEvent event) {
        if(checkScrollEvent(event))return;
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if(!Crafts.ScrollsCrafts.TELEPORTATION.craftScroll(false).getItemMeta().getLore().equals(item.getItemMeta().getLore())) return;


        Player player = event.getPlayer();
        event.setCancelled(true);

        Location newLocation = checkForTeleportation(player);
        if(!checkBlock(newLocation)) {
            player.sendMessage(ChatColor.RED + readingLangFile.msg_yctt);
            return;
        }


        Mana playerMana = Mana.getPlayerMap().get(player);
        if(System.currentTimeMillis() <= playerMana.getTupaFixCalledTwice()) return;
        playerMana.setTupaFixCalledTwice(System.currentTimeMillis() + 50);
        Main plugin = Main.getPlugin();
        CDSystem.Scrolls scroll = CDSystem.Scrolls.TELEPORTATION;
        if(!playerMana.getCdSystem().CDStat(scroll, playerMana, plugin.getConfig().getDouble(scroll.name() + ".consumedMana") , plugin.getConfig().getInt(scroll.name() + ".CDseconds"), false))return;

        player.teleport(newLocation);

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
