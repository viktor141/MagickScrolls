package ru.vixtor141.MagickScrolls;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import ru.vixtor141.MagickScrolls.interfaces.AltarFace;
import ru.vixtor141.MagickScrolls.interfaces.Ritual;
import ru.vixtor141.MagickScrolls.lang.LangVar;

import java.util.ArrayList;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.checkReqItems;

public class RitualHandler {

    private final Mana playerMana;
    private final Ritual ritual;

    public RitualHandler(Player player, Location location, int witchCount){
        playerMana = Main.getPlugin().getPlayerMap().get(player);
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
        altar.behavior(this, needW);
    }

    public void ritualEnd(){
        playerMana.setInRitualChecker(false);
        ritual.action();
    }

}
