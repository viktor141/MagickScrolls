package ru.vixtor141.MagickScrolls.Misc;

import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class CheckUp {

    public static boolean checkScrollEvent(PlayerInteractEvent event){
        if(event.getHand().equals(EquipmentSlot.OFF_HAND)) return true;
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return true;
        if(event.getPlayer().getInventory().getItemInMainHand().getType() != Material.PAPER) return true;
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if(!item.getItemMeta().hasLore()) return true;

        return false;
    }
}
