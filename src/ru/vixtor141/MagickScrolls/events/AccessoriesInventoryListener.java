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


import java.util.List;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.getPlayerMana;

public class AccessoriesInventoryListener implements Listener{

    @EventHandler
    public void move(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        Mana playerMana = getPlayerMana(player);
        if(!event.getInventory().equals(playerMana.getPlayerResearch().getAccessoriesInventory().getInventory()))return;
        if(!event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY) && !event.getAction().equals(InventoryAction.PICKUP_ALL) && !event.getAction().equals(InventoryAction.PLACE_ALL)){
            event.setCancelled(true);
            return;
        }

        if(event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
            ItemStack itemStack = event.getCurrentItem();
            if(!itemStack.getItemMeta().hasLore())return;
            List<String> lore = itemStack.getItemMeta().getLore();
            String name = lore.get(lore.size() - 2).substring(Main.getPlugin().getSubStr());
            if(ACCrafts.AccessoryArtefact.isArtefact(name)){
                if(event.getClickedInventory().equals(playerMana.getPlayerResearch().getAccessoriesInventory().getInventory())){
                    removeWearingAcc(playerMana, name);
                }
                if(!event.getClickedInventory().equals(playerMana.getPlayerResearch().getAccessoriesInventory().getInventory())){
                    addWearingAcc(playerMana, name);
                }
            }
            return;
        }

        if(!event.getClickedInventory().equals(playerMana.getPlayerResearch().getAccessoriesInventory().getInventory()))return;
        if(event.getAction().equals(InventoryAction.PICKUP_ALL)){
            ItemStack itemStack = event.getCurrentItem();
            if(!itemStack.getItemMeta().hasLore())return;
            List<String> lore = itemStack.getItemMeta().getLore();
            String name = lore.get(lore.size() - 2).substring(Main.getPlugin().getSubStr());
            if(ACCrafts.AccessoryArtefact.isArtefact(name)){
                removeWearingAcc(playerMana, name);
            }
        }

        if(event.getAction().equals(InventoryAction.PLACE_ALL)){
            ItemStack itemStack = event.getCursor();
            if(!itemStack.getItemMeta().hasLore())return;
            List<String> lore = itemStack.getItemMeta().getLore();
            String name = lore.get(lore.size() - 2).substring(Main.getPlugin().getSubStr());
            if(ACCrafts.AccessoryArtefact.isArtefact(name)){
                addWearingAcc(playerMana, name);
            }
        }

    }

    private void removeWearingAcc(Mana playerMana, String string){
        playerMana.getWearingArtefact().set(ACCrafts.AccessoryArtefact.valueOf(string).ordinal(), false);
        for(FlyingItemsForPlayer flyingItemsForPlayer : playerMana.getFlyingItemsForPlayer()){
            flyingItemsForPlayer.cancelAll();
        }
        playerMana.getFlyingItemsForPlayer().clear();
    }

    private void addWearingAcc(Mana playerMana, String string){
        playerMana.getWearingArtefact().set(ACCrafts.AccessoryArtefact.valueOf(string).ordinal(), true);
    }
}
