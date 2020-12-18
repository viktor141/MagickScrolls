package ru.vixtor141.MagickScrolls.interfaces;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.RitualHandler;

import java.util.List;

public interface AltarFace {

    void builder(Player player);
    boolean checker();
    void behavior(RitualHandler ritualHandler, int witches, int[] neededAmounts);
    List<ItemStack> itemChecker();
    void ritualBrake();
}
