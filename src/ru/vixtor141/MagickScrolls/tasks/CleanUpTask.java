package ru.vixtor141.MagickScrolls.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import ru.vixtor141.MagickScrolls.Main;

import java.util.ArrayList;
import java.util.List;

public class CleanUpTask implements Runnable {

    private List<Arrow> arrows;

    public void arrow(List<Arrow> list){
        arrows = new ArrayList<>(list);
        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getPlugin(), this::arrowCleanUp, 80);
    }

    public void arrowCleanUp() {
        for(Arrow arrow: arrows){
            arrow.remove();
        }
        arrows.clear();
    }

    @Override
    public void run() {

    }
}
