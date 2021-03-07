package ru.vixtor141.MagickScrolls.inventories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import ru.vixtor141.MagickScrolls.lang.LangVar;

public class AccessoriesInventory {

    private final Player player;
    private final Inventory inventory;

    public AccessoriesInventory(Player player){
        this.player = player;
        inventory = Bukkit.createInventory(player, InventoryType.HOPPER, ChatColor.DARK_GRAY + "" + ChatColor.BOLD +  LangVar.in_ya.getVar());
    }

    public Inventory getInventory(){
        return inventory;
    }

    public void open(){
        player.openInventory(inventory);
    }
}
