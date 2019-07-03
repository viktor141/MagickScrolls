package ru.vixtor141.MagickScrolls;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Crafts {

    public static void crafts(){
        scrollOfTeleportation();

    }

    private static void scrollOfTeleportation(){
        ItemStack scrollOfTeleportation = new ItemStack(Material.PAPER);
        ItemMeta meta = scrollOfTeleportation.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE+ "Scroll of teleportation");
        List<String> lore = new ArrayList<String>();
        lore.add("Scroll for teleportation in dimension");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.DAMAGE_ALL, 3,true);
        scrollOfTeleportation.setItemMeta(meta);

        scrollOfTeleportation.setAmount(4);
        ShapedRecipe s = new ShapedRecipe(scrollOfTeleportation);
        s.shape(new String[]{"E E"," P ","E E"});
        s.setIngredient('E', Material.ENDER_PEARL);
        s.setIngredient('P', Material.PAPER);
        Bukkit.getServer().addRecipe(s);
    }



}
