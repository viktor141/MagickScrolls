package ru.vixtor141.MagickScrolls.effects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import ru.vixtor141.MagickScrolls.Main;

public class ShootingStarEffect {

    private final BukkitTask ray;
    private float k;
    private final float distance,  step, speed;
    private final Location startRay;
    private final Vector dirRay;
    private final int minusSteps;


    public ShootingStarEffect(Location startRay, Location endRay, int amountOfSteps, float speed, int repeating, int minusSteps){
        this.speed = speed;
        this.startRay = startRay.clone();
        this.minusSteps = minusSteps;
        dirRay = endRay.clone().subtract(startRay).toVector();

        distance = (float) endRay.distance(startRay);
        step = distance/amountOfSteps;
        k = step;

        ray = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getPlugin(), this::ray, 0, repeating);
    }

    public ShootingStarEffect(Location startRay, Location endRay, int amountOfSteps){
        this(startRay, endRay, amountOfSteps, 0.05f, 4, 5);
    }

    private void ray(){
        dirRay.normalize();
        dirRay.multiply(k);
        startRay.add(dirRay);

        startRay.getWorld().spawnParticle(Particle.FLAME, startRay, 0, dirRay.getX(), dirRay.getY(), dirRay.getZ(), speed);

        startRay.subtract(dirRay);
        if(k > distance - (step * minusSteps)){
            ray.cancel();
            return;
        }
        k+=step;
    }
}
