package ru.vixtor141.MagickScrolls.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.Base64;

public class HeadGetter {

    private ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
    private ItemStack[] heads = new ItemStack[Head.values().length];

    public enum Head{
        Brain("http://textures.minecraft.net/texture/2dd3b01ec32f3f5ea26ad4e23305401f73d13f3c16b3facefe70d92176b311"),
        Bestia("http://textures.minecraft.net/texture/f83a2aa9d3734b919ac24c9659e5e0f86ecafbf64d4788cfa433bbec189e8");

        ItemStack heads;

        Head(String url){
            heads = getSkull(url);
        }

        public ItemStack getHead() {
            return heads;
        }

        private ItemStack getSkull(String url) {
            ItemStack skull= new ItemStack(Material.SKULL_ITEM, 1, (short) 3);

            if (url == null || url.isEmpty())
                return skull;

            ItemMeta skullMeta = skull.getItemMeta();
            GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
            profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
            Field profileField = null;

            try {
                profileField = skullMeta.getClass().getDeclaredField("profile");
            } catch (NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }

            profileField.setAccessible(true);

            try {
                profileField.set(skullMeta, profile);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }

            skull.setItemMeta(skullMeta);
            return skull;
        }
    }

    public HeadGetter(){
        for (Head head: Head.values()){
            heads[head.ordinal()] = head.getHead();
        }
    }

    public ItemStack getHead(String value){
        ItemStack itemStack = head.clone();

        itemStack = Bukkit.getUnsafe().modifyItemStack(itemStack, "{display:{Name:\"" + ChatColor.WHITE + "headName" + "\"},SkullOwner:{Id:" + UUID.randomUUID().toString() + ",Properties:{textures:[{Value:" + value + "}]}}}");

        return itemStack;
    }


    public ItemStack getHeadS(Head head){
        return heads[head.ordinal()].clone();
    }

}
