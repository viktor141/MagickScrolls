package ru.vixtor141.MagickScrolls.ritual;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.vixtor141.MagickScrolls.lang.LangVar;

public class RitualBook {

    private final Inventory inventory;
    private final int inventorySize = 18;

    public RitualBook(Player player){
        ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)8);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(" ");
        itemStack.setItemMeta(itemMeta);
        inventory = Bukkit.createInventory(player, inventorySize, ChatColor.DARK_GREEN + "" + ChatColor.BOLD  + LangVar.in_rt.getVar());
        for(int i = 0; i < inventorySize; i++){
            inventory.setItem(i, itemStack);
        }
    }

    public Inventory getInventory() {
        return inventory;
    }
}
