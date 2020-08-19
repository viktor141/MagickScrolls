package ru.vixtor141.MagickScrolls.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;

import java.util.List;
import java.util.stream.Collectors;

public class AstralPetTask implements Runnable{

    private final Player player;
    private int count;
    private final int high = 10;
    private final BukkitTask liveTask, damageTask;
    private boolean damageCheck = false;
    private final Main plugin = Main.getPlugin();
    private final double damage = plugin.getConfig().getDouble(ACCrafts.CraftsOfScrolls.ASTRAL_PET.name() + ".damage");
    private final int countMax = plugin.getConfig().getInt(ACCrafts.CraftsOfScrolls.ASTRAL_PET.name() + ".secondsLive") * 4;

    public AstralPetTask(Player player){
        this.player = player;
        count = 0;
        liveTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getPlugin(), this::liveTask, 0, 5);
        damageTask = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(), this::damageTask, 0, 20);
    }

    private void liveTask() {
        if(count >= countMax){
            damageCheck = true;
            liveTask.cancel();
        }
        player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation().add(0, high,0), 20, 0.4,0.4,0.4, 0.0001);
        player.getWorld().spawnParticle(Particle.FLAME, player.getLocation().add(0, high,0), 5, 0,0,0, 0.05);
        player.getWorld().spawnParticle(Particle.DRAGON_BREATH, player.getLocation().add(0, high,0), 5, 0,0,0, 0.05);
        count++;
    }

    private void damageTask(){
        if(damageCheck)damageTask.cancel();
        List<Entity> entityList = player.getNearbyEntities(8,8,8).stream().filter(entity1 -> entity1 instanceof Monster).collect(Collectors.toList());
        if(entityList.isEmpty())return;

        LivingEntity entity = null;

        for(Entity entityCheck : entityList){
            if(!entityCheck.isDead()){
                entity = (LivingEntity) entityCheck;
                break;
            }
        }

        if(entity == null)return;

        final Location toEntity = entity.getEyeLocation();

        Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), () -> {
            Location start = player.getLocation().clone().add(0, high,0);
            Vector dir = toEntity.clone().subtract(start).toVector();
            for(int i = 1; i < 20; i++) {
                dir.normalize();
                dir.multiply(i);
                start.add(dir);
                player.getWorld().spawnParticle(Particle.SPELL_WITCH , start, 1);
                start.subtract(dir);
            }
        });

        entity.damage(damage, player);
    }

    @Override
    public void run() {

    }
}
