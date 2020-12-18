package ru.vixtor141.MagickScrolls.research;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.vixtor141.MagickScrolls.lang.LangVar;

public class ResearchBook {

    private final Inventory inventory;
    private final int inventorySize = 45;

    public ResearchBook(){
        ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)8);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(" ");
        inventory = Bukkit.createInventory(null, inventorySize, LangVar.s_ti.getVar());
        for(int i = 0; i < inventorySize; i++){
            inventory.setItem(i, itemStack);
        }

    }


}
