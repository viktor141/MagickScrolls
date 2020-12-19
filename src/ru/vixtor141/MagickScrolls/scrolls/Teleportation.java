package ru.vixtor141.MagickScrolls.scrolls;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.interfaces.Scroll;
import ru.vixtor141.MagickScrolls.lang.LangVar;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.itemConsumer;

public class Teleportation implements Scroll {

    public Teleportation(Player player, ItemStack item){
        Main plugin = Main.getPlugin();
        Location newLocation = checkForTeleportation(player);
        if(!checkBlock(newLocation)) {
            player.sendMessage(ChatColor.RED + LangVar.msg_yctt.getVar());
            return;
        }

        if(!player.hasMetadata("MagickScrollsMana")){
            player.sendMessage(ChatColor.RED + "WARNING!!! Player: " + player.getDisplayName() + " lost a plugin meta.");
            return;
        }
        Mana playerMana =(Mana) player.getMetadata("MagickScrollsMana").get(0).value();
        if(System.currentTimeMillis() <= playerMana.getTupaFixCalledTwice()) return;
        playerMana.setTupaFixCalledTwice(System.currentTimeMillis() + 50);
        CDSystem.Scrolls scroll = CDSystem.Scrolls.TELEPORTATION;
        if(!playerMana.getCdSystem().CDStat(scroll, true))return;

        player.teleport(newLocation);
        newLocation.getWorld().spawnParticle(Particle.SPELL_INSTANT, newLocation.clone().add(0,1,0), 15,1,1,1, 2);

        itemConsumer(player, item);
    }

    public Location checkForTeleportation(Player player){
        Location locationOfPlayer = player.getLocation();
        locationOfPlayer.add(player.getEyeLocation().getDirection().clone().normalize().multiply(8));
        return locationOfPlayer;
    }

    public boolean checkBlock(Location newLocation){
        return newLocation.getWorld().getBlockAt(newLocation.getBlockX(), newLocation.getBlockY(), newLocation.getBlockZ()).getType() == Material.AIR || newLocation.getWorld().getBlockAt(newLocation.getBlockX(), newLocation.getBlockY() - 1, newLocation.getBlockZ()).getType() == Material.AIR;
    }
}
