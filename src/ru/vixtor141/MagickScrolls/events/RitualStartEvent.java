package ru.vixtor141.MagickScrolls.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.RitualHandler;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;

import java.util.List;

public class RitualStartEvent implements Listener {

    @EventHandler
    public void ritualStart(PlayerInteractEvent event){
        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))return;
        if(event.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        if(!event.getClickedBlock().getType().equals(Material.GOLD_BLOCK))return;
        ItemStack item = event.getItem();
        if(item == null)return;
        if(!item.getType().equals(ACCrafts.ItemsCauldronCrafts.WITCH_ARTIFACT.craftCauldronGetMaterial()))return;
        if(!item.getItemMeta().hasLore() || !(item.getItemMeta().getLore().size() >= 3)) return;
        List<String> lore = item.getItemMeta().getLore();
        if(!ACCrafts.ItemsCauldronCrafts.WITCH_ARTIFACT.name().equals(lore.get(lore.size() - 2).substring(Main.getPlugin().getSubStr())))return;

        event.setCancelled(true);

        new RitualHandler(event.getPlayer(), event.getClickedBlock().getLocation(), Integer.parseInt(lore.get(lore.size() - 3)));
    }

}
