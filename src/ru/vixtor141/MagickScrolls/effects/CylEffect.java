package ru.vixtor141.MagickScrolls.effects;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitTask;
import ru.vixtor141.MagickScrolls.Main;

import static java.lang.Math.*;

public class CylEffect{

    private final BukkitTask circleEffect;
    private final float r, high, cylHigh;
    private final Entity entity;
    private final int angleStep;
    private int i = 0;
    private final Particle particle;

    public CylEffect(Entity entity, float r, float high, float cylHigh, int repeatingTick, int angleStep){
        this.entity = entity;
        this.r = r;
        this.high = high;
        this.cylHigh = cylHigh;
        this.angleStep = angleStep;
        this.particle = Particle.SPELL_INSTANT;

        if(angleStep > 0) {
            circleEffect = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getPlugin(), this::positive, 0, repeatingTick);
        }else {
            circleEffect = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getPlugin(), this::negative, 0, repeatingTick);
        }
    }

    public BukkitTask getTask(){
        return circleEffect;
    }

    public void positive() {
        if(i > 360){
            circleEffect.cancel();
            return;
        }
        entity.getWorld().spawnParticle(particle, entity.getLocation().add(r * cos(toRadians(i)), high + cylHigh * sin(toDegrees(i)) ,r * sin(toRadians(i))), 0, 0,0,0, 0.015);
        i+=angleStep;
    }

    public void negative() {
        if(i < -360){
            circleEffect.cancel();
            return;
        }
        entity.getWorld().spawnParticle(particle, entity.getLocation().add(r * cos(toRadians(i)), high + cylHigh * sin(toDegrees(i*-1)) ,r * sin(toRadians(i))), 0, 0,0,0, 0.015);
        i+=angleStep;
    }
}
