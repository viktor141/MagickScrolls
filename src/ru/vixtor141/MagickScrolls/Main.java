package ru.vixtor141.MagickScrolls;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftShapedRecipe;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import ru.vixtor141.MagickScrolls.events.TeleportationScroll;

import java.util.ArrayList;
import java.util.List;

import static ru.vixtor141.MagickScrolls.Crafts.crafts;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new TeleportationScroll(), this);
        this.getLogger().info(ChatColor.YELLOW+"Plugin was enabled");
        crafts();
    }

    @Override
    public void onDisable() {
        this.getLogger().info(ChatColor.GOLD+"Plugin was disabled");
    }



}
