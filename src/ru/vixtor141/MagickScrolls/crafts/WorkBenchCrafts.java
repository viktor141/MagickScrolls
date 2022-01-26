package ru.vixtor141.MagickScrolls.crafts;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.lang.LangVar;

import java.util.ArrayList;
import java.util.List;

public class WorkBenchCrafts {

    private final Main plugin = Main.getPlugin();
    private final List<ShapedRecipe> shapedRecipes;

    public WorkBenchCrafts(){
        shapedRecipes = new ArrayList<>();
        shapedRecipes.add(pedestalRecipe());
    }

    private ShapedRecipe pedestalRecipe(){
        ItemStack pedestal = ACCrafts.ItemsCauldronCrafts.PEDESTAL.craftCauldronGetItem();
        NamespacedKey key = new NamespacedKey(plugin, "Bone_pedestal");

        ShapedRecipe pedestalCraft = new ShapedRecipe(key, pedestal);

        pedestalCraft.shape(" A "," B "," A ");

        pedestalCraft.setIngredient('A', Material.BONE_BLOCK);
        pedestalCraft.setIngredient('B', Material.FENCE);
        return pedestalCraft;
    }

    public List<ShapedRecipe> getCustomRecipes(){
        return shapedRecipes;
    }
}
