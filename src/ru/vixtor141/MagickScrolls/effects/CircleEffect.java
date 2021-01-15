package ru.vixtor141.MagickScrolls.effects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitTask;
import ru.vixtor141.MagickScrolls.Main;

import static java.lang.Math.*;

public class CircleEffect implements Runnable {

    private final BukkitTask circleEffect;
    private final float r, high;
    private final Location circleCentre;
    private final int angleStep;
    private int i = 0;

    public CircleEffect(Location circleCentre, float r, float high, int repeatingTick, int angleStep){
        this.circleCentre = circleCentre;
        this.r = r;
        this.high = high;
        this.angleStep = angleStep;

        circleEffect = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getPlugin(), this, 0, repeatingTick);
    }

    public BukkitTask getTask(){
        return circleEffect;
    }

    @Override
    public void run() {
        if(i > 360){
            circleEffect.cancel();
            return;
        }
        circleCentre.getWorld().spawnParticle(Particle.FLAME, circleCentre.clone().add(r * cos(toRadians(i)), high ,r * sin(toRadians(i))), 0, 0,5,0, 0.015);
        i+=angleStep;
    }
}
