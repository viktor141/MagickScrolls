package ru.vixtor141.MagickScrolls.ritual;

import org.bukkit.inventory.Inventory;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.Misc.RandomStuff;
import ru.vixtor141.MagickScrolls.research.PlayerResearch;
import ru.vixtor141.MagickScrolls.research.Research;

public class RitualBookUpdater {

    public RitualBookUpdater(Mana playerMana, Inventory inventory){
        PlayerResearch playerResearch = playerMana.getPlayerResearch();
        for(RitualE ritual: RitualE.values()){
            boolean flag = true;
            for(Research research: ritual.getNeededResearch()){
                if (!playerResearch.getResearches().get(research.ordinal())) {
                    flag = false;
                    break;
                }
            }
            if(flag){
                inventory.setItem(ritual.ordinal(), ritual.getItem());
            }else {
                inventory.setItem(ritual.ordinal(), RandomStuff.standardGuiCell());
            }
        }
    }
}
