package ru.vixtor141.MagickScrolls.research;

import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class ResearchBookUpdater {

    public ResearchBookUpdater(Inventory inventory, List<Boolean> researches) {
        for (Research research : Research.values()) {
            boolean flag = true;
            for(Research neededResearch: research.getNeededResearch()) {
                if(!researches.get(neededResearch.ordinal())){
                    flag = false;
                    break;
                }
            }
            if (flag) {
                if (researches.get(research.ordinal())) {
                    inventory.setItem(research.getPosition(), research.getResearchItem(ChatColor.GREEN + "✔"));
                } else {
                    if(!inventory.getItem(research.getPosition()).getType().equals(research.getMaterialType())) {
                        inventory.setItem(research.getPosition(), research.getResearchItem(ChatColor.RED + "✖"));
                    }
                }

            }
        }
    }
}
