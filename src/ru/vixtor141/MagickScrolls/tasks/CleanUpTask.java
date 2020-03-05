package ru.vixtor141.MagickScrolls.tasks;

import net.minecraft.server.v1_12_R1.EntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import ru.vixtor141.MagickScrolls.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CleanUpTask implements Runnable {

    private List<Arrow> arrows;
    private List<LivingEntity> mobs;

    public void arrow(List<Arrow> list){
        arrows = new ArrayList<>(list);
        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getPlugin(), this::arrowCleanUp, 80);
    }

    public void mob(List<LivingEntity> list){
        mobs = new ArrayList<>(list);
        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getPlugin(), this::mobsCleanUp, 3600);
    }

    public void arrowCleanUp() {
        for(Arrow arrow: arrows){
            arrow.remove();
        }
        arrows.clear();
    }

    public void mobsCleanUp() {
        for(LivingEntity livingEntity: mobs){
            if(!livingEntity.isDead()){
                livingEntity.remove();
            }
        }
        mobs.clear();
    }

    @Override
    public void run() {

    }
}
