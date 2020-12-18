package ru.vixtor141.MagickScrolls.research;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;


import java.util.Arrays;
import java.util.List;

public class StandardResearchBookCreator {

    public StandardResearchBookCreator(Inventory inventory){
        inventory.setItem(23, basic(ACCrafts.ItemsCauldronCrafts.RITUAL_BOOK.craftCauldronGetItem().clone()));
        inventory.setItem(24, standard(new ItemStack(Material.EXP_BOTTLE), "Ancient Bottle", Arrays.asList("Nobody knows where this bottle came from","But her ability to store and give experience can be useful")));
    }

    private ItemStack basic(ItemStack itemStack){
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("Basic");
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack standard(ItemStack itemStack, String name, List<String> lore){
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
