package ru.vixtor141.MagickScrolls.Misc;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;

import java.util.List;

public class CheckUp {

    public static boolean checkItemLore(ACCrafts.CraftsOfScrolls craftsOfScrolls, ItemStack item){
        List<String> lore = item.getItemMeta().getLore();
        return craftsOfScrolls.name().equals(lore.get(lore.size() - 2).substring(Main.getPlugin().getSubStr()));
    }

    public static void itemConsumer(Player player, ItemStack item){
        if(!player.getGameMode().equals(GameMode.CREATIVE)) {
            item.setAmount(item.getAmount() - 1);
        }
    }
}
