package ru.vixtor141.MagickScrolls.bossesSkills;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.bosses.Boss;
import ru.vixtor141.MagickScrolls.interfaces.BossSkill;

import java.util.Random;

public class DodgeMove implements BossSkill {

    private final Skill skill = Skill.MOVE_DODGE;
    private final Entity entity, damageE;
    private final BukkitTask bukkitTask;
    private int counter = 0;
    private final Boss boss;

    public DodgeMove(Entity entity, Boss boss, Entity damageE){
        this.entity = entity;
        this.boss = boss;
        this.damageE = damageE;
        bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getPlugin(), this::setRandomVector, 0, 20);
    }

    private void setRandomVector(){
        if(counter > skill.getTime(boss) && entity.isDead()){
            bukkitTask.cancel();
        }
        if(!damageE.isDead()){
            entity.setVelocity(damageE.getLocation().subtract(entity.getLocation()).toVector().normalize());
        }
        counter++;
    }

    public BukkitTask getBukkitTask() {
        return bukkitTask;
    }
}
