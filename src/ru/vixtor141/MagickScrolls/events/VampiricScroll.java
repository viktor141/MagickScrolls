package ru.vixtor141.MagickScrolls.events;

import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.Misc.CheckUp;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;
import ru.vixtor141.MagickScrolls.scrolls.Vampiric;

public class VampiricScroll implements Listener {

    @EventHandler
    public void entityDamage(EntityDamageByEntityEvent event) {
        if(!event.getDamager().getType().equals(EntityType.PLAYER)) return;
        if(event.getEntity() instanceof ArmorStand)return;
        if(event.getEntity() instanceof Fireball)return;
        if(event.getEntity() instanceof ItemFrame)return;
        Player player = (Player) event.getDamager();
        if(player.getInventory().getItemInMainHand().getType() != Material.PAPER) return;
        ItemStack item = player.getInventory().getItemInMainHand();
        if(!item.getItemMeta().hasLore()) return;
        if(!CheckUp.checkItemLore(ACCrafts.CraftsOfScrolls.VAMPIRIC, item)) return;

        new Vampiric(player, item, event);
    }

}
