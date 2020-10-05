package ru.vixtor141.MagickScrolls.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;

import java.util.List;

public class ScrollUseEvent implements Listener {

    @EventHandler
    public void use(PlayerInteractEvent event){
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(event.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        if(!event.getMaterial().equals(Material.PAPER)) return;
        ItemStack item = event.getItem();
        if(!item.getItemMeta().hasLore() || !(item.getItemMeta().getLore().size() >= 2)) return;

        for(ACCrafts.CraftsOfScrolls scroll : ACCrafts.CraftsOfScrolls.values()){
            List<String> tLore = scroll.craftAltarResult().getItemMeta().getLore();
            List<String> lore = item.getItemMeta().getLore();
            if(tLore.get(tLore.size() - 2).equals(lore.get(lore.size() - 2))){
                event.setCancelled(true);
                scroll.start(event.getPlayer(), item);
                break;
            }
        }
    }

}
