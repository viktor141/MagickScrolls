package ru.vixtor141.MagickScrolls.aspects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.vixtor141.MagickScrolls.lang.LangVar;

import java.util.ArrayList;
import java.util.List;

public class AspectGui {

    private final Inventory inventory;
    private final PlayerAspectStorage playerAspectsStorage;

    public AspectGui(Player player, PlayerAspectStorage playerAspectsStorage){
        this.playerAspectsStorage = playerAspectsStorage;
        inventory = Bukkit.createInventory(player, 54, ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + LangVar.in_aspec.getVar());
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void inventoryFill(){
        for(int i = 0; i < Aspect.values().length; i++){
            ItemStack itemStack = Aspect.values()[i].getItem();
            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + "" + Aspect.values()[i].name());
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(ChatColor.LIGHT_PURPLE + "❉" + ChatColor.GRAY + " " + LangVar.msg_a.getVar() + ": " + ChatColor.YELLOW + playerAspectsStorage.getAspects()[i] + ChatColor.LIGHT_PURPLE + " ❉");
            lore.add("");
            meta.setLore(lore);
            itemStack.setItemMeta(meta);
            inventory.setItem(i, itemStack);
        }

        ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)8);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(" ");
        itemStack.setItemMeta(itemMeta);
        for(int i = Aspect.values().length; i < 54; i++){
            inventory.setItem(i, itemStack);
        }
    }

    public void aspectGuiUpdate(Aspect aspect){
        ItemStack itemStack = inventory.getItem(aspect.ordinal());
        ItemMeta meta = itemStack.getItemMeta();
        List<String> lore = meta.getLore();
        lore.set(1, ChatColor.LIGHT_PURPLE + "❉" + ChatColor.GRAY + " " + LangVar.msg_a.getVar() + ": " + ChatColor.YELLOW + playerAspectsStorage.getAspects()[aspect.ordinal()] + ChatColor.LIGHT_PURPLE + " ❉");
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
    }
}
