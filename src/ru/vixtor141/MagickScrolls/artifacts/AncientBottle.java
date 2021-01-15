package ru.vixtor141.MagickScrolls.artifacts;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.vixtor141.MagickScrolls.Mana;

import java.util.HashMap;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.getPlayerMana;

public class AncientBottle {

    public AncientBottle(Player player, ItemStack itemStack){
        if(itemStack.getAmount() != 1){
            ItemStack cloned = itemStack.clone();
            cloned.setAmount(cloned.getAmount() - 1);
            itemStack.setAmount(1);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.addEnchant(Enchantment.DURABILITY, 10, true);
            itemStack.setItemMeta(itemMeta);
            HashMap<Integer, ItemStack> map = player.getInventory().addItem(cloned);
            if(!map.isEmpty()){
                player.getWorld().dropItem(player.getLocation(), cloned);
            }
        }
        Mana playerMana = getPlayerMana(player);
        player.openInventory(playerMana.getAncientBottleInventory().useInventory(itemStack));
    }
}
