package ru.vixtor141.MagickScrolls.Misc;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.Crafts;

import java.util.List;

public class CheckUp {

    public static boolean checkScrollEvent(PlayerInteractEvent event){
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return true;
        if(event.getHand().equals(EquipmentSlot.OFF_HAND)) return true;
        if(!event.getMaterial().equals(Material.PAPER)) return true;
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if(!item.getItemMeta().hasLore() || !(item.getItemMeta().getLore().size() >= 2)) return true;

        return false;
    }

    public static boolean checkItemLore(Crafts.ScrollsCrafts scrollsCrafts, ItemStack item){
        List<String> tLore = scrollsCrafts.craftScroll(false).getItemMeta().getLore();
        List<String> lore = item.getItemMeta().getLore();
        return tLore.get(tLore.size() - 2).equals(lore.get(lore.size() - 2));
    }
}
