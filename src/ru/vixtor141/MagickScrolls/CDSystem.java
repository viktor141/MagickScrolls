package ru.vixtor141.MagickScrolls;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CDSystem{

    private List<Integer> CDs;
    private Player player;
    public enum Scrolls{
        ARROWSTORM,
        LIGHTNING,
        NECROMANCY,
        TELEPORTATION,
        VAMPIRIC,
        VORTEX;
    }

    public List<Integer> getCDs() {
        return CDs;
    }

    public CDSystem(Player player){
        this.player = player;
        this.CDs = new ArrayList<>(Scrolls.values().length);
    }

    public boolean CDStat(Scrolls scroll, Mana playerMana, double consumedMana, int CDSeconds){
        if(CDs.get(scroll.ordinal()) > 0) {
            player.sendMessage("You can use this scroll after " + CDs.get(scroll.ordinal()) + " seconds");
            return false;
        }
        if(!playerMana.consumeMana(consumedMana))return false;
        CDSet(scroll, CDSeconds);
        playerMana.getDefaultEffect().defaultEffectOfScrolls();
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