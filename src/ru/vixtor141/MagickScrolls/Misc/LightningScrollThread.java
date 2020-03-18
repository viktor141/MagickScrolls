package ru.vixtor141.MagickScrolls.Misc;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftArrow;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftFireball;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftItemFrame;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Mana;

import java.util.List;
import java.util.stream.Collectors;

import static ru.vixtor141.MagickScrolls.Main.readingLangFile;

public class LightningScrollThread extends Thread {

    private Player player;
    private PlayerInteractEvent event;
    private double bound;
    private int numberOfEntities;
    private ItemStack item;

    public LightningScrollThread(PlayerInteractEvent event, Player player, double bound, int numberOfEntities, ItemStack item){
        this.player = player;
        this.event = event;
        this.bound = bound;
        this.numberOfEntities = numberOfEntities;
        this.item = item;
    }

    @Override
    public void run() {
        event.setCancelled(true);
        List<Entity> entitesInLocation = player.getNearbyEntities(bound,bound,bound);
        Entity entity;
        Mana playerMana = Mana.getPlayerMap().get(player);
        if (entitesInLocation.isEmpty()) {
            player.sendMessage(ChatColor.YELLOW + readingLangFile.msg_nmay);
            return;
        }
        entitesInLocation = entitesInLocation.parallelStream().filter(e ->!(e instanceof CraftItemFrame) && !(e instanceof CraftFireball) && !(e instanceof CraftArmorStand) && !(e instanceof CraftArrow)).collect(Collectors.toList());
        if (entitesInLocation.isEmpty()) {
            player.sendMessage(ChatColor.YELLOW + readingLangFile.msg_nmay);
            return;
        }
        if(!playerMana.getCdSystem().CDStat(CDSystem.Scrolls.LIGHTNING, playerMana, numberOfEntities * 5, (int) (20 + 30 * Math.log10(numberOfEntities))))return;


        for(int i = 0; i < entitesInLocation.size(); i++) {

            entity = entitesInLocation.get(i);
            entity.getLocation().getWorld().strikeLightning(entity.getLocation()).setSilent(true);
            if(i == numberOfEntities-1){
                if(!player.getGameMode().equals(GameMode.CREATIVE)) {
                    item.setAmount(item.getAmount() - 1);
                    event.getPlayer().getInventory().setItemInMainHand(item);
                }
            }
        }

    }
}
