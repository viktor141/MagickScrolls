package ru.vixtor141.MagickScrolls.levels;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.vixtor141.MagickScrolls.lang.LangVar;

import java.util.ArrayList;
import java.util.List;

public enum ManaShieldLevel {
    ZERO,
    FIRST,
    SECOND,
    THIRD,
    FOURTH,
    FIFTH;
    private final static ItemStack[] itemStacks = new ItemStack[values().length];
    private final static int[] counts = new int[values().length];
    private final static int[] xpLevels = new int[values().length];
    private final static double[][] damageAndMana = new double[values().length][2];
    public enum TypeDamageOrMana{
        MANA,
        DAMAGE
    }

    public  void setCountNumber(int number){
        counts[this.ordinal()] = number;
    }
    public  int getCountNumber(){
        return  counts[this.ordinal()];
    }

    public void setItemForLevel(String name){
        ItemStack itemStack = new ItemStack(Material.SHIELD);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.YELLOW + "✹ " + ChatColor.GREEN +"" + ChatColor.BOLD + name + ChatColor.YELLOW + " ✹");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GREEN + LangVar.ims_otl.getVar());
        lore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "◊ " + ChatColor.AQUA + LangVar.ims_dm.getVar() + ": " + String.format("%.1f",this.getDamageAndMana(TypeDamageOrMana.DAMAGE) * 100) + "%" + ChatColor.GOLD + "" + ChatColor.BOLD + " ◊");
        lore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "◊ " + ChatColor.AQUA + LangVar.ims_mr.getVar() + ": " + String.format("%.1f", this.getDamageAndMana(TypeDamageOrMana.MANA) * 100) + "%" + ChatColor.GOLD +  "" + ChatColor.BOLD + " ◊");
        lore.add("");
        if(this.ordinal() != values().length - 1) {
            lore.add(ChatColor.DARK_RED + "" + ChatColor.BOLD + "◉ " + ChatColor.YELLOW + LangVar.ims_fnlyn.getVar() + ": " + values()[this.ordinal()+1].getCountNumber() + ChatColor.DARK_RED + "" + ChatColor.BOLD + " ◉");
        }else {
            lore.add(ChatColor.DARK_RED + "" + ChatColor.BOLD + "◉ " + ChatColor.YELLOW + LangVar.ims_yhcmts.getVar() + ChatColor.DARK_RED + "" + ChatColor.BOLD + " ◉");
        }
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);

        itemStacks[this.ordinal()] = itemStack;
    }

    public ItemStack getItemForLevel(int count, ManaShieldLevel manaShieldLevel){
        ItemStack itemStack = itemStacks[this.ordinal()].clone();
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = itemStack.getItemMeta().getLore();
        lore.add("");
        if(manaShieldLevel.ordinal() < this.ordinal()){
            lore.add(ChatColor.BLUE + "✚ " + ChatColor.RED + LangVar.ims_ctbtf.getVar() + ": " + xpLevels[this.ordinal()] + ChatColor.BLUE + " ✚");
        }else {
            lore.add(ChatColor.DARK_GREEN + "✔ " + LangVar.ims_b.getVar() + " ✔");
        }
        lore.add("");
        lore.add(ChatColor.WHITE +"✴ " + LangVar.ims_nyci.getVar() + " " + count + " ✴");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public void setDamageAndMana(double value, TypeDamageOrMana type){
        damageAndMana[this.ordinal()][type.ordinal()] = value;
    }

    public double getDamageAndMana(TypeDamageOrMana typeDamageOrMana){
        return damageAndMana[this.ordinal()][typeDamageOrMana.ordinal()];
    }

    public void setXpLevels(int num){
        xpLevels[this.ordinal()] = num;
    }

    public int getXpLevel(){
        return xpLevels[this.ordinal()];
    }

}
