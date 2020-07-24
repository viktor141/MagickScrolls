package ru.vixtor141.MagickScrolls;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CDSystem{

    private final Main plugin = Main.getPlugin();

    private List<Integer> CDs;
    private final Player player;
    public enum Scrolls{
        ARROWSTORM,
        LIGHTNING,
        NECROMANCY,
        TELEPORTATION,
        VAMPIRIC,
        VORTEX,
        SPIDERWEB,
        TRAP
    }

    public List<Integer> getCDs(){
        return CDs;
    }

    public CDSystem(Player player){
        this.player = player;
        this.CDs = new ArrayList<>(Scrolls.values().length);
    }

    public boolean CDStatE(Scrolls scroll, Mana playerMana, String sConsumedMana, String sCDSeconds, double extraConsumedMana, int extraCDSeconds, boolean isDefaultEffect){
        double consumedMana = plugin.getConfig().getDouble(scroll.name() + sConsumedMana, plugin.getConfig().getDefaults().getDouble(scroll.name() + sConsumedMana));
        int CDSeconds = plugin.getConfig().getInt(scroll.name() + sCDSeconds, plugin.getConfig().getDefaults().getInt(scroll.name() + sCDSeconds));

        consumedMana *= extraConsumedMana;
        CDSeconds += extraCDSeconds;

        if(CDs.get(scroll.ordinal()) > 0) {
            player.sendMessage(ChatColor.RED + plugin.getReadingLangFile().getMsg("msg_ycutsa") + CDs.get(scroll.ordinal()) + " " + plugin.getReadingLangFile().getMsg("msg_seconds"));
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

    public boolean CDStat(Scrolls scroll, Mana playerMana, String sConsumedMana, String sCDSeconds, boolean isDefaultEffect){
        double consumedMana = plugin.getConfig().getDouble(scroll.name() + sConsumedMana, plugin.getConfig().getDefaults().getDouble(scroll.name() + sConsumedMana));
        int CDSeconds = plugin.getConfig().getInt(scroll.name() + sCDSeconds, plugin.getConfig().getDefaults().getInt(scroll.name() + sCDSeconds));

        if(CDs.get(scroll.ordinal()) > 0) {
            player.sendMessage(ChatColor.RED + plugin.getReadingLangFile().getMsg("msg_ycutsa") + CDs.get(scroll.ordinal()) + " " + plugin.getReadingLangFile().getMsg("msg_seconds"));
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