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
        scrollOfLightning();
        scrollOfLightningPowerTwo();
        scrollOfLightningPowerThree();
        scrollOfVampirism();

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

    private static void scrollOfLightning(){
        ItemStack scrollOfLightning = new ItemStack(Material.PAPER);
        ItemMeta meta = scrollOfLightning.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE+ "Scroll of lightning I");
        List<String> lore = new ArrayList<String>();
        lore.add("Scroll for lightning strike");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.DAMAGE_ALL, 3,true);
        scrollOfLightning.setItemMeta(meta);

        scrollOfLightning.setAmount(8);
        ShapedRecipe s = new ShapedRecipe(scrollOfLightning);
        s.shape(new String[]{"PPP","PNP","PPP"});
        s.setIngredient('N', Material.NETHER_STAR);
        s.setIngredient('P', Material.PAPER);
        Bukkit.getServer().addRecipe(s);
    }

    private static void scrollOfLightningPowerTwo(){
        ItemStack scrollOfLightningPowerTwo = new ItemStack(Material.PAPER);
        ItemMeta meta = scrollOfLightningPowerTwo.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE+ "Scroll of lightning II");
        List<String> lore = new ArrayList<String>();
        lore.add("Scroll for lightning strike power two");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.DAMAGE_ALL, 3,true);
        scrollOfLightningPowerTwo.setItemMeta(meta);

        scrollOfLightningPowerTwo.setAmount(8);
        ShapedRecipe s = new ShapedRecipe(scrollOfLightningPowerTwo);
        s.shape(new String[]{"PNP","PNP","PNP"});
        s.setIngredient('N', Material.NETHER_STAR);
        s.setIngredient('P', Material.PAPER);
        Bukkit.getServer().addRecipe(s);
    }

    private static void scrollOfLightningPowerThree(){
        ItemStack scrollOfLightningPowerThree = new ItemStack(Material.PAPER);
        ItemMeta meta = scrollOfLightningPowerThree.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE+ "Scroll of lightning III");
        List<String> lore = new ArrayList<String>();
        lore.add("Scroll for lightning strike power three");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.DAMAGE_ALL, 3,true);
        scrollOfLightningPowerThree.setItemMeta(meta);

        scrollOfLightningPowerThree.setAmount(8);
        ShapedRecipe s = new ShapedRecipe(scrollOfLightningPowerThree);
        s.shape(new String[]{"PNP","NNN","PNP"});
        s.setIngredient('N', Material.NETHER_STAR);
        s.setIngredient('P', Material.PAPER);
        Bukkit.getServer().addRecipe(s);
    }

    private static void scrollOfVampirism(){
        ItemStack scrollOfVampirism = new ItemStack(Material.PAPER);
        ItemMeta meta = scrollOfVampirism.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE+ "Scroll of teleportation");
        List<String> lore = new ArrayList<String>();
        lore.add("Vampirism power");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.DURABILITY, 1,true);
        scrollOfVampirism.setItemMeta(meta);

        scrollOfVampirism.setAmount(4);
        ShapedRecipe s = new ShapedRecipe(scrollOfVampirism);
        s.shape(new String[]{"E E"," P ","E E"});
        s.setIngredient('E', Material.GHAST_TEAR);
        s.setIngredient('P', Material.PAPER);
        Bukkit.getServer().addRecipe(s);
    }

}
