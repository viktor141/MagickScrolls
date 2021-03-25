package ru.vixtor141.MagickScrolls.mobs;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.metadata.LazyMetadataValue;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VillagerIMP {

    private final String name = ChatColor.GOLD + "✹ " + ChatColor.DARK_RED + ChatColor.BOLD + "I" + ChatColor.BLACK +ChatColor.MAGIC + "fff" + ChatColor.DARK_RED + ChatColor.BOLD + "M" + ChatColor.BLACK + ChatColor.MAGIC + "fff" + ChatColor.DARK_RED + ChatColor.BOLD + "P" + ChatColor.GOLD + " ✹";
    private final Player player;
    private final Material[] materials = {Material.DIAMOND, Material.EMERALD, Material.ANVIL, Material.BEACON, Material.BLAZE_ROD, Material.BONE, Material.BOOKSHELF,
            Material.CAKE, Material.CHORUS_FLOWER, Material.COAL_BLOCK, Material.DEAD_BUSH, Material.COOKIE, Material.ENDER_PEARL, Material.END_ROD, Material.GHAST_TEAR,
            Material.GLOWSTONE_DUST, Material.IRON_INGOT, Material.REDSTONE_BLOCK, Material.SADDLE, Material.OBSIDIAN, Material.PUMPKIN, Material.PRISMARINE, Material.SOUL_SAND,
            Material.SPIDER_EYE, Material.SHULKER_SHELL, Material.SLIME_BALL, Material.ROTTEN_FLESH};
    private final List<MerchantRecipe> merchantRecipes = new ArrayList<>();
    private final Merchant merchant;

    public VillagerIMP(Player player, Location location){
        this.player = player;
        Villager villager = (Villager) location.getWorld().spawnEntity(location.clone().add(0.5,1,0.5), EntityType.VILLAGER);
        villager.setCustomName(name);
        villager.setCustomNameVisible(true);

        MerchantRecipe recipe = new MerchantRecipe(ACCrafts.ItemsCauldronCrafts.RESEARCH_BOOK.craftCauldronGetItem().clone(), 1);
        recipe.addIngredient(new ItemStack(Material.NETHER_STAR, 1));
        recipe.addIngredient(new ItemStack(Material.BOOK, 1));
        merchantRecipes.add(recipe);

        for(int i = 0; i < 3; i++)recipeSet();

        villager.setRecipes(new ArrayList<>());
        merchant = Bukkit.createMerchant(name);
        merchant.setRecipes(merchantRecipes);


        villager.setMetadata("magickScrolls_IMP", new LazyMetadataValue(Main.getPlugin(), this::getMerchant));
        villager.setMetadata("magickScrolls_IMP_player", new LazyMetadataValue(Main.getPlugin(), this::getPlayer));
    }

    private Merchant getMerchant(){
        return merchant;
    }

    private Player getPlayer(){
        return player;
    }

    private void recipeSet(){
        if(new Random().nextInt(100) < Main.getPlugin().getConfig().getInt("netherStarTrade")){
            MerchantRecipe recipe = new MerchantRecipe(new ItemStack(Material.NETHER_STAR), 1);
            recipe.addIngredient(randomRecipeItem());
            recipe.addIngredient(randomRecipeItem());
            merchantRecipes.add(recipe);
        }else {
            MerchantRecipe recipe = new MerchantRecipe(new ItemStack(Material.FIRE, 23), 1);
            recipe.addIngredient(new ItemStack(Material.FIRE, 1));
            recipe.addIngredient(new ItemStack(Material.BEDROCK, 1));
            merchantRecipes.add(recipe);
        }
    }

    private ItemStack randomRecipeItem(){
        ItemStack itemStack = new ItemStack(materials[new Random().nextInt(materials.length)]);
        if(itemStack.getMaxStackSize() > 1){
            itemStack.setAmount(new Random().nextInt(itemStack.getMaxStackSize())+1);
        }
        return itemStack;
    }
}
