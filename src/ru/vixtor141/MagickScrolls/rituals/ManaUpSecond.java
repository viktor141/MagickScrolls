package ru.vixtor141.MagickScrolls.rituals;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.Misc.RitualEnum;
import ru.vixtor141.MagickScrolls.altars.UsualAltar;
import ru.vixtor141.MagickScrolls.effects.RandomParticleGenerator;
import ru.vixtor141.MagickScrolls.interfaces.AltarFace;
import ru.vixtor141.MagickScrolls.interfaces.Ritual;
import ru.vixtor141.MagickScrolls.lang.LangVar;

import java.util.ArrayList;
import java.util.List;

import static ru.vixtor141.MagickScrolls.Misc.RitualEnum.Rituals.MANAUPSECOND;

public class ManaUpSecond implements Ritual {

    private final RitualEnum.Rituals ritual = MANAUPSECOND;
    private final Mana playerMana;
    private final List<ItemStack> reqItems = new ArrayList<>(ritual.getReqItems());;
    private AltarFace altar;
    private Location location;
    private final float state = (float) Main.getPlugin().getConfig().getDouble(ritual.name() + ".state");

    public ManaUpSecond(Mana playerMana){
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
        this.location = location;
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
        if(playerMana.getMaxMana() >= state){
            player.sendMessage(ChatColor.RED + LangVar.msg_ycrtr.getVar());
            return false;
        }
        if(playerMana.getMaxMana() < Main.getPlugin().getConfig().getDouble(RitualEnum.Rituals.MANAUPFIRST.name() + ".state")){
            player.sendMessage(ChatColor.RED + LangVar.msg_ymptpr.getVar());
            return false;
        }
        return true;

    }

    @Override
    public void repeatingEffect() {
        new RandomParticleGenerator(location.clone().add(0.5,2.5,0.5), Particle.SPELL_WITCH, 10, 10);
    }
}
