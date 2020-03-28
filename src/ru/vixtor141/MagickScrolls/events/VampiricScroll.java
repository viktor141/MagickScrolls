package ru.vixtor141.MagickScrolls.events;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftFireball;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftItemFrame;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Crafts;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;

public class VampiricScroll implements Listener {

    @EventHandler
    public void entityDamage(EntityDamageByEntityEvent event) {
        if(!event.getDamager().getType().equals(EntityType.PLAYER)) return;
        if(event.getEntity() instanceof CraftArmorStand)return;
        if(event.getEntity() instanceof CraftFireball)return;
        if(event.getEntity() instanceof CraftItemFrame)return;
        Player player = (Player) event.getDamager();
        if(player.getInventory().getItemInMainHand().getType() != Material.PAPER) return;
        ItemStack item = player.getInventory().getItemInMainHand();
        if(!item.getItemMeta().hasLore()) return;
        if(!Crafts.ScrollsCrafts.VAMPIRIC.craftScroll(false).getItemMeta().getLore().equals(item.getItemMeta().getLore())) return;

        Mana playerMana = Mana.getPlayerMap().get(player);
        
        if(player.getHealth() == player.getHealthScale())return;
        Main plugin = Main.getPlugin();
        CDSystem.Scrolls scroll = CDSystem.Scrolls.VAMPIRIC;

        if(!playerMana.getCdSystem().CDStat(scroll, playerMana, plugin.getConfig().getDouble(scroll.name() + ".consumedMana") , plugin.getConfig().getInt(scroll.name() + ".CDseconds"), false))return;

        event.setDamage(3);

        if(player.getHealth() <= player.getHealthScale() - 1.5) {
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
