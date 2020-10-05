package ru.vixtor141.MagickScrolls.rituals;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Misc.RitualEnum;
import ru.vixtor141.MagickScrolls.altars.UsualAltar;
import ru.vixtor141.MagickScrolls.interfaces.AltarFace;
import ru.vixtor141.MagickScrolls.interfaces.Ritual;

import java.util.ArrayList;
import java.util.List;

import static ru.vixtor141.MagickScrolls.Misc.RitualEnum.Rituals.VILLAGER_CAST;

public class VillagerCast implements Ritual {

    private final RitualEnum.Rituals ritual = VILLAGER_CAST;
    private final List<ItemStack> reqItems;
    private AltarFace altar;
    private Location location;

    public VillagerCast(){
        reqItems = new ArrayList<>((List<ItemStack>) Main.getPlugin().getRitualsCF().getList(ritual.name()));
    }

    @Override
    public void action() {
        location.getWorld().spawnEntity(location.clone().add(0.5,1,0.5), EntityType.VILLAGER);
    }

    @Override
    public List<ItemStack> getRequiredItems() {
        return new ArrayList<>(reqItems);
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
        return false;
    }

    @Override
    public boolean canExec() {
        return true;
    }
}
