package ru.vixtor141.MagickScrolls.events;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Cauldron;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;
import ru.vixtor141.MagickScrolls.crafts.AltarCrafting;
import ru.vixtor141.MagickScrolls.crafts.AspectCraftingCauldron;
import ru.vixtor141.MagickScrolls.crafts.CauldronCrafting;

import java.util.Collection;
import java.util.List;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.getPlayerMana;

public class CraftStartEvent implements Listener {

    @EventHandler
    public void altarUse(PlayerInteractEvent event){
        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if(event.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        if(!event.getClickedBlock().getType().equals(Material.ENCHANTMENT_TABLE))return;
        if(!event.getMaterial().equals(Material.STICK)) return;
        ItemStack item = event.getItem();
        if(!item.getItemMeta().hasLore() || !(item.getItemMeta().getLore().size() >= 2))return;
        List<String> lore = item.getItemMeta().getLore();
        if(!lore.get(lore.size() - 2).substring(Main.getPlugin().getSubStr()).equals(ACCrafts.ItemsCauldronCrafts.MAGIC_STAFF.name()))return;

        event.setCancelled(true);

        new AltarCrafting(event.getClickedBlock(), getPlayerMana(event.getPlayer()).getPlayerResearch());
    }

    @EventHandler
    public void cauldronUse(PlayerInteractEvent event){
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(event.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        if(!event.getClickedBlock().getType().equals(Material.CAULDRON)) return;
        if(((Cauldron)(event.getClickedBlock().getState().getData())).isEmpty())return;
        Material material = event.getClickedBlock().getLocation().add(0,-1, 0).getBlock().getState().getType();
        if((!material.equals(Material.STATIONARY_LAVA)) && (!material.equals(Material.LAVA)) && (!material.equals(Material.FIRE)))return;
        if(event.getClickedBlock().getData() == 0)return;

        event.setCancelled(true);

        Location location = event.getClickedBlock().getLocation().add(0.5, 0, 0.5);
        Collection<Entity> collection = event.getClickedBlock().getWorld().getNearbyEntities(location.clone().add(0,0.5625,0), 0.75,0.875,0.75);
        List<String> lore = event.getItem().getItemMeta().getLore();
        if(event.getMaterial().equals(Material.BLAZE_POWDER)) {
            new CauldronCrafting(collection, location.add(0, 1.5, 0), event.getItem(), event.getPlayer(), event.getClickedBlock());
        }else if(event.getMaterial().equals(Material.STICK) && lore.get(lore.size() - 2).substring(Main.getPlugin().getSubStr()).equals(ACCrafts.ItemsCauldronCrafts.MAGIC_STAFF.name())){
            new AspectCraftingCauldron(collection, location.add(0, 1.5, 0), event.getPlayer(), event.getClickedBlock());
        }
        //0.125 cauldron wall size
    }

}
