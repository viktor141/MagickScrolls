package ru.vixtor141.MagickScrolls;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.interfaces.AltarFace;
import ru.vixtor141.MagickScrolls.interfaces.Ritual;
import ru.vixtor141.MagickScrolls.lang.LangVar;

import java.util.ArrayList;
import java.util.List;

public class RitualHandler {

    private Mana playerMana;
    private Ritual ritual;
    private int[] neededAmounts;


    public RitualHandler(Player player, Location location, int witchCount){
        if(!player.hasMetadata("MagickScrollsMana")){
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "WARNING!!! Player: " + player.getDisplayName() + " lost a plugin meta.");
            return;
        }
        playerMana =(Mana) player.getMetadata("MagickScrollsMana").get(0).value();
        ritual = playerMana.getRitual();
        if(ritual == null){
            player.sendMessage(ChatColor.RED + LangVar.msg_rins.getVar());
            return;
        }
        if(playerMana.getInRitualChecker())return;
        AltarFace altar = ritual.getAltar(location);
        if(!altar.checker()){
            player.sendMessage(ChatColor.RED + LangVar.msg_ira.getVar());
            return;
        }
        if(!checkReqItems(new ArrayList<>(ritual.getRequiredItems()), new ArrayList<>(altar.itemChecker()))){
            player.sendMessage(ChatColor.RED + LangVar.msg_nitr.getVar());
            return;
        }
        int needW = Main.getPlugin().getConfig().getInt(ritual.getEnumRitual().name() + ".witchesNeed");
        if(needW > witchCount){
            player.sendMessage(ChatColor.RED + LangVar.msg_newit.getVar());
            player.sendMessage(ChatColor.RED + LangVar.msg_n.getVar() + needW);
            return;
        }
        if(!ritual.canExec()){
            return;
        }
        if(!playerMana.consumeMana(Main.getPlugin().getConfig().getDouble(ritual.getEnumRitual().name() + ".needMana"))){
            return;
        }

        if(ritual.ObjectIsPlayer())player.teleport(location.clone().add(0.5,1,0.5));
        playerMana.setInRitualChecker(ritual.ObjectIsPlayer());
        altar.behavior(this, needW, neededAmounts);
    }

    public void ritualEnd(){
        playerMana.setInRitualChecker(false);
        ritual.action();
    }

    private boolean checkReqItems(List<ItemStack> reqItems, List<ItemStack> items){
        if(reqItems.size() != items.size())return false;
        neededAmounts = new int[items.size()];
        int k = 0;
        List<ItemStack> removingRealList = new ArrayList<>(items);

        for (ItemStack realItem : items) {
            for(ItemStack recipeItem : reqItems) {
                if (realItem.getType().equals(recipeItem.getType()) && recipeItem.getAmount() <= realItem.getAmount() && realItem.getDurability() == recipeItem.getDurability()) {
                    if ((!realItem.getItemMeta().hasLore() && !recipeItem.getItemMeta().hasLore()) || (realItem.getItemMeta().hasLore() && recipeItem.getItemMeta().hasLore() && recipeItem.getItemMeta().getLore().get(0).equals(realItem.getItemMeta().getLore().get(realItem.getItemMeta().getLore().size() - 2).substring(Main.getPlugin().getSubStr())))) {
                        reqItems.remove(recipeItem);
                        removingRealList.remove(realItem);
                        neededAmounts[k] = recipeItem.getAmount();
                        k++;
                        break;
                    }
                }
            }
        }
        return reqItems.isEmpty() && removingRealList.isEmpty();
    }

}
