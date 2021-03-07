package ru.vixtor141.MagickScrolls.Misc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;
import ru.vixtor141.MagickScrolls.lang.LangVar;
import ru.vixtor141.MagickScrolls.research.PlayerResearch;
import ru.vixtor141.MagickScrolls.research.Research;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public static Mana getPlayerMana(Player player){
        if(!player.hasMetadata("MagickScrollsMana")){
            player.sendMessage(ChatColor.RED + LangVar.msg_w.getVar() + " " + player.getDisplayName() + " " + LangVar.msg_lpm.getVar());
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + LangVar.msg_w.getVar() + " " + player.getDisplayName() + " " + LangVar.msg_lpm.getVar());
            throw new NullPointerException();
        }
        return (Mana) player.getMetadata("MagickScrollsMana").get(0).value();
    }

    public static void updateItemInInventory(PlayerResearch playerResearch, Research research, List<String> infoLore){
        Inventory inventory = playerResearch.getResearchBookInventory();
        ItemStack itemStack = research.getResearchItem();
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>();
        if(itemMeta.hasLore())lore.addAll(itemMeta.getLore());
        lore.addAll(infoLore);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);

        inventory.setItem(research.getPosition(), itemStack);
    }

    public static void updateKills(List<String> lore, Map<EntityType, Integer> maxTypeIntegerMap, int[] currentKills ){
        int i = 0;
        for(EntityType entityType : maxTypeIntegerMap.keySet()) {
            lore.add(ChatColor.AQUA + LangVar.q_ntk.getVar() + ": " + entityType.getEntityClass().getSimpleName() + " " + currentKills[i] + "/" + maxTypeIntegerMap.values().toArray()[i]);
            i++;
        }
    }
}
