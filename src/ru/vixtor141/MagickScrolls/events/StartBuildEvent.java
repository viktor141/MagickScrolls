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
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;
import ru.vixtor141.MagickScrolls.interfaces.AltarFace;
import ru.vixtor141.MagickScrolls.interfaces.Ritual;
import ru.vixtor141.MagickScrolls.lang.LangVar;

import java.util.List;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.getPlayerMana;

public class StartBuildEvent implements Listener {

    @EventHandler
    public void use(PlayerInteractEvent event){
        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))return;
        if(event.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        if(!event.getClickedBlock().getType().equals(Material.GOLD_BLOCK))return;
        ItemStack item = event.getItem();
        if(item == null)return;
        if(!item.getType().equals(ACCrafts.ItemsCauldronCrafts.RITUAL_ALTAR_BUILDER.craftCauldronGetMaterial()))return;
        if(!item.getItemMeta().hasLore() || !(item.getItemMeta().getLore().size() >= 2)) return;
        List<String> lore = item.getItemMeta().getLore();
        if(!ACCrafts.ItemsCauldronCrafts.RITUAL_ALTAR_BUILDER.name().equals(lore.get(lore.size() - 2).substring(Main.getPlugin().getSubStr())))return;

        event.setCancelled(true);

        Player player = event.getPlayer();
        Mana playerMana = getPlayerMana(player);
        Ritual ritual = playerMana.getRitual();
        if(ritual == null){
            player.sendMessage(ChatColor.RED + LangVar.msg_rins.getVar());
            return;
        }
        AltarFace altar = ritual.getAltar(event.getClickedBlock().getLocation());
        altar.builder(event.getPlayer());
    }

}
