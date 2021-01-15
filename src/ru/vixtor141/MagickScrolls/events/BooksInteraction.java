package ru.vixtor141.MagickScrolls.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.lang.LangVar;
import ru.vixtor141.MagickScrolls.levels.ManaShieldLevel;
import ru.vixtor141.MagickScrolls.levels.ShieldManaLevels;
import ru.vixtor141.MagickScrolls.research.PlayerResearch;
import ru.vixtor141.MagickScrolls.research.Research;
import ru.vixtor141.MagickScrolls.ritual.RitualE;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.getPlayerMana;

public class BooksInteraction implements Listener {

    @EventHandler
    public void clickOnResearch(InventoryClickEvent event){
        if(!(event.getWhoClicked() instanceof Player))return;
        Player player = (Player)event.getWhoClicked();
        Mana playerMana = getPlayerMana(player);
        if(event.getClickedInventory() == null)return;
        PlayerResearch playerResearch = playerMana.getPlayerResearch();
        if(event.getClickedInventory().equals(playerResearch.getResearchBookInventory())) {
            event.setCancelled(true);

            for (Research research : Research.values()) {
                if (event.getSlot() == research.getPosition()) {
                    if (!playerResearch.getResearches().get(research.ordinal())) {
                        if (playerResearch.checkResearch(research)) {
                            player.sendMessage(ChatColor.RED + LangVar.msg_yastr.getVar());
                        }
                        research.start(playerResearch);
                    } else {
                        player.sendMessage(ChatColor.RED + LangVar.msg_yaltr.getVar());
                    }
                    return;
                }
            }
        }else if(event.getClickedInventory().equals(playerMana.getPlayerRitualInventory().getRitualInventory())){
            event.setCancelled(true);
            if(event.getSlot() < RitualE.values().length) {
                playerMana.setRitual(RitualE.values()[event.getSlot()]);
                player.sendMessage(ChatColor.GREEN + RitualE.values()[event.getSlot()].getRitualName() + " " + LangVar.msg_sr.getVar());
            }
        }else if(event.getClickedInventory().equals(playerMana.getPlayerResearch().getShieldManaLevels().getInventory())){
            event.setCancelled(true);
            if(event.getSlot() < ManaShieldLevel.values().length){
                ShieldManaLevels shieldManaLevels = playerResearch.getShieldManaLevels();
                if(shieldManaLevels.getManaShieldLevel().ordinal()+1 == ManaShieldLevel.values().length)return;
                ManaShieldLevel nextManaShieldLevel = ManaShieldLevel.values()[shieldManaLevels.getManaShieldLevel().ordinal()+1];
                if(event.getSlot() == nextManaShieldLevel.ordinal()){
                    if(player.getLevel() >= nextManaShieldLevel.getXpLevel()){
                        player.setLevel(player.getLevel() - nextManaShieldLevel.getXpLevel());
                        shieldManaLevels.updateManaShieldLevel(nextManaShieldLevel);
                        player.sendMessage(ChatColor.GREEN + LangVar.msg_ybanl.getVar());
                    }else {
                        player.sendMessage(ChatColor.GREEN + LangVar.msg_ydhel.getVar());
                    }
                }else if(event.getSlot() > nextManaShieldLevel.ordinal()){
                    player.sendMessage(ChatColor.RED + LangVar.msg_ymbpl.getVar());
                }else {
                    player.sendMessage(ChatColor.YELLOW + LangVar.msg_yhabtl.getVar());
                }

            }
        }
    }
}
