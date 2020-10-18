package ru.vixtor141.MagickScrolls.rituals;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.Misc.RitualEnum;
import ru.vixtor141.MagickScrolls.altars.UsualAltar;
import ru.vixtor141.MagickScrolls.interfaces.AltarFace;
import ru.vixtor141.MagickScrolls.interfaces.Ritual;
import ru.vixtor141.MagickScrolls.lang.LangVar;

import java.util.ArrayList;
import java.util.List;

import static ru.vixtor141.MagickScrolls.Misc.RitualEnum.Rituals.MANAUPFIRST;

public class ManaUpFirst implements Ritual {

    private final RitualEnum.Rituals ritual = MANAUPFIRST;
    private final Mana playerMana;
    private final List<ItemStack> reqItems;
    private AltarFace altar;
    private final float state = (float)Main.getPlugin().getConfig().getDouble(ritual.name() + ".state");

    public ManaUpFirst(Mana playerMana){
        this.playerMana = playerMana;
        reqItems = new ArrayList<>((List<ItemStack>)Main.getPlugin().getRitualsCF().getList(ritual.name()));
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
    public RitualEnum.Rituals getEnumRitual() {
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
