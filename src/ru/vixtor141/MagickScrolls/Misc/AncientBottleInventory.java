package ru.vixtor141.MagickScrolls.Misc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.vixtor141.MagickScrolls.lang.LangVar;
import ru.vixtor141.MagickScrolls.research.Research;

public class AncientBottleInventory {

    private final String addS = ChatColor.GREEN + "❍", removeS = ChatColor.RED + "❍", color = ChatColor.YELLOW + "";
    private final Inventory inventory;
    private ItemStack artefact;
    private final ItemStack[]
            add = new ItemStack[]{
            new ItemStack(Material.CONCRETE, 64, (short) 13),
            new ItemStack(Material.CONCRETE, 10, (short) 13),
            new ItemStack(Material.CONCRETE, 1, (short) 13)},
            remove = new ItemStack[]{
            new ItemStack(Material.CONCRETE, 64, (short) 14),
            new ItemStack(Material.CONCRETE, 10, (short) 14),
            new ItemStack(Material.CONCRETE, 1, (short) 14)};

    public AncientBottleInventory(Player player){
        inventory = Bukkit.createInventory(player, 9, Research.ANCIENT_BOTTLE.getStandName());
        for(int i = 0; i < 3; i++){
            inventory.setItem(i, setAdd(i));
        }

        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)8);
        ItemMeta itemMeta = glass.getItemMeta();
        itemMeta.setDisplayName(" ");
        glass.setItemMeta(itemMeta);
        for(int i = 3; i < 6; i+=2){
            inventory.setItem(i, glass);
        }

        for(int i = 0; i < 3; i++){
            inventory.setItem(inventory.getSize() - 1 - i, setRemove(i));
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    private ItemStack setAdd(int i){
        ItemStack itemStack = add[i];
        ItemMeta itemMeta = itemStack.getItemMeta();
        if(itemStack.getAmount() < 64) {
            itemMeta.setDisplayName(addS + color + " " + LangVar.msg_mtb.getVar() + " " + itemStack.getAmount() + " " + (itemStack.getAmount() == 1? LangVar.msg_lvl.getVar() : LangVar.msg_lvls.getVar()) + " " + addS);
        }else {
            itemMeta.setDisplayName(addS + color + " " + LangVar.msg_mtba.getVar() + " " + LangVar.msg_lvls.getVar() + " " + addS);
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack setRemove(int i){
        ItemStack itemStack = remove[i];
        ItemMeta itemMeta = itemStack.getItemMeta();
        if(itemStack.getAmount() < 64) {
            itemMeta.setDisplayName(removeS + color + " " + LangVar.msg_tfb.getVar() + " " + itemStack.getAmount() + " " + (itemStack.getAmount() == 1? LangVar.msg_lvl.getVar() : LangVar.msg_lvls.getVar()) + " " + removeS);
        }else {
            itemMeta.setDisplayName(removeS + color + " " + LangVar.msg_tfba.getVar() + " " + LangVar.msg_lvls.getVar() + " " + removeS);
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public Inventory useInventory(ItemStack itemStack){
        this.artefact = itemStack;
        inventory.setItem(4, itemStack);
        return inventory;
    }

    public ItemStack getArtefact(){
        return artefact;
    }

    public void setArtifactToNull(){
        artefact = null;
    }
}
