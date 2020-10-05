package ru.vixtor141.MagickScrolls.scrolls;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.interfaces.Scroll;
import ru.vixtor141.MagickScrolls.lang.LangVar;

import java.util.Collection;

import static java.lang.Math.*;
import static java.lang.Math.toRadians;
import static ru.vixtor141.MagickScrolls.Misc.CheckUp.itemConsumer;

public class Vortex implements Scroll {

    private final Player player;
    private final ItemStack item;
    private final Main plugin = Main.getPlugin();

    public Vortex(Player player, ItemStack item){
        this.player = player;
        this.item = item;

        Location locationOfPlayer = player.getEyeLocation();
        Location newLocation;

        int r = 10;
        float yaw = locationOfPlayer.getYaw();
        float pitch = locationOfPlayer.getPitch();
        newLocation = new Location(player.getWorld(), locationOfPlayer.getX() + (r * sin(toRadians(yaw)) * cos(toRadians(pitch)) * ((double)-1)), locationOfPlayer.getY() + (r * sin(toRadians(pitch)) * ((double)-1)), locationOfPlayer.getZ() + (r * cos(toRadians(yaw)) * cos(toRadians(pitch))));
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
            player.sendMessage(ChatColor.YELLOW + LangVar.msg_tind.getVar());
            return;
        }

        CDSystem.Scrolls scroll = CDSystem.Scrolls.VORTEX;
        Mana playerMana = plugin.getPlayerMap().get(player);
        if(!playerMana.getCdSystem().CDStat(scroll, ".consumedMana", ".CDseconds", true))return;

        targetEntity.setVelocity(targetEntity.getVelocity().add(new Vector(0,1.9,0)));

        itemConsumer(player, item);
    }
}
