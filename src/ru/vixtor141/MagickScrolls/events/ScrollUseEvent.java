package ru.vixtor141.MagickScrolls.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;

import java.util.List;

public class ScrollUseEvent implements Listener {

    @EventHandler
    public void use(PlayerInteractEvent event){
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(event.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        if(event.getItem() == null)return;
        ItemStack item = event.getItem();
        if(!item.getItemMeta().hasLore() || !(item.getItemMeta().getLore().size() >= 2)) return;

        for(ACCrafts.CraftsOfScrolls scroll : ACCrafts.CraftsOfScrolls.values()){
            List<String> lore = item.getItemMeta().getLore();
            if(lore.get(lore.size() - 2).substring(Main.getPlugin().getSubStr()).equals(scroll.name()) && item.getType().equals(scroll.craftAltarResult().getType())){
                event.setCancelled(true);
                scroll.start(event.getPlayer(), item);
                return;
            }
        }

        for(ACCrafts.Artifacts artifact : ACCrafts.Artifacts.values()){
            if(artifact.isIgnoreUse())continue;
            List<String> lore = item.getItemMeta().getLore();
            if(lore.get(lore.size() - 2).substring(Main.getPlugin().getSubStr()).equals(artifact.name())){
                event.setCancelled(true);
                artifact.use(event.getPlayer(), item);
                return;
            }
        }
    }

}
