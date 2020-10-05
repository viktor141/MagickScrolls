package ru.vixtor141.MagickScrolls.scrolls;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.interfaces.Scroll;
import ru.vixtor141.MagickScrolls.lang.LangVar;


public class Trap implements Scroll {

    public Trap(Player player, ItemStack item){
        Mana playerMana = Main.getPlugin().getPlayerMap().get(player);

        ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)8);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(" ");
        itemStack.setItemMeta(itemMeta);
        Inventory inventory = Bukkit.createInventory(player, 9, LangVar.s_ti.getVar());
        for(int i = 0; i < 9; i++){
            if(i == 4)continue;
            inventory.setItem(i, itemStack);
        }

        playerMana.setTrapScroll(item);
        playerMana.setInventory(inventory);
        player.openInventory(inventory);
    }

}
