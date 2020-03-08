package ru.vixtor141.MagickScrolls.events;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.Mana;

import java.util.List;

public class LightningScroll implements Listener {

    @EventHandler
    public void use(PlayerInteractEvent event){
        if(event.getAction() != Action.RIGHT_CLICK_AIR) return;
        if(event.getPlayer().getInventory().getItemInMainHand().getType() != Material.PAPER) return;
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if(!item.getItemMeta().hasLore())return;


        if(!item.getItemMeta().getLore().get(0).equals("Scroll for lightning strike")){
            if(!item.getItemMeta().getLore().get(0).equals("Scroll for lightning strike power two")){
                if(!item.getItemMeta().getLore().get(0).equals("Scroll for lightning strike power three"))return;
            }
        }



        event.setCancelled(true);
        Player player = event.getPlayer();



        switch (checkTypeOfScroll(item)){

            case 1: if((strickeLightningEntity(player, 6, 1))) return;
            break;

            case 2: if((strickeLightningEntity(player, 10, 4))) return;
            break;

            case 3: if((strickeLightningEntity(player, 15, 8))) return;
            break;

            case 0: player.sendMessage(ChatColor.RED + "Unexpected errore"); return;

        }



        if(!player.getGameMode().equals(GameMode.CREATIVE)) {
            item.setAmount(item.getAmount() - 1);
            event.getPlayer().getInventory().setItemInMainHand(item);
        }

    }

    private int checkTypeOfScroll(ItemStack item){

        switch (item.getItemMeta().getLore().get(0)) {
            case "Scroll for lightning strike":
                return 1;
            case "Scroll for lightning strike power two":
                return 2;
            case "Scroll for lightning strike power three":
                return 3;
        }
        return 0;
    }

    private boolean strickeLightningEntity(Player player, int bound, int numberOfEntities){
        List<Entity> entityLocations = player.getNearbyEntities(bound,bound,bound);
        Entity entity;
        Mana playerMana = Mana.getPlayerMap().get(player);
        if (entityLocations.isEmpty()) {
            player.sendMessage(ChatColor.LIGHT_PURPLE + "No mobs around you");
            return true;
        }
        if(!playerMana.consumeMana(numberOfEntities * 5)){
            return true;
        }

        playerMana.getDefaultEffect().defaultEffectOfScrolls(player);
        for(int i = 0; i < entityLocations.size(); i++) {

                entity = entityLocations.get(i);
                entity.getLocation().getWorld().strikeLightning(entity.getLocation()).setSilent(true);
                if(i == numberOfEntities-1)return false;
        }
        return true;
    }
}
