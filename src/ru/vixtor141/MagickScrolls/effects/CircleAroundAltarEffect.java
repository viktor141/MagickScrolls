package ru.vixtor141.MagickScrolls.effects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitTask;
import ru.vixtor141.MagickScrolls.Main;

import static java.lang.Math.*;

public class CircleAroundAltarEffect implements Runnable {

    private final BukkitTask bukkitTask;
    private int i = 0;
    private final int d;
    private final Location location;
    private final float r;

    public CircleAroundAltarEffect(Location location, float r, int d){
        this.location = location;
        this.r = r;
        this.d = d;
        i -= d;
        bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getPlugin(), this, 0, 1);
    }

    @Override
    public void run() {
        if(i > 360 - d){
            bukkitTask.cancel();
            return;
        }
        location.getWorld().spawnParticle(Particle.SPELL_WITCH, location.clone().add((r * cos(toRadians(i))) + 0.5, 2 ,(r * sin(toRadians(i))) + 0.5), 2, 0.3,0.3,0.3);
        location.getWorld().spawnParticle(Particle.END_ROD, location.clone().add((r * cos(toRadians(i))) + 0.5, 1, (r * sin(toRadians(i))) + 0.5), 0, 0, 0.5, 0);
        i+=4;
    }
}
