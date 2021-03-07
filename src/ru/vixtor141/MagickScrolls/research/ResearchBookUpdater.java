package ru.vixtor141.MagickScrolls.research;

import org.bukkit.inventory.Inventory;
import ru.vixtor141.MagickScrolls.Misc.RandomStuff;
import ru.vixtor141.MagickScrolls.interfaces.ResearchI;

import java.util.List;

public class ResearchBookUpdater {

    public ResearchBookUpdater(Inventory inventory, List<Boolean> researches, List<ResearchI> activeResearch) {
        for (Research research : Research.values()) {
            boolean flag = true;
            for(Research neededResearch: research.getNeededResearch()) {
                if(!researches.get(neededResearch.ordinal())){
                    flag = false;
                    break;
                }
            }
            if (flag && activeResearch.get(research.ordinal()) == null) {
                if (researches.get(research.ordinal())) {
                    inventory.setItem(research.getPosition(), research.getResearchItem(true));
                } else {
                    if(!inventory.getItem(research.getPosition()).equals(research.getResearchItem(false))) {
                        inventory.setItem(research.getPosition(), research.getResearchItem(false));
                    }
                }

            }else if(activeResearch.get(research.ordinal()) == null){
                inventory.setItem(research.getPosition(), RandomStuff.standardGuiCell());
            }
        }
    }
}
