package ru.vixtor141.MagickScrolls;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import ru.vixtor141.MagickScrolls.interfaces.AltarFace;
import ru.vixtor141.MagickScrolls.interfaces.Ritual;
import ru.vixtor141.MagickScrolls.lang.LangVar;

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
        if(!checkReqItems(ritual.getRequiredItems(), altar.itemChecker())){
            player.sendMessage(ChatColor.RED + LangVar.msg_nitr.getVar());
            return;
        }
        if(!ritual.canExec()){
            player.sendMessage(ChatColor.RED + LangVar.msg_ycrtr.getVar());
            return;
        }
        int needW = Main.getPlugin().getConfig().getInt(ritual.getEnumRitual().name() + ".witchesNeed");
        if(needW > witchCount){
            player.sendMessage(ChatColor.RED + LangVar.msg_newit.getVar());
            player.sendMessage(ChatColor.RED + LangVar.msg_n.getVar() + needW);
            return;
        }

        if(ritual.ObjectIsPlayer())player.teleport(location.clone().add(0.5,1,0.5));
        playerMana.setInRitualChecker(ritual.ObjectIsPlayer());
        altar.behavior(this, needW);

    }

    public void ritualEnd(){
        ritual.action();
        playerMana.setInRitualChecker(false);
    }

}
