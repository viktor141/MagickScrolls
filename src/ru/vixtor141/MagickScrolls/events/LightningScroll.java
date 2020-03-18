package ru.vixtor141.MagickScrolls.events;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftArrow;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftFireball;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftItemFrame;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Crafts;
import ru.vixtor141.MagickScrolls.Mana;

import java.util.List;
import java.util.stream.Collectors;

import static ru.vixtor141.MagickScrolls.Main.readingLangFile;
import static ru.vixtor141.MagickScrolls.Misc.CheckUp.checkScrollEvent;

public class LightningScroll implements Listener {

    @EventHandler
    public void use(PlayerInteractEvent event){
        if(checkScrollEvent(event)){
            return;
        }
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

        Player player = event.getPlayer();

        switch (checkTypeOfScroll(item)){

            case 1: if((strickeLightningEntity(event, player, 5, 1))) return;//add new thread instead methods
            break;

            case 2: if((strickeLightningEntity(event, player, 8, 4))) return;
            break;

            case 3: if((strickeLightningEntity(event, player, 10, 8))) return;
            break;

            case 0: return;
        }

        if(!player.getGameMode().equals(GameMode.CREATIVE)) {
            item.setAmount(item.getAmount() - 1);
            event.getPlayer().getInventory().setItemInMainHand(item);
        }

    }

    private int checkTypeOfScroll(ItemStack item){
        if(Crafts.ScrollsCrafts.LIGHTNINGONE.craftScroll(false).getItemMeta().getLore().equals(item.getItemMeta().getLore())){
            return 1;
        }else if(Crafts.ScrollsCrafts.LIGHTNINGTWO.craftScroll(false).getItemMeta().getLore().equals(item.getItemMeta().getLore())){
            return 2;
        }else if(Crafts.ScrollsCrafts.LIGHTNINGTHREE.craftScroll(false).getItemMeta().getLore().equals(item.getItemMeta().getLore())){
            return 3;
        }else {
            return 0;
        }
    }

    private boolean strickeLightningEntity(PlayerInteractEvent event, Player player, int bound, int numberOfEntities){
        event.setCancelled(true);
        List<Entity> entitesInLocation = player.getNearbyEntities(bound,bound,bound);
        Entity entity;
        Mana playerMana = Mana.getPlayerMap().get(player);
        if (entitesInLocation.isEmpty()) {
            player.sendMessage(ChatColor.YELLOW + readingLangFile.msg_nmay);
            return true;
        }
        entitesInLocation = entitesInLocation.parallelStream().filter(e ->!(e instanceof CraftItemFrame) && !(e instanceof CraftFireball) && !(e instanceof CraftArmorStand) && !(e instanceof CraftArrow)).collect(Collectors.toList());
        if (entitesInLocation.isEmpty()) {
            player.sendMessage(ChatColor.YELLOW + readingLangFile.msg_nmay);
            return true;
        }
        if(!playerMana.getCdSystem().CDStat(CDSystem.Scrolls.LIGHTNING, playerMana, numberOfEntities * 5, (int) (20 + 30 * Math.log10(numberOfEntities))))return true;

        for(int i = 0; i < entitesInLocation.size(); i++) {

                entity = entitesInLocation.get(i);
                entity.getLocation().getWorld().strikeLightning(entity.getLocation()).setSilent(true);
                if(i == numberOfEntities-1)return false;
        }
        return true;
    }
}
