package ru.vixtor141.MagickScrolls.effects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import ru.vixtor141.MagickScrolls.Main;

public class ParticleRay {

    private BukkitTask ray;
    private float k;
    private final float distance,  step;
    private final Location startRay;
    private final Vector dirRay;
    private final Particle particle;
    private final int repeating, wait;


    public ParticleRay(Location startRay, Location endRay, int amountOfSteps, int repeating, Particle particle, int wait){
        this.startRay = startRay.clone();
        this.particle = particle;
        this.repeating = repeating;
        this.wait = wait;
        dirRay = endRay.clone().subtract(startRay).toVector();

        distance = (float) endRay.distance(startRay);
        step = distance/amountOfSteps;
        k = step;
    }

    public void start(){
        ray = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getPlugin(), this::ray, wait*3, repeating);
    }

    private void ray(){
        dirRay.normalize();
        dirRay.multiply(k);
        startRay.add(dirRay);

        startRay.getWorld().spawnParticle(particle, startRay, 0, 0, 0, 0, 0.5);

        startRay.subtract(dirRay);
        if(k > distance){
            ray.cancel();
            return;
        }
        k+=step;
    }
}
