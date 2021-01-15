package ru.vixtor141.MagickScrolls.rituals;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.altars.UsualAltar;
import ru.vixtor141.MagickScrolls.interfaces.AltarFace;
import ru.vixtor141.MagickScrolls.interfaces.Ritual;
import ru.vixtor141.MagickScrolls.ritual.RitualE;

import java.util.ArrayList;
import java.util.List;

public class VillagerCast implements Ritual {

    private final RitualE ritual = RitualE.VILLAGER_CAST;
    private final List<ItemStack> reqItems = new ArrayList<>(ritual.getReqItems());
    private AltarFace altar;
    private Location location;

    public VillagerCast(){
    }

    @Override
    public void action() {
        location.getWorld().spawnEntity(location.clone().add(0.5,1,0.5), EntityType.VILLAGER);
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
