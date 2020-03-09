package ru.vixtor141.MagickScrolls;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class DefaultEffect {
    private Player player;

    public DefaultEffect(Player player){
        this.player = player;
    }

    public void defaultEffectOfScrolls(){
        player.getWorld().spawnParticle(Particle.DRAGON_BREATH, player.getLocation().add(0, 1.7,0), 30, 0.35,0.35,0.35, 0.03);
        player.getWorld().playSound(player.getLocation().add(0,1,0), Sound.ENTITY_ILLUSION_ILLAGER_CAST_SPELL, 5, (float) 0.5);
    }
}
