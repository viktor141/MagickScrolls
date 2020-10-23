package ru.vixtor141.MagickScrolls.Misc;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import ru.vixtor141.MagickScrolls.Main;

public class StartEffectForSpectralShield {

    private final BukkitTask bukkitTask;
    private final Player player;

    public StartEffectForSpectralShield(Player player){
        this.player = player;
        bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getPlugin(), this::spectralShieldEffect, 0, 5);
    }

    public BukkitTask getBukkitTask(){
        return bukkitTask;
    }

    private void spectralShieldEffect(){
        player.getWorld().spawnParticle(Particle.SPELL_WITCH, player.getEyeLocation().clone().add(0,1, 0), 4, 1,1,1);
    }
}
