package ru.vixtor141.MagickScrolls.interfaces;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.Misc.RitualEnum;

import java.util.List;

public interface Ritual {

    void action();
    List<ItemStack> getRequiredItems();
    AltarFace getAltar(Location location);
    AltarFace getAltar();
    RitualEnum.Rituals getEnumRitual();
    boolean ObjectIsPlayer();
    boolean canExec();
}
