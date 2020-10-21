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

    public RandomParticleGenerator(Location location, int r, int count, int red, int green, int blue){
        this.r = r;
        for(int i = 0; i < count; i++){
            location.getWorld().spawnParticle(Particle.REDSTONE, location.clone().add(randValue(),randValue(),randValue()), 0, red, green, blue);
        }
    }

    public RandomParticleGenerator(Location location, int r, int count, double red, double green, double blue){
        this.r = r;
        for(int i = 0; i < count; i++){
            location.getWorld().spawnParticle(Particle.REDSTONE, location.clone().add(randValue(),randValue(),randValue()), 0, red, green, blue);
        }
    }

    private float randValue(){
        return ((float) (new Random().nextInt(r))/10) * (new Random().nextInt(2) * 2 - 1);
    }
}
