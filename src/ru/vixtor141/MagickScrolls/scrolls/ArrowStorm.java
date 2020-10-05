package ru.vixtor141.MagickScrolls.scrolls;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.interfaces.Scroll;
import ru.vixtor141.MagickScrolls.tasks.CleanUpTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.pow;
import static ru.vixtor141.MagickScrolls.Misc.CheckUp.itemConsumer;

public class ArrowStorm implements Scroll, Runnable {

    private final Player player;
    private final List<Arrow> arrows = new ArrayList<>();

    public ArrowStorm(Player player, ItemStack item){
        this.player = player;

        Mana playerMana = Main.getPlugin().getPlayerMap().get(player);
        if(!playerMana.getCdSystem().CDStat(CDSystem.Scrolls.ARROWSTORM ,".consumedMana", ".CDseconds", true))return;

        Bukkit.getScheduler().runTask(Main.getPlugin(), this);

        itemConsumer(player, item);
    }

    @Override
    public void run() {
        Random random = new Random(System.currentTimeMillis());
        for(int x = - 14; x < 15; x++){
            for(int z = - 14; z < 15; z++){
                if(x == 0 && z == 0) continue;
                if (pow( x, 2) + pow( z, 2) <= 13*13 && random.nextInt(11) < 6) {
                    Arrow arrow = player.getWorld().spawnArrow(new Location(player.getWorld(), player.getLocation().getX() + x, player.getLocation().getY() + 30, player.getLocation().getZ() + z), new Vector(0, -3, 0), (float) 4, (float) 0.5);
                    arrow.setSilent(true);
                    arrow.setCustomName(player.getUniqueId().toString()+"magickscrolls");
                    arrows.add(arrow);
                }
            }
        }
        CleanUpTask cleanUpTask = new CleanUpTask();
        cleanUpTask.arrow(arrows);
    }
}

