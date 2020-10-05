package ru.vixtor141.MagickScrolls.Misc;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;

import java.util.List;

public class CheckUp {

    public static boolean checkItemLore(ACCrafts.CraftsOfScrolls craftsOfScrolls, ItemStack item){
        List<String> tLore = craftsOfScrolls.craftAltarResult().getItemMeta().getLore();
        List<String> lore = item.getItemMeta().getLore();
        return tLore.get(tLore.size() - 2).equals(lore.get(lore.size() - 2));
    }

    public static boolean checkReqItems(List<ItemStack> reqItems, List<ItemStack> items){
        reqItems.removeIf(items::remove);
        return reqItems.isEmpty() && items.isEmpty();
    }

    public static void itemConsumer(Player player, ItemStack item){
        if(!player.getGameMode().equals(GameMode.CREATIVE)) {
            item.setAmount(item.getAmount() - 1);
        }
    }
}
