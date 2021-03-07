package ru.vixtor141.MagickScrolls.effects;

import org.bukkit.Location;
import org.bukkit.Particle;
import java.util.Random;

public class EndrodSnowEffect {

    private int r;

    public EndrodSnowEffect(int r, int count, Location location){
        this.r = r;
        for(int i = 0; i < count; i++){
            location.getWorld().spawnParticle(Particle.END_ROD, location.clone(), 0, randValue(),randValue(),randValue(), 0.2);
        }
    }

    private float randValue(){
        return ((float) (new Random().nextInt(r))/10) * (new Random().nextInt(2) * 2 - 1);
    }
}
