package ru.vixtor141.MagickScrolls.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitTask;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;

import java.util.ArrayList;
import java.util.List;

public class CleanUpTask implements Runnable {

    private List<Arrow> arrows;
    private List<LivingEntity> mobs;
    private static List<LivingEntity> existMobs = new ArrayList<>();
    private static List<CleanUpTask> cleanUpTasks = new ArrayList<>();
    private Mana playerMana;
    private Location location;
    private BlockState[] blockStates;
    private BukkitTask sWebTask;

    public static List<CleanUpTask> getCleanUpTasks(){
        return cleanUpTasks;
    }

    public static List<LivingEntity> getExistMobs(){
        return existMobs;
    }

    public BukkitTask getsWebTask() {
        return sWebTask;
    }

    public void arrow(List<Arrow> list){
        arrows = new ArrayList<>(list);
        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getPlugin(), this::arrowCleanUp, 80);
    }

    public void mob(List<LivingEntity> list, Mana playerMana){
        mobs = new ArrayList<>(list);
        this.playerMana = playerMana;
        existMobs.addAll(list);
        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getPlugin(), this::mobsCleanUp, 3600);
    }

    public void sWeb(Location location, BlockState[] blockStates, Mana playerMana){
        this.location = location;
        this.blockStates = blockStates;
        this.playerMana = playerMana;
        sWebTask = Bukkit.getScheduler().runTaskLater(Main.getPlugin(), this::sWebCleanUp, 200);
        cleanUpTasks.add(this);
    }

    private void arrowCleanUp() {
        for(Arrow arrow: arrows){
            if(!arrow.isDead()) {
                arrow.remove();
            }
        }
        arrows.clear();
    }

    private void mobsCleanUp() {
        for(LivingEntity livingEntity: mobs){
            if(!livingEntity.isDead()){
                livingEntity.remove();
            }
        }
        mobs.clear();
        existMobs.removeAll(mobs);
        playerMana.getExistMobs().removeAll(mobs);
    }

    public void sWebCleanUp() {
        for(int i = 0; i < 2; i++) {
            if(blockStates[i] == null)continue;
            Block block = location.add(0, i , 0).getBlock();
            if(!block.getType().equals(Material.WEB))continue;
            block.setType(blockStates[i].getType());
            block.getState().setData(blockStates[i].getData());
            blockStates[i].update();
        }
        cleanUpTasks.remove(this);
    }
    public void sWebCleanUpOnDisable() {
        for(int i = 0; i < 2; i++) {
            if(blockStates[i] == null)continue;
            Block block = location.add(0, i , 0).getBlock();
            if(!block.getType().equals(Material.WEB))continue;
            block.setType(blockStates[i].getType());
            block.getState().setData(blockStates[i].getData());
            blockStates[i].update();
        }
    }

    @Override
    public void run() {

    }

}
