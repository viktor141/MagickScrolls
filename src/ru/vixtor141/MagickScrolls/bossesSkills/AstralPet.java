package ru.vixtor141.MagickScrolls.bossesSkills;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.bosses.Boss;
import ru.vixtor141.MagickScrolls.interfaces.BossSkill;


public class AstralPet implements BossSkill {

    private final Main plugin = Main.getPlugin();
    private final Skill skill = Skill.ASTRAL_PET_SKILL;
    private final BukkitTask liveTask, damageTask;
    private final Player target;
    private final Entity bossEntity;
    private final Boss boss;
    private final int countMax, high = 4;
    private final double damage;
    private int count = 0;


    public AstralPet(Entity bossEntity, Boss boss, Player target){
        this.target = target;
        this.boss = boss;
        this.bossEntity = bossEntity;
        countMax = skill.getTime(boss) * 4;
        damage = plugin.getConfig().getDouble(boss.name() + ".skills.ASTRAL_PET_SKILL.damage");
        liveTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getPlugin(), this::liveTask, 0, 5);
        damageTask = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(), this::damageTask, 0, 20);
    }

    private void liveTask() {
        if(count >= countMax){
            damageTask.cancel();
            liveTask.cancel();
        }
        bossEntity.getWorld().spawnParticle(Particle.CLOUD, bossEntity.getLocation().add(0, high,0), 20, 0.4,0.4,0.4, 0.0001);
        bossEntity.getWorld().spawnParticle(Particle.FLAME, bossEntity.getLocation().add(0, high,0), 5, 0,0,0, 0.05);
        bossEntity.getWorld().spawnParticle(Particle.DRAGON_BREATH, bossEntity.getLocation().add(0, high,0), 5, 0,0,0, 0.05);
        count++;
    }

    private void damageTask(){
        if(target.getLocation().distance(bossEntity.getLocation()) > 10)return;

        final Location toEntity = target.getEyeLocation();

        Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), () -> {
            Location start = bossEntity.getLocation().clone().add(0, high,0);
            Vector dir = toEntity.clone().subtract(start).toVector();
            int amountOfSteps = 10;
            float distance = (float) start.distance(toEntity),  step = distance/amountOfSteps;
            for(float i = step; i < distance; i+= step) {
                dir.normalize();
                dir.multiply(i);
                start.add(dir);
                bossEntity.getWorld().spawnParticle(Particle.SPELL_WITCH , start, 1);
                start.subtract(dir);
            }
        });

        target.damage(damage, bossEntity);
    }

    public void stop() {
        liveTask.cancel();
        damageTask.cancel();
    }
}
