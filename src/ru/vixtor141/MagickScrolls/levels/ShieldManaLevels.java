package ru.vixtor141.MagickScrolls.levels;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.vixtor141.MagickScrolls.lang.LangVar;

public class ShieldManaLevels {

    private final Player player;
    private final Inventory inventory;
    private int count;
    private final int max;
    private ManaShieldLevel shieldLevel;

    public ShieldManaLevels(Player player){
        this.player = player;
        this.count = 0;
        inventory = Bukkit.createInventory(player, 9, ChatColor.DARK_BLUE + LangVar.in_msl.getVar());
        ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)8);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(" ");
        itemStack.setItemMeta(itemMeta);
        for(int i = 0; i< 9; i++){
            inventory.setItem(i, itemStack);
        }
        max = ManaShieldLevel.FIFTH.getCountNumber();
    }

    public void openInventory(){
        inventoryUpdate();
        player.openInventory(inventory);
    }

    public void update(int num){
        if(count < max) {
            count += num;
        }
    }

    private void inventoryUpdate(){
        inventory.setItem(0, ManaShieldLevel.ZERO.getItemForLevel(count, shieldLevel));
        for(int i = 1; i<ManaShieldLevel.values().length; i++){
            ManaShieldLevel manaShieldLevel = ManaShieldLevel.values()[i];
            if(count >= manaShieldLevel.getCountNumber()){
                inventory.setItem(i, manaShieldLevel.getItemForLevel(count, shieldLevel));
            }

        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setCount(int num){
        count = num;
    }

    public int getCount(){
        return count;
    }

    public ManaShieldLevel getManaShieldLevel() {
        return shieldLevel;
    }

    public void setManaShieldLevel(String manaShieldLevel) {
        this.shieldLevel = ManaShieldLevel.valueOf(manaShieldLevel);
    }

    public void updateManaShieldLevel(ManaShieldLevel manaShieldLevel) {
        this.shieldLevel = manaShieldLevel;
        inventory.setItem(manaShieldLevel.ordinal(), manaShieldLevel.getItemForLevel(count, shieldLevel));
    }
}
