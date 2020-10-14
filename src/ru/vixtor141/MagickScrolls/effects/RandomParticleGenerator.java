package ru.vixtor141.MagickScrolls.effects;

import org.bukkit.Location;
import org.bukkit.Particle;

import java.util.Random;

public class RandomParticleGenerator {

    private final int r;

    public RandomParticleGenerator(Location location, Particle particle, int r, int count){
        this.r = r;
        for(int i = 0; i < count; i++){
            location.getWorld().spawnParticle(particle, location.clone().add(randValue(),randValue(),randValue()), 0, 0,0,0);
        }
    }

    private float randValue(){
        return ((float) (new Random().nextInt(r))/10) * (new Random().nextInt(2) * 2 - 1);
    }
}
