package ru.vixtor141.MagickScrolls.Misc;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RitualsRecipesStorage {

    private final List<List<ItemStack>> list;

    public RitualsRecipesStorage(){
        list = new ArrayList<>();
    }

    public void filling(List<ItemStack> list){
        this.list.add(list);
    }

    public List<List<ItemStack>> getRecipes(){
        return list;
    }
}
