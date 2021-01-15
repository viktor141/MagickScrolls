package ru.vixtor141.MagickScrolls.rituals;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.altars.UsualAltar;
import ru.vixtor141.MagickScrolls.interfaces.AltarFace;
import ru.vixtor141.MagickScrolls.interfaces.Ritual;
import ru.vixtor141.MagickScrolls.lang.LangVar;
import ru.vixtor141.MagickScrolls.ritual.RitualE;

import java.util.ArrayList;
import java.util.List;

public class ManaUpFirst implements Ritual {

    private final RitualE ritual = RitualE.MANAUPFIRST;
    private final Mana playerMana;
    private final List<ItemStack> reqItems = new ArrayList<>(ritual.getReqItems());
    private AltarFace altar;
    private final float state = (float)Main.getPlugin().getConfig().getDouble(ritual.name() + ".state");

    public ManaUpFirst(Mana playerMana){
        this.playerMana = playerMana;
    }

    @Override
    public void action() {
        playerMana.setMaxMana(state);
    }

    @Override
    public List<ItemStack> getRequiredItems() {
        return reqItems;
    }

    @Override
    public AltarFace getAltar(Location location) {
        altar = new UsualAltar(location, this);
        return altar;
    }

    @Override
    public AltarFace getAltar() {
        return altar;
    }

    @Override
    public RitualE getEnumRitual() {
        return ritual;
    }

    @Override
    public boolean ObjectIsPlayer() {
        return true;
    }

    @Override
    public boolean canExec() {
        Player player = playerMana.getPlayer();
        if(playerMana.getMaxMana() < state){
            return true;
        }
        player.sendMessage(ChatColor.RED + LangVar.msg_ycrtr.getVar());
        return false;
    }

    @Override
    public void repeatingEffect() {

    }

}
