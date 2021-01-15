package ru.vixtor141.MagickScrolls.inventories;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class AccessoriesInventory {

    private final Player player;
    private final Inventory inventory;

    public AccessoriesInventory(Player player){
        this.player = player;
        inventory = Bukkit.createInventory(player, InventoryType.HOPPER, "Your Accessories");
    }

    public Inventory getInventory(){
        return inventory;
    }

    public void open(){
        player.openInventory(inventory);
    }
}
