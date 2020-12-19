package ru.vixtor141.MagickScrolls.scrolls;

import org.bukkit.*;
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

        Bukkit.getScheduler().runTaskAsynchronously(plugin, this::searchTarget);
    }

    private void searchTarget(){
        Location start = player.getEyeLocation().clone();
        Vector dir = player.getEyeLocation().getDirection();
        for(int i = 1; i < 10; i++) {
            dir.normalize();
            dir.multiply(i);
            start.add(dir);

            Collection<Entity> collectionEntities = player.getWorld().getNearbyEntities(start, 0.5,0.5,0.5);

            if(!collectionEntities.isEmpty()) {
                for(Entity entity : collectionEntities){
                    if(entity == player){
                        continue;
                    }
                    entitySetVelocity(entity);
                    return;
                }
            }
            start.subtract(dir);

        }
        player.sendMessage(ChatColor.YELLOW + LangVar.msg_tind.getVar());
    }

    private void entitySetVelocity(Entity targetEntity){
        CDSystem.Scrolls scroll = CDSystem.Scrolls.VORTEX;
        if(!player.hasMetadata("MagickScrollsMana")){
            player.sendMessage(ChatColor.RED + "WARNING!!! Player: " + player.getDisplayName() + " lost a plugin meta.");
            return;
        }
        Mana playerMana =(Mana) player.getMetadata("MagickScrollsMana").get(0).value();
        if(!playerMana.getCdSystem().CDStat(scroll, true))return;

        vortexEffect(targetEntity);
        targetEntity.setVelocity(targetEntity.getVelocity().add(new Vector(0,1.9,0)));

        itemConsumer(player, item);
    }

    private void vortexEffect(Entity entity){
        for(int i = 1; i < 4; i++){
            vortexEffectR(entity, i);
        }
    }

    private void vortexEffectR(Entity entity, int r){
        for(int i =0; i<=360; i+=24){
            Location particleLocation = entity.getLocation().clone().add(sin(toRadians(i)), 2 - r ,cos(toRadians(i)));
            Vector vector = particleLocation.clone().subtract(entity.getLocation()).toVector();
            particleLocation.getWorld().spawnParticle(Particle.CLOUD, particleLocation, 0, vector.getX()/r, 5, vector.getZ()/r, 0.15);
        }
    }
}
