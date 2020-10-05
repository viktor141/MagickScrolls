package ru.vixtor141.MagickScrolls.effects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitTask;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.RitualHandler;

import java.util.List;

public class ItemEffectsHandler implements Runnable{

    private final BukkitTask bukkitTask;
    private final Item item;
    private int i = 0;
    private final Location midLoc;
    private final RitualHandler ritualHandler;

    public ItemEffectsHandler(Item item, Location midLoc, RitualHandler ritualHandler, List<BukkitTask> bukkitTaskList){
        this.item = item;
        this.midLoc = midLoc;
        this.ritualHandler = ritualHandler;
        bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getPlugin(), this, 0, 8);
        bukkitTaskList.add(bukkitTask);
    }

    @Override
    public void run() {
            if(i > 20){
                bukkitTask.cancel();
                return;
            }
            if(i > 18){
                item.remove();
            }
            new UsualAltarItemEffects(item.getLocation(), item.getItemStack(), midLoc.clone().add(0.5,1,0.5));
            i++;
    }
}
