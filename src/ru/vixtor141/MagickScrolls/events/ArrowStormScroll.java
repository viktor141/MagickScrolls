package ru.vixtor141.MagickScrolls.events;

import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Crafts;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.tasks.CleanUpTask;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.pow;
import static ru.vixtor141.MagickScrolls.Misc.CheckUp.checkScrollEvent;

public class ArrowStormScroll implements Listener, Runnable {

    private Player player;
    private List<Arrow> arrows = new ArrayList<>();


    @EventHandler
    public void use(PlayerInteractEvent event) {
        if(checkScrollEvent(event)){
            return;
        }
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if(!Crafts.ScrollsCrafts.ARROWSTORM.craftScroll(false).getItemMeta().getLore().equals(item.getItemMeta().getLore())) return;

        event.setCancelled(true);
        player = event.getPlayer();

        Mana playerMana = Mana.getPlayerMap().get(player);
        if(!playerMana.getCdSystem().CDStat(CDSystem.Scrolls.ARROWSTORM, playerMana, 15, 20))return;

        Bukkit.getScheduler().runTask(Main.getPlugin(), this);

        if(!player.getGameMode().equals(GameMode.CREATIVE)) {
            item.setAmount(item.getAmount() - 1);
            event.getPlayer().getInventory().setItemInMainHand(item);
        }
    }

    @EventHandler
    public void hitsOfArrows(EntityDamageByEntityEvent event){
        if(!(event.getEntity() instanceof Player))return;
        if(!(event.getDamager() instanceof Arrow))return;
        Arrow arrow = (Arrow) event.getDamager();
        if(arrow.getCustomName() == null)return;
        if(arrow.getCustomName().equals(event.getEntity().getUniqueId().toString()+"magickscrolls")){
            event.setCancelled(true);
        }
    }

    @Override
    public void run() {
        for(int x = - 14; x < 14; x++){
            for(int y = - 14; y < 14; y++){
                if(x == 0 && y ==0) break;
                if (pow( x, 2) + pow( y, 2) <= 12*12 && Math.random() * 10 < 6) {
                    Arrow arrow = player.getWorld().spawnArrow(new Location(player.getWorld(), player.getLocation().getX() + x, player.getLocation().getY() + 30, player.getLocation().getZ() + y), new Vector(0, -3, 0), (float) 4, (float) 0.5);
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
