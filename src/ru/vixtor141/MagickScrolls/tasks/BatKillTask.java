package ru.vixtor141.MagickScrolls.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import ru.vixtor141.MagickScrolls.Main;

public class BatKillTask {

    private final Entity entity;

    public BatKillTask(Entity entity){
        this.entity = entity;

        Bukkit.getScheduler().runTaskLater(Main.getPlugin(), this::kill, 200);
    }

    private void kill(){
        entity.remove();
    }
}
