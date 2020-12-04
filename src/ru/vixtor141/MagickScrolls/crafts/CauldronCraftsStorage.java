package ru.vixtor141.MagickScrolls.crafts;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CauldronCraftsStorage {

    private final List<Map<List<ItemStack>, ACCrafts.ItemsCauldronCrafts>> list;
    private final int maxIngredients;

    public CauldronCraftsStorage(int maxIngredients){
        this.maxIngredients = maxIngredients;
        list = new ArrayList<>(maxIngredients);
        for(int i = 0; i < maxIngredients; i++){
            list.add(new HashMap<>());
        }
    }

    public void filling(List<ItemStack> items, ACCrafts.ItemsCauldronCrafts craft){
        list.get(items.size()-1).put(items, craft);
    }

    public int getMaxIngredients(){
        return maxIngredients;
    }

    public List<Map<List<ItemStack>, ACCrafts.ItemsCauldronCrafts>> getRecipes(){
        return list;
    }

}
