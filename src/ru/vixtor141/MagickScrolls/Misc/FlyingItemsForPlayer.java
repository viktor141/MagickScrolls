package ru.vixtor141.MagickScrolls.Misc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FlyingItemsForPlayer {

    private final Player player;
    private final Location itemsLoc;
    private List<Item> items;
    private BukkitTask move, search;
    private final Mana playerMana;
    private final int radius = 8;

    public FlyingItemsForPlayer(Mana playerMana, Location itemsLoc){
        this.player = playerMana.getPlayer();
        this.itemsLoc = itemsLoc;
        this.playerMana = playerMana;

        if(player.getLocation().distance(itemsLoc) > radius)return;

        playerMana.getFlyingItemsForPlayer().add(this);
        search = Bukkit.getScheduler().runTaskLater(Main.getPlugin(), this::findItems, 10);
    }

    private void findItems(){
        items = (List<Item>)(List<?>)itemsLoc.getWorld().getNearbyEntities(itemsLoc, 1,1,1).parallelStream().filter(entity -> entity instanceof Item).collect(Collectors.toList());
        move = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(), this::moveItem, 0, 20);
    }

    private void moveItem(){
        if(items.isEmpty()){
            move.cancel();
            playerMana.getFlyingItemsForPlayer().remove(this);
        }
        List<Integer> deadItems = new ArrayList<>();
        for(int i = 0; i<items.size(); i++) {
            Item item = items.get(i);
            if (!item.isDead()) {
                item.setVelocity(player.getLocation().subtract(item.getLocation()).toVector());
            }else {
                deadItems.add(i);
            }
        }
        for(int i = deadItems.size()-1; i> -1 ; i--) {
            items.remove((int)deadItems.get(i));
        }
    }

    public void cancelAll(){
        if(search != null && !search.isCancelled())
            search.cancel();
        if(move != null && !move.isCancelled()){
            move.cancel();
        }
    }
}
