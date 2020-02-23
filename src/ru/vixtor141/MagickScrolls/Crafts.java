package ru.vixtor141.MagickScrolls;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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
        scrollOfVortex();
    }

    private static void scrollOfTeleportation(){
        ItemStack scrollOfTeleportation = new ItemStack(Material.PAPER);
        NamespacedKey key = new NamespacedKey(Main.getPlugin(), "scrollOfTeleportation");
        ItemMeta meta = scrollOfTeleportation.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE+ "Scroll of teleportation");
        List<String> lore = new ArrayList<String>();
        lore.add("Scroll for teleportation in dimension");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.DURABILITY, 1,true);
        scrollOfTeleportation.setItemMeta(meta);

        scrollOfTeleportation.setAmount(4);
        ShapedRecipe s = new ShapedRecipe(key, scrollOfTeleportation);
        s.shape("EPE","P P","EPE");
        s.setIngredient('E', Material.ENDER_PEARL);
        s.setIngredient('P', Material.PAPER);
        Bukkit.getServer().addRecipe(s);
    }

    private static void scrollOfLightning(){
        ItemStack scrollOfLightning = new ItemStack(Material.PAPER);
        NamespacedKey key = new NamespacedKey(Main.getPlugin(), "scrollOfLightning");
        ItemMeta meta = scrollOfLightning.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE+ "Scroll of lightning I");
        List<String> lore = new ArrayList<String>();
        lore.add("Scroll for lightning strike");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.DURABILITY, 1,true);
        scrollOfLightning.setItemMeta(meta);

        scrollOfLightning.setAmount(8);
        ShapedRecipe s = new ShapedRecipe(key, scrollOfLightning);
        s.shape("PPP","PNP","PPP");
        s.setIngredient('N', Material.NETHER_STAR);
        s.setIngredient('P', Material.PAPER);
        Bukkit.getServer().addRecipe(s);
    }

    private static void scrollOfLightningPowerTwo(){
        ItemStack scrollOfLightningPowerTwo = new ItemStack(Material.PAPER);
        NamespacedKey key = new NamespacedKey(Main.getPlugin(), "scrollOfLightningPowerTwo");
        ItemMeta meta = scrollOfLightningPowerTwo.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE+ "Scroll of lightning II");
        List<String> lore = new ArrayList<String>();
        lore.add("Scroll for lightning strike power two");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.DURABILITY, 1,true);
        scrollOfLightningPowerTwo.setItemMeta(meta);

        scrollOfLightningPowerTwo.setAmount(8);
        ShapedRecipe s = new ShapedRecipe(key, scrollOfLightningPowerTwo);
        s.shape("PNP","PNP","PNP");
        s.setIngredient('N', Material.NETHER_STAR);
        s.setIngredient('P', Material.PAPER);
        Bukkit.getServer().addRecipe(s);
    }

    private static void scrollOfLightningPowerThree(){
        ItemStack scrollOfLightningPowerThree = new ItemStack(Material.PAPER);
        NamespacedKey key = new NamespacedKey(Main.getPlugin(), "scrollOfLightningPowerThree");
        ItemMeta meta = scrollOfLightningPowerThree.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE+ "Scroll of lightning III");
        List<String> lore = new ArrayList<String>();
        lore.add("Scroll for lightning strike power three");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.DURABILITY, 1,true);
        scrollOfLightningPowerThree.setItemMeta(meta);

        scrollOfLightningPowerThree.setAmount(8);
        ShapedRecipe s = new ShapedRecipe(key, scrollOfLightningPowerThree);
        s.shape("PNP","NNN","PNP");
        s.setIngredient('N', Material.NETHER_STAR);
        s.setIngredient('P', Material.PAPER);
        Bukkit.getServer().addRecipe(s);
    }

    private static void scrollOfVampirism(){
        ItemStack scrollOfVampirism = new ItemStack(Material.PAPER);
        NamespacedKey key = new NamespacedKey(Main.getPlugin(), "scrollOfVampirism");
        ItemMeta meta = scrollOfVampirism.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE+ "Scroll of Vampirism");
        List<String> lore = new ArrayList<String>();
        lore.add("Vampirism power");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.DURABILITY, 1,true);
        scrollOfVampirism.setItemMeta(meta);

        scrollOfVampirism.setAmount(4);
        ShapedRecipe s = new ShapedRecipe(key, scrollOfVampirism);
        s.shape("T T"," P ","T T");
        s.setIngredient('T', Material.GHAST_TEAR);
        s.setIngredient('P', Material.PAPER);
        Bukkit.getServer().addRecipe(s);
    }

    private static void scrollOfVortex(){
        ItemStack scrollOfVortex = new ItemStack(Material.PAPER);
        NamespacedKey key = new NamespacedKey(Main.getPlugin(), "scrollOfVortex");
        ItemMeta meta = scrollOfVortex.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE+ "Scroll of vortex");
        List<String> lore = new ArrayList<String>();
        lore.add("Vortex scroll");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.DURABILITY, 1,true);
        scrollOfVortex.setItemMeta(meta);

        scrollOfVortex.setAmount(4);
        ShapedRecipe s = new ShapedRecipe(key, scrollOfVortex);
        s.shape("PBP","GSG","PFP");
        s.setIngredient('S', Material.SHULKER_SHELL);
        s.setIngredient('B', Material.BLAZE_POWDER);
        s.setIngredient('G', Material.GLOWSTONE_DUST);
        s.setIngredient('F', Material.FEATHER);
        s.setIngredient('P', Material.PAPER);
        Bukkit.getServer().addRecipe(s);
    }

}
