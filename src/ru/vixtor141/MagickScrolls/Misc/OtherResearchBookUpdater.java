package ru.vixtor141.MagickScrolls.Misc;

import org.bukkit.inventory.Inventory;
import ru.vixtor141.MagickScrolls.research.Research;

import java.util.List;

public class OtherResearchBookUpdater {

    public OtherResearchBookUpdater(Inventory inventory, List<Boolean> researches){
        for (Menu menu : Menu.values()) {
            boolean flag = true;
            for(Research neededResearch: menu.getNeededResearch()) {
                if(!researches.get(neededResearch.ordinal())){
                    flag = false;
                    break;
                }
            }
            if (flag) {
                inventory.setItem(menu.getPosition(), menu.getItem());
            }else {
                inventory.setItem(menu.getPosition(), RandomStuff.standardGuiCell());
            }
        }
    }
}
