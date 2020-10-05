package ru.vixtor141.MagickScrolls;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ru.vixtor141.MagickScrolls.lang.LangVar;

import java.util.ArrayList;
import java.util.List;

public class CDSystem{

    private final Main plugin = Main.getPlugin();

    private List<Integer> CDs;
    private final Player player;
    private final Mana playerMana;

    public enum Scrolls{
        ARROWSTORM,
        LIGHTNING,
        NECROMANCY,
        TELEPORTATION,
        VAMPIRIC,
        VORTEX,
        SPIDERWEB,
        TRAP,
        EARTH,
        ASTRAL_PET
    }

    public List<Integer> getCDs(){
        return CDs;
    }

    public CDSystem(Player player, Mana playerMana){
        this.player = player;
        this.playerMana = playerMana;
        this.CDs = new ArrayList<>(Scrolls.values().length);
    }

    public boolean CDStat(Scrolls scroll, String sConsumedMana, String sCDSeconds, double extraConsumedMana, int extraCDSeconds, boolean isDefaultEffect){
        double consumedMana = plugin.getConfig().getDouble(scroll.name() + sConsumedMana, plugin.getConfig().getDefaults().getDouble(scroll.name() + sConsumedMana));
        int CDSeconds = plugin.getConfig().getInt(scroll.name() + sCDSeconds, plugin.getConfig().getDefaults().getInt(scroll.name() + sCDSeconds));

        consumedMana *= extraConsumedMana;
        CDSeconds += extraCDSeconds;

        if(CDs.get(scroll.ordinal()) > 0) {
            player.sendMessage(ChatColor.RED + LangVar.msg_ycutsa.getVar() + CDs.get(scroll.ordinal()) + " " + LangVar.msg_seconds.getVar());
            return false;
        }
        if(!playerMana.consumeMana(consumedMana))return false;
        CDSet(scroll, CDSeconds);
        if(isDefaultEffect) {
            DefaultEffect defaultEffect = new DefaultEffect(player);
            defaultEffect.defaultEffectOfScrolls();
        }
        return true;

    }

    public boolean CDStat(Scrolls scroll, String sConsumedMana, String sCDSeconds, boolean isDefaultEffect){
        double consumedMana = plugin.getConfig().getDouble(scroll.name() + sConsumedMana, plugin.getConfig().getDefaults().getDouble(scroll.name() + sConsumedMana));
        int CDSeconds = plugin.getConfig().getInt(scroll.name() + sCDSeconds, plugin.getConfig().getDefaults().getInt(scroll.name() + sCDSeconds));

        if(CDs.get(scroll.ordinal()) > 0) {
            player.sendMessage(ChatColor.RED + LangVar.msg_ycutsa.getVar() + CDs.get(scroll.ordinal()) + " " + LangVar.msg_seconds.getVar());
            return false;
        }
        if(!playerMana.consumeMana(consumedMana))return false;
        CDSet(scroll, CDSeconds);
        if(isDefaultEffect) {
           DefaultEffect defaultEffect = new DefaultEffect(player);
           defaultEffect.defaultEffectOfScrolls();
        }
        return true;
    }

    public void CDUpdate(){
        for(int i = 0; i < CDs.size(); i++){
            if(CDs.get(i) > 0){
                CDs.set(i, CDs.get(i) - 1);
            }
        }
    }

    public void CDSet(Scrolls scrolls, int seconds){
        CDs.set(scrolls.ordinal(), seconds);
    }

}