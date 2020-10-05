package ru.vixtor141.MagickScrolls.scrolls;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.interfaces.Scroll;
import ru.vixtor141.MagickScrolls.lang.LangVar;

import static java.lang.Math.*;
import static java.lang.Math.toRadians;
import static ru.vixtor141.MagickScrolls.Misc.CheckUp.itemConsumer;

public class Teleportation implements Scroll {

    public Teleportation(Player player, ItemStack item){
        Main plugin = Main.getPlugin();
        Location newLocation = checkForTeleportation(player);
        if(!checkBlock(newLocation)) {
            player.sendMessage(ChatColor.RED + LangVar.msg_yctt.getVar());
            return;
        }

        Mana playerMana = plugin.getPlayerMap().get(player);
        if(System.currentTimeMillis() <= playerMana.getTupaFixCalledTwice()) return;
        playerMana.setTupaFixCalledTwice(System.currentTimeMillis() + 50);
        CDSystem.Scrolls scroll = CDSystem.Scrolls.TELEPORTATION;
        if(!playerMana.getCdSystem().CDStat(scroll, ".consumedMana", ".CDseconds", true))return;

        player.teleport(newLocation);

        itemConsumer(player, item);
    }

    public Location checkForTeleportation(Player player){
        Location locationOfPlayer = player.getEyeLocation();
        float yaw = locationOfPlayer.getYaw();
        float pitch = locationOfPlayer.getPitch();

        locationOfPlayer.add(8 * sin(toRadians(yaw)) * cos(toRadians(pitch)) * (-1), 8 * sin(toRadians(pitch)) * (-1), 8 * cos(toRadians(yaw)) * cos(toRadians(pitch)));

        return locationOfPlayer;
    }

    public boolean checkBlock(Location newLocation){
        return newLocation.getWorld().getBlockAt(newLocation.getBlockX(), newLocation.getBlockY(), newLocation.getBlockZ()).getType() == Material.AIR || newLocation.getWorld().getBlockAt(newLocation.getBlockX(), newLocation.getBlockY() - 1, newLocation.getBlockZ()).getType() == Material.AIR;
    }
}
