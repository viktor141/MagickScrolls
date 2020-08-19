package ru.vixtor141.MagickScrolls.events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.LazyMetadataValue;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.Misc.CheckUp;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;


import java.util.UUID;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.checkScrollEvent;

public class TrapScroll implements Listener {

    @EventHandler
    public void use(PlayerInteractEvent event){
        if(checkScrollEvent(event))return;
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if(!CheckUp.checkItemLore(ACCrafts.CraftsOfScrolls.TRAP, item)) return;


        Player player = event.getPlayer();
        Mana playerMana = Main.getPlugin().getPlayerMap().get(player);

        ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)8);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(" ");
        itemStack.setItemMeta(itemMeta);
        Inventory inventory = Bukkit.createInventory(player, 9, "Trap Item");
        for(int i = 0; i < 9; i++){
            if(i == 4)continue;
            inventory.setItem(i, itemStack);
        }

        playerMana.setTrapScroll(item);
        playerMana.setInventory(inventory);
        player.openInventory(inventory);
    }

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

        if(!player.getGameMode().equals(GameMode.CREATIVE)) {
            trapScroll.setAmount(trapScroll.getAmount() - 1);
            event.getPlayer().getInventory().setItemInMainHand(trapScroll);
        }
    }

}
