package ru.vixtor141.MagickScrolls.crafts;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AltarCraftsStorage {

    private final List<List<ItemStack>> list;

    public AltarCraftsStorage(){
        list = new ArrayList<>();
    }

    public void filling(List<ItemStack> list){
        this.list.add(list);
    }

    public List<List<ItemStack>> getRecipes(){
        return list;
    }
}
