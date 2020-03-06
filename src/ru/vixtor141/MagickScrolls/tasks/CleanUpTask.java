package ru.vixtor141.MagickScrolls.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import ru.vixtor141.MagickScrolls.Main;

import java.util.ArrayList;
import java.util.List;

public class CleanUpTask implements Runnable {

    private List<Arrow> arrows;
    private List<LivingEntity> mobs;
    private static List<LivingEntity> existMobs = new ArrayList<>();

    public List<LivingEntity> getExistMobs(){
        return existMobs;
    }

    public void arrow(List<Arrow> list){
        arrows = new ArrayList<>(list);
        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getPlugin(), this::arrowCleanUp, 80);
    }

    public void mob(List<LivingEntity> list){
        mobs = new ArrayList<>(list);
        existMobs.addAll(list);
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
        existMobs.removeAll(mobs);
    }

    @Override
    public void run() {

    }
}
