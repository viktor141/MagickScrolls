package ru.vixtor141.MagickScrolls;

import org.bukkit.Bukkit;
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
        ASTRAL_PET,
        METEORITE,
        SPECTRAL_SHIELD,
        AIR_TRAP,
        CONFUSED
    }

    public enum RitualsCD{
        MINER,
        WARRIOR,
        DIVER,
        TRAVELER
    }

    public List<Integer> getCDs(){
        return CDs;
    }

    public CDSystem(Player player, Mana playerMana){
        this.player = player;
        this.playerMana = playerMana;
        this.CDs = new ArrayList<>(CDsValuesLength());
    }

    public boolean CDStat(Scrolls scroll, double extraConsumedMana, int extraCDSeconds, boolean isDefaultEffect){
        return CDStatResult(scroll, getConsumedMana(scroll, ".consumedMana") * extraConsumedMana, getCDSeconds(scroll, ".CDseconds") + extraCDSeconds, isDefaultEffect);
    }

    public boolean CDStat(Scrolls scroll, String sConsumedMana, String sCDSeconds, boolean isDefaultEffect){
        return CDStatResult(scroll, getConsumedMana(scroll, sConsumedMana),  getCDSeconds(scroll, sCDSeconds), isDefaultEffect);
    }

    public boolean CDStat(Scrolls scroll, boolean isDefaultEffect){
        return CDStatResult(scroll, getConsumedMana(scroll, ".consumedMana"), getCDSeconds(scroll, ".CDseconds"), isDefaultEffect);
    }

    public boolean CDStat(RitualsCD ritualsCD){
        int CDSeconds = plugin.getConfig().getInt(ritualsCD.name() + ".CDseconds", plugin.getConfig().getDefaults().getInt(ritualsCD.name() + ".CDseconds"));

        if(CDs.get(Scrolls.values().length + ritualsCD.ordinal()) > 0) {
            player.sendMessage(ChatColor.RED + LangVar.msg_ycutsa.getVar() + CDs.get(Scrolls.values().length + ritualsCD.ordinal()) + " " + LangVar.msg_seconds.getVar());
            return false;
        }
        CDs.set(Scrolls.values().length + ritualsCD.ordinal(), CDSeconds);
        return true;
    }

    private boolean CDStatResult(Scrolls scroll, double consumedMana, int CDSeconds, boolean isDefaultEffect){
        if(CDs.get(scroll.ordinal()) > 0) {
            player.sendMessage(ChatColor.RED + LangVar.msg_ycutsa.getVar() + CDs.get(scroll.ordinal()) + " " + LangVar.msg_seconds.getVar());
            return false;
        }
        if(!playerMana.consumeMana(consumedMana))return false;
        CDSet(scroll, CDSeconds);
        if(isDefaultEffect) {

            Bukkit.getScheduler().runTask(plugin, () -> new DefaultEffect(player));
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

    public void CDSet(Scrolls scroll, int seconds){
        CDs.set(scroll.ordinal(), seconds);
    }

    public void CDSet(RitualsCD ritual, int seconds){
        CDs.set(Scrolls.values().length + ritual.ordinal(), seconds);
    }

    public void CDSet(int i, int seconds){
        CDs.set(i, seconds);
    }

    public static int CDsValuesLength(){
        return Scrolls.values().length + RitualsCD.values().length;
    }

    private double getConsumedMana(Scrolls scroll, String path){
        return plugin.getConfig().getDouble(scroll.name() + path, plugin.getConfig().getDefaults().getDouble(scroll.name() + path));
    }

    private int getCDSeconds(Scrolls scroll, String path){
        return plugin.getConfig().getInt(scroll.name() + path, plugin.getConfig().getDefaults().getInt(scroll.name() + path));
    }

}