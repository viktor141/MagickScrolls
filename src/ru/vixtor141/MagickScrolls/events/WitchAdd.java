package ru.vixtor141.MagickScrolls.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;
import ru.vixtor141.MagickScrolls.lang.LangVar;

import java.util.List;

public class WitchAdd implements Listener {

    @EventHandler
    public void use(PlayerInteractEntityEvent event){
        if(event.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        if(!event.getRightClicked().getType().equals(EntityType.WITCH))return;
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if(!item.getType().equals(ACCrafts.ItemsCauldronCrafts.WITCH_ARTIFACT.craftCauldronGetMaterial())) return;
        if(!item.getItemMeta().hasLore() || !(item.getItemMeta().getLore().size() >= 2)) return;
        List<String> lore = item.getItemMeta().getLore();
        if(!ACCrafts.ItemsCauldronCrafts.WITCH_ARTIFACT.name().equals(lore.get(lore.size() - 2).substring(Main.getPlugin().getSubStr())))return;
        if(event.getRightClicked().hasMetadata("magickscrolls_ritualWitch"))return;

        int nb = Integer.parseInt(lore.get(lore.size() - 3));

        if(nb >= 6){
            player.sendMessage(ChatColor.RED + LangVar.msg_yhamw.getVar());
            return;
        }
        event.getRightClicked().remove();
        nb++;
        lore.set(lore.size() - 3, Integer.toString(nb));
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        item.setItemMeta(meta);
        player.sendMessage(ChatColor.GREEN + LangVar.msg_wha.getVar());

    }

}
