package ru.vixtor141.MagickScrolls.events;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.Misc.CheckUp;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;

import java.util.*;

import static java.lang.Math.*;
import static java.lang.Math.toRadians;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.checkScrollEvent;

public class VortexScroll implements Listener {

    private Player player;
    private ItemStack item;
    private final Main plugin = Main.getPlugin();

    @EventHandler
    public void use(PlayerInteractEvent event) {
        if(checkScrollEvent(event))return;
        item = event.getPlayer().getInventory().getItemInMainHand();
        if(!CheckUp.checkItemLore(ACCrafts.CraftsOfScrolls.VORTEX, item)) return;

        player = event.getPlayer();
        event.setCancelled(true);

        getEntity();
    }

    public void getEntity() {
        Location locationOfPlayer = player.getEyeLocation();
        Location newLocation;

        double newX, newZ, newY;
        float yaw = locationOfPlayer.getYaw();
        float pitch = locationOfPlayer.getPitch();
        newY = 10 * sin(toRadians(pitch));
        newX = 10 * sin(toRadians(yaw)) * cos(toRadians(pitch));
        newZ = 10 * cos(toRadians(yaw)) * cos(toRadians(pitch));
        newLocation = new Location(player.getWorld(), locationOfPlayer.getX() + newX * ((double)-1), locationOfPlayer.getY() + newY * ((double)-1), locationOfPlayer.getZ() + newZ);
        Location start = locationOfPlayer.clone();
        Vector dir = newLocation.clone().subtract(start).toVector();
        for(int i = 1; i < 10; i++) {
            dir.normalize();
            dir.multiply(i);
            start.add(dir);

            Collection<Entity> collectionEntities = player.getWorld().getNearbyEntities(start, 0.5,0.5,0.5);

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
            start.subtract(dir);

        }
        entitySetVelocity(null);
    }

    private void entitySetVelocity(Entity targetEntity){
        if(targetEntity == null){
            player.sendMessage(ChatColor.YELLOW + plugin.getReadingLangFile().getMsg("msg_tind"));
            return;
        }

        CDSystem.Scrolls scroll = CDSystem.Scrolls.VORTEX;
        Mana playerMana = plugin.getPlayerMap().get(player);
        if(!playerMana.getCdSystem().CDStat(scroll, playerMana, ".consumedMana", ".CDseconds", true))return;

        targetEntity.setVelocity(targetEntity.getVelocity().add(new Vector(0,1.9,0)));

        if(!player.getGameMode().equals(GameMode.CREATIVE)) {
            item.setAmount(item.getAmount() - 1);
            player.getInventory().setItemInMainHand(item);
        }
    }
}
