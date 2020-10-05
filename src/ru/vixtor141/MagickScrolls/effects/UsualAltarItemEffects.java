package ru.vixtor141.MagickScrolls.effects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import ru.vixtor141.MagickScrolls.Main;

import static java.lang.Math.*;

public class UsualAltarItemEffects implements Runnable{

    private final Location location;
    private int i = 0, count = 0;
    private final float r = 0.5f;
    private final BukkitTask bukkitTask;
    private final ItemStack itemStack;
    private final Vector to;

    public UsualAltarItemEffects(Location location, ItemStack itemStack, Location toLocation){
        this.location = location;
        this.itemStack = itemStack;
        to = toLocation.clone().subtract(location.clone()).toVector().normalize();
        bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getPlugin(), this, 0, 2);
    }

    @Override
    public void run() {
        if(i > 360){
            bukkitTask.cancel();
            return;
        }
        location.getWorld().spawnParticle(Particle.SPELL_WITCH, location.clone().add(r * cos(toRadians(i)), 0.2 ,r * sin(toRadians(i))), 0, 0,5,0);
        if(count % 4 == 0 ) {
            location.getWorld().spawnParticle(Particle.END_ROD, location, 0, to.getX()/2, to.getY()/2, to.getZ()/2);
        }
        if(count % 4 == 2 ) {
            location.getWorld().spawnParticle(Particle.ITEM_CRACK, location, 0, to.getX() * 0.9, to.getY() * 0.9, to.getZ() * 0.9, itemStack);
        }
        count++;
        i+=18;
    }
}
