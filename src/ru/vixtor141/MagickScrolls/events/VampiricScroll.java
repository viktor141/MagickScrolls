package ru.vixtor141.MagickScrolls.events;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;

public class VampiricScroll implements Listener {

    private final Main plugin = Main.getPlugin();

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
        if(!ACCrafts.CraftsOfScrolls.VAMPIRIC.craftAltarResult().getItemMeta().getLore().get(1).equals(item.getItemMeta().getLore().get(1))) return;

        Mana playerMana = plugin.getPlayerMap().get(player);
        
        if(player.getHealth() == player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())return;
        Main plugin = Main.getPlugin();
        CDSystem.Scrolls scroll = CDSystem.Scrolls.VAMPIRIC;

        if(!playerMana.getCdSystem().CDStat(scroll, playerMana, plugin.getConfig().getDouble(scroll.name() + ".consumedMana") , plugin.getConfig().getInt(scroll.name() + ".CDseconds"), false))return;

        event.setDamage(3);

        if(player.getHealth() <= player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() - 1.5) {
            player.setHealth(player.getHealth() + 1.5);
        }else{
            player.setHealth(player.getHealthScale());
        }


        if(!player.getGameMode().equals(GameMode.CREATIVE)) {
                item.setAmount(item.getAmount() - 1);
                player.getInventory().setItemInMainHand(item);
        }

    }

}
