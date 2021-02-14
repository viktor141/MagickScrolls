package ru.vixtor141.MagickScrolls.events;

import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;
import ru.vixtor141.MagickScrolls.lang.LangVar;
import ru.vixtor141.MagickScrolls.utils.AncientBottleLogic;

import java.util.List;
import java.util.Optional;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.getPlayerMana;

public class AncientBottleUse implements Listener {

    @EventHandler
    public void InteractAncientBottle(InventoryClickEvent event){
        if(!(event.getWhoClicked() instanceof Player))return;
        if(event.getClickedInventory() == null)return;
        Player player = (Player)event.getWhoClicked();
        if(!event.getClickedInventory().equals(getPlayerMana(player).getAncientBottleInventory().getInventory()))return;
        event.setCancelled(true);

        ItemStack artefact = getPlayerMana(player).getAncientBottleInventory().getArtefact();
        Inventory inventory = event.getClickedInventory();

        int levelNeed = event.getCurrentItem().getAmount();
        String string = levelNeed == 1? LangVar.msg_lvl.getVar() : LangVar.msg_lvls.getVar();
        ItemMeta itemMeta = artefact.getItemMeta();
        List<String> lore = itemMeta.getLore();
        AncientBottleLogic ancientBottleLogic = new AncientBottleLogic(player, Integer.parseInt(lore.get(lore.size() - 3).substring(2).split(" ")[0]));

        boolean flag = false;
        if(event.getSlot() < 3){
            switch (levelNeed){
                case 1:
                    flag = ancientBottleLogic.moveLevel();
                break;
                case 10:
                    flag = ancientBottleLogic.moveTenLevels();
                break;
                case 64:
                    flag = ancientBottleLogic.moveAll();
                break;
            }
            if(!flag){
                player.sendMessage(ChatColor.RED + LangVar.msg_ne.getVar() + " " + string);
            }
        }else if (event.getSlot() > 5){
            switch (levelNeed){
                case 1:
                    flag = ancientBottleLogic.takeLevel();
                    break;
                case 10:
                    flag = ancientBottleLogic.takeTenLevels();
                    break;
                case 64:
                    flag = ancientBottleLogic.takeAll();
                    break;
            }
            if(!flag){
                player.sendMessage(ChatColor.RED + LangVar.msg_ne.getVar() + " " + string);
            }
        }
        if(flag){
            lore.set(lore.size() - 3, ChatColor.GREEN + "" + ancientBottleLogic.getExpInBottle() + " " + LangVar.msg_lvl.getVar() + "(" + ancientBottleLogic.levelCalc() + ")");
            itemMeta.setLore(lore);
            artefact.setItemMeta(itemMeta);
            inventory.setItem(4, artefact);
        }

    }

    @EventHandler
    public void move(InventoryClickEvent event){
        if(event.getClickedInventory() == null)return;
        if(!(event.getWhoClicked() instanceof Player))return;
        Player player = (Player)event.getWhoClicked();
        if(!event.getInventory().equals(getPlayerMana(player).getAncientBottleInventory().getInventory()))return;
        event.setCancelled(true);
    }

    @EventHandler
    public void dispense(BlockDispenseEvent event){
        ItemStack itemStack = event.getItem();
        if(itemStack.getType().equals(ACCrafts.ItemsCauldronCrafts.ANCIENT_BOTTLE.craftCauldronGetMaterial()))event.setCancelled(true);
    }

    @EventHandler
    public void close(InventoryCloseEvent event){
        if(!(event.getPlayer() instanceof Player))return;
        Player player =(Player) event.getPlayer();
        if(!event.getInventory().equals(getPlayerMana(player).getAncientBottleInventory().getInventory()))return;
        if(player.isDead())return;

        ItemStack artefact = getPlayerMana(player).getAncientBottleInventory().getArtefact();
        ItemMeta itemMeta = artefact.getItemMeta();
        itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        artefact.setItemMeta(itemMeta);
        getPlayerMana(player).getAncientBottleInventory().setArtifactToNull();
    }

    @EventHandler
    public void dead(PlayerDeathEvent event){
        Player player = event.getEntity();
        if(getPlayerMana(player).getAncientBottleInventory().getArtefact() == null)return;
        List<ItemStack> list = event.getDrops();
        ItemStack artefact = getPlayerMana(player).getAncientBottleInventory().getArtefact();
        Optional<ItemStack> first = list.stream().filter(itemStack -> itemStack.getType().equals(artefact.getType()) && itemStack.getItemMeta().hasLore() && itemStack.getItemMeta().getLore().equals(artefact.getItemMeta().getLore()) && (itemStack.getEnchantments().get(Enchantment.DURABILITY) == 10 )).findFirst();

        if(first.isPresent()){
            ItemStack itemStack = first.get();
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
            itemStack.setItemMeta(itemMeta);
            getPlayerMana(player).getAncientBottleInventory().setArtifactToNull();
        }

    }

     /*if(event.getSlot() < 3){
        if(levelNeed < 64){
            if(player.getLevel() >= levelNeed){
                levelMove(player, levelNeed, artefact, inventory);
            }else {
                player.sendMessage(ChatColor.RED + LangVar.msg_ne.getVar() + " " + string);
            }
        }else {
            levelMove(player, player.getLevel(), artefact, inventory);
        }
    }else if (event.getSlot() > 5){
        ItemMeta itemMeta = artefact.getItemMeta();
        int lvlNow = Integer.parseInt(itemMeta.getLore().get(itemMeta.getLore().size() - 3).substring(2));
        if(levelNeed < 64){
            if(lvlNow >= levelNeed){
                levelTake(player, levelNeed, artefact, inventory);
            }else {
                player.sendMessage(ChatColor.RED + LangVar.msg_ne.getVar() +" " + string);
            }
        }else {
            levelTake(player, lvlNow, artefact, inventory);
        }

    }*/
}
