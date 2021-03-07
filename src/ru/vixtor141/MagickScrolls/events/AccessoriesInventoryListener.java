package ru.vixtor141.MagickScrolls.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.Misc.FlyingItemsForPlayer;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;


import java.util.HashMap;
import java.util.List;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.getPlayerMana;

public class AccessoriesInventoryListener implements Listener{

    @EventHandler
    public void onClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        Mana playerMana = getPlayerMana(player);
        if(!event.getInventory().equals(playerMana.getPlayerResearch().getAccessoriesInventory().getInventory()))return;
        if(!event.getAction().equals(InventoryAction.PICKUP_ALL)){
            event.setCancelled(true);
            return;
        }
        event.setCancelled(true);
        ItemStack itemStack = event.getCurrentItem();
        if(!itemStack.getItemMeta().hasLore())return;
        List<String> lore = itemStack.getItemMeta().getLore();
        String name = lore.get(lore.size() - 2).substring(Main.getPlugin().getSubStr());
        if(!ACCrafts.AccessoryArtefact.isArtefact(name))return;

        ItemStack newForInventory = itemStack.clone();

        newForInventory.setAmount(1);
        if(event.getClickedInventory() == player.getInventory()){
            HashMap<Integer, ItemStack> integerItemStackHashMap = playerMana.getPlayerResearch().getAccessoriesInventory().getInventory().addItem(newForInventory);
            if(integerItemStackHashMap.isEmpty()) {
                itemStack.setAmount(itemStack.getAmount() - 1);
                addWearingAcc(playerMana, name);
            }
        }else{
            HashMap<Integer, ItemStack> integerItemStackHashMap = player.getInventory().addItem(newForInventory);
            if(integerItemStackHashMap.isEmpty()) {
                itemStack.setAmount(itemStack.getAmount() - 1);
                removeWearingAcc(playerMana, name);
            }
        }


    }

    private void removeWearingAcc(Mana playerMana, String string){
        playerMana.getWearingArtefact().set(ACCrafts.AccessoryArtefact.valueOf(string).ordinal(), false);
        if(ACCrafts.AccessoryArtefact.valueOf(string).equals(ACCrafts.AccessoryArtefact.MAGNET)) {
            for (FlyingItemsForPlayer flyingItemsForPlayer : playerMana.getFlyingItemsForPlayer()) {
                flyingItemsForPlayer.cancelAll();
            }
            playerMana.getFlyingItemsForPlayer().clear();
        }
    }

    private void addWearingAcc(Mana playerMana, String string){
        playerMana.getWearingArtefact().set(ACCrafts.AccessoryArtefact.valueOf(string).ordinal(), true);
    }
}
