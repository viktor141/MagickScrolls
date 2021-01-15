package ru.vixtor141.MagickScrolls.rituals;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.altars.UsualAltar;
import ru.vixtor141.MagickScrolls.chatPlay.ImpSay;
import ru.vixtor141.MagickScrolls.interfaces.AltarFace;
import ru.vixtor141.MagickScrolls.interfaces.Ritual;
import ru.vixtor141.MagickScrolls.mobs.VillagerIMP;
import ru.vixtor141.MagickScrolls.ritual.RitualE;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ImpSummon implements Ritual {

    private final RitualE ritual = RitualE.IMP_SUMMON;
    private final List<ItemStack> reqItems = new ArrayList<>(ritual.getReqItems());
    private final Player player;
    private AltarFace altar;
    private Location location;
    public ImpSummon(Player player){
        this.player = player;
    }

    @Override
    public void action() {
        new VillagerIMP(player, location);
        List<String> messagesToPlayer = Main.getPlugin().getLangCF().getStringList("imp.messages");
        String message = messagesToPlayer.get(new Random().nextInt(messagesToPlayer.size()));
        player.sendMessage(new ImpSay(message).changeMessage());
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
    public RitualE getEnumRitual() {
        return ritual;
    }

    @Override
    public boolean ObjectIsPlayer() {
        return false;
    }

    @Override
    public boolean canExec() {
        return true;
    }

    @Override
    public void repeatingEffect() {

    }
}
