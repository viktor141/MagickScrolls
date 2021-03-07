package ru.vixtor141.MagickScrolls.aspects;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class AspectsInItems {

    private final HashMap<ItemStack, Map<Aspect, Integer>> aspectItems;

    public AspectsInItems(){
        aspectItems = new HashMap<>();
    }


    public void addNew(ItemStack itemStack, Map<Aspect, Integer> map){
        aspectItems.put(itemStack, map);
    }

    public HashMap<ItemStack, Map<Aspect, Integer>> get(){
        return aspectItems;
    }
}
