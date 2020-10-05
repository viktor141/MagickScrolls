package ru.vixtor141.MagickScrolls.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.LazyMetadataValue;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;


import java.util.UUID;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.itemConsumer;


public class TrapScroll implements Listener {

    @EventHandler
    public void trap(EntityPickupItemEvent event){
        if(!event.getItem().hasMetadata("magickscrolls_trapitem"))return;
        UUID metadataValue = (UUID) event.getItem().getMetadata("magickscrolls_trapitem").get(0).value();
        if((event.getEntity() instanceof Player) && (metadataValue.equals(event.getEntity().getUniqueId())))return;
        Location location = event.getEntity().getLocation();
        event.setCancelled(true);
        event.getItem().setPickupDelay(10);

        location.getWorld().spawnEntity(location, EntityType.EVOKER_FANGS);
    }

    @EventHandler
    public void hopperEvent(InventoryPickupItemEvent event){
        if(!event.getItem().hasMetadata("magickscrolls_trapitem"))return;
        event.setCancelled(true);
    }

    @EventHandler
    public void click(InventoryClickEvent event){
        if(!(event.getWhoClicked() instanceof Player))return;
        Mana playerMana = Main.getPlugin().getPlayerMap().get((Player)event.getWhoClicked());
        if(event.getClickedInventory() == null)return;
        if(!event.getClickedInventory().equals(playerMana.getInventory()))return;
        if(event.getSlot() != 4)event.setCancelled(true);
    }

    @EventHandler
    public void preventDropScroll(PlayerDropItemEvent event){
        if(event.getPlayer().getOpenInventory().getTopInventory() == null)return;
        Mana playerMana = Main.getPlugin().getPlayerMap().get(event.getPlayer());
        if(!event.getPlayer().getOpenInventory().getTopInventory().equals(playerMana.getInventory()))return;
        event.setCancelled(true);
    }

    @EventHandler
    public void preventMergeTrap(ItemMergeEvent event){
        if(!event.getEntity().hasMetadata("magickscrolls_trapitem"))return;
        event.setCancelled(true);
    }

    @EventHandler
    public void useScroll(InventoryCloseEvent event){
        Player player = (Player) event.getPlayer();
        Mana playerMana = Main.getPlugin().getPlayerMap().get(player);
        if(!event.getInventory().equals(playerMana.getInventory()))return;
        Location location = player.getLocation();
        ItemStack item = event.getInventory().getItem(4);
        if(item == null || item.getType().equals(Material.AIR))return;


        Item trapItem = location.getWorld().dropItem(location, item);
        trapItem.setPickupDelay(20);

        if(!playerMana.getCdSystem().CDStat(CDSystem.Scrolls.TRAP, ".consumedMana", ".CDseconds", true))return;

        ItemStack trapScroll = playerMana.getTrapScroll();

        trapItem.setPickupDelay(60);
        trapItem.setMetadata("magickscrolls_trapitem", new LazyMetadataValue(Main.getPlugin(), player::getUniqueId));

        itemConsumer(player, trapScroll);
    }

}
