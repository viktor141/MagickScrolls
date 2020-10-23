package ru.vixtor141.MagickScrolls.effects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitTask;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.RitualHandler;
import ru.vixtor141.MagickScrolls.interfaces.Ritual;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.*;
import static java.lang.Math.toRadians;

public class UsualAltarEffects implements Runnable {

    private final Main plugin = Main.getPlugin();
    private final float r;
    private final Ritual ritual;
    private final Location location;
    private int j = 0, i = 0, k = 0, d = 0;
    private final BukkitTask repeaterEffects, itemsRepeaterEffects, witchSpawnerEffect, shootingStarEffect;
    private final int secondsOfRitual = 10 * 2;
    private final List<Item> list;
    private final RitualHandler ritualHandler;
    private final List<BukkitTask> bukkitTaskList = new ArrayList<>();
    private final int witchCount;
    private final List<WitchSpawner> witchSpawners = new ArrayList<>();
    private BukkitTask ritualEndTask;

    public UsualAltarEffects(Ritual ritual, float r, Location location, List<Item> list, RitualHandler ritualHandler, int witchCount){
        this.r = r;
        this.ritual = ritual;
        this.location = location;
        this.list = new ArrayList<>(list);
        this.ritualHandler = ritualHandler;
        this.witchCount = witchCount;
        repeaterEffects = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::repeaterEffects, 0, 10);
        itemsRepeaterEffects = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::onItemsRepeaterEffects, 0, 20);
        witchSpawnerEffect = Bukkit.getScheduler().runTaskTimer(plugin, this::witchSpawning, 0, 20);
        shootingStarEffect = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::shootingStarEffect, 0, 2);
    }

    private void witchSpawning(){
        if(k > witchCount - 1){
            witchSpawnerEffect.cancel();
            return;
        }
        int rndLoc = new Random().nextInt(4);
        witchSpawners.add(new WitchSpawner(location.clone().add(r * cos(toRadians(rndLoc * 90)) + 0.5, 1, r * sin(toRadians(rndLoc * 90)) + 0.5), ritual, k));
        k++;
    }


    public void ritualBreaking(){
        if(ritualEndTask != null)ritualEndTask.cancel();
        repeaterEffects.cancel();
        itemsRepeaterEffects.cancel();
        witchSpawnerEffect.cancel();
        shootingStarEffect.cancel();
        for(BukkitTask bukkitTask: bukkitTaskList)bukkitTask.cancel();
        for(WitchSpawner witchSpawner : witchSpawners)witchSpawner.killall();
    }

    private void onItemsRepeaterEffects(){
        if(i > list.size() - 1){
            itemsRepeaterEffects.cancel();
            ritualEndTask = Bukkit.getScheduler().runTaskLater(Main.getPlugin(), ritualHandler::ritualEnd, 140);
            return;
        }
        new ItemEffectsHandler(list.get(i), location.clone().add(0 ,1,0), ritualHandler, bukkitTaskList);
        i++;
    }

    private void repeaterEffects(){
        if(j > secondsOfRitual){
            repeaterEffects.cancel();//можно убивать все таски
            shootingStarEffect.cancel();
            return;
        }
        new CircleAroundAltarEffect(location, r + 1, (360/9) * j);
        ritual.repeatingEffect();
        j++;
    }

    private void shootingStarEffect(){
        new ShootingStarEffect(list.get(d).getLocation().clone().add(0,0.2,0), 20, 6, 25);
        d++;
        if(d == list.size()){
            d = 0;
        }

    }

    @Override
    public void run() {

    }

}
