package ru.vixtor141.MagickScrolls.effects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.LazyMetadataValue;
import org.bukkit.scheduler.BukkitTask;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.interfaces.Ritual;

public class WitchSpawner implements Runnable {

    private final LivingEntity livingEntity;
    private final BukkitTask bukkitTask;

    public WitchSpawner(Location location, Ritual ritual, int k){
        LivingEntity livingEntity = (LivingEntity) location.getWorld().spawnEntity(location, EntityType.WITCH);
        livingEntity.setMetadata("magickscrolls_ritualWitch", new LazyMetadataValue(Main.getPlugin(), ritual.getEnumRitual()::name));
        livingEntity.setInvulnerable(true);
        livingEntity.setAI(false);
        this.livingEntity = livingEntity;
        bukkitTask = Bukkit.getScheduler().runTaskLater(Main.getPlugin(), this, 180 - (k * 20));
    }

    public void killall() {
        if(!bukkitTask.isCancelled())bukkitTask.cancel();
        if(!livingEntity.isDead()) livingEntity.remove();
    }

    @Override
    public void run() {
        livingEntity.remove();
    }
}
