package ru.vixtor141.MagickScrolls.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.AltarCrafting;

public class CraftStartEvent implements Listener {

    @EventHandler
    public void use(PlayerInteractEvent event){
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(event.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        if(!event.getMaterial().equals(Material.STICK)) return;
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if(!event.getClickedBlock().getType().equals(Material.ENCHANTMENT_TABLE)) return;
        if(!item.getItemMeta().hasLore() || !(item.getItemMeta().getLore().size() >= 2))return;
        if(!item.getItemMeta().getLore().get(1).equals(ChatColor.BLUE + "magick stuff")) return;

        event.setCancelled(true);

        Player player = event.getPlayer();
        player.sendMessage("Typa pohuy");
        AltarCrafting altarCrafting = new AltarCrafting(event.getClickedBlock());
    }

}
