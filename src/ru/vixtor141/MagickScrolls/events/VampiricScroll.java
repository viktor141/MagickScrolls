package ru.vixtor141.MagickScrolls.events;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.Mana;

public class VampiricScroll implements Listener {

    @EventHandler
    public void entityDamage(EntityDamageByEntityEvent event) {
        if (!event.getDamager().getType().equals(EntityType.PLAYER)) return;
        Player player = (Player) event.getDamager();
        if (player.getInventory().getItemInMainHand().getType() != Material.PAPER) return;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!item.getItemMeta().hasLore()) return;
        if (!item.getItemMeta().getLore().get(0).equals("Vampirism power")) return;

        Mana playerMana = Mana.getPlayerMap().get(player);
        playerMana.consumeMana(3);

        event.setDamage(3);
        if(player.getHealth() <= player.getHealthScale() - 1.5) {
            player.setHealth(player.getHealth() + 1.5);
        }else {
            player.setHealth(player.getHealthScale());
        }


        if(!player.getGameMode().equals(GameMode.CREATIVE)) {
                item.setAmount(item.getAmount() - 1);
                player.getInventory().setItemInMainHand(item);
        }

    }

}
