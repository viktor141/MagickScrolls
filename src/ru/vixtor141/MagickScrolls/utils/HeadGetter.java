package ru.vixtor141.MagickScrolls.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class HeadGetter {

    private ItemStack itemStack;

    public HeadGetter(String value){
        itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);

        itemStack = Bukkit.getUnsafe().modifyItemStack(itemStack, "{display:{Name:\"" + ChatColor.WHITE + "headName" + "\"},SkullOwner:{Id:" + UUID.randomUUID().toString() + ",Properties:{textures:[{Value:" + value + "}]}}}");
    }

    public ItemStack getHead(){
        return itemStack;
    }
}
