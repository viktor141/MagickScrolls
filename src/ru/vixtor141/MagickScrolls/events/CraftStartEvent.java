package ru.vixtor141.MagickScrolls.events;

import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Cauldron;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;
import ru.vixtor141.MagickScrolls.crafts.AltarCrafting;
import ru.vixtor141.MagickScrolls.crafts.CauldronCrafting;

import java.util.List;


public class CraftStartEvent implements Listener {

    @EventHandler
    public void altarUse(PlayerInteractEvent event){
        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if(event.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        if(!event.getClickedBlock().getType().equals(Material.ENCHANTMENT_TABLE))return;
        if(!event.getMaterial().equals(Material.STICK)) return;
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if(!item.getItemMeta().hasLore() || !(item.getItemMeta().getLore().size() >= 2))return;
        List<String> tLore = ACCrafts.ItemsCauldronCrafts.MAGIC_STAFF.craftCauldronGetItem().getItemMeta().getLore();
        List<String> lore = item.getItemMeta().getLore();
        if(!tLore.get(tLore.size() - 2).equals(lore.get(lore.size() - 2)))return;

        event.setCancelled(true);

        AltarCrafting altarCrafting = new AltarCrafting(event.getClickedBlock());
    }

    @EventHandler
    public void cauldronUse(PlayerInteractEvent event){
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(event.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        if(!event.getMaterial().equals(Material.BLAZE_POWDER)) return;
        if(!event.getClickedBlock().getType().equals(Material.CAULDRON)) return;
        if(((Cauldron)(event.getClickedBlock().getState().getData())).isEmpty())return;
        Material material = event.getClickedBlock().getLocation().add(0,-1, 0).getBlock().getState().getType();
        if((!material.equals(Material.STATIONARY_LAVA)) && (!material.equals(Material.LAVA)) && (!material.equals(Material.FIRE)))return;

        event.setCancelled(true);

        Location location = event.getClickedBlock().getLocation().add(0.125,0.125,0.125);
        CauldronCrafting cauldronCrafting = new CauldronCrafting(event.getClickedBlock().getWorld().getNearbyEntities(location, 0.75,0.875,0.75), location.add(0.375,1.375,0.375), event.getPlayer().getInventory().getItemInMainHand());
        //0.125 cauldron wall size
    }

}
