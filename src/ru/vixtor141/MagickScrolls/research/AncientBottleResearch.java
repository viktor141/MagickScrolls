package ru.vixtor141.MagickScrolls.research;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import ru.vixtor141.MagickScrolls.Misc.TypeOfResearchQuest;
import ru.vixtor141.MagickScrolls.interfaces.ResearchI;
import ru.vixtor141.MagickScrolls.lang.LangVar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.updateItemInInventory;
import static ru.vixtor141.MagickScrolls.Misc.CheckUp.updateKills;

public class AncientBottleResearch implements ResearchI {

    private final Research research = Research.ANCIENT_BOTTLE;
    private int currentLvl;
    private final PlayerResearch playerResearch;
    private boolean killed = false, lvl = false;
    private final Map<EntityType, Integer> maxTypeIntegerMap;
    private final int[] currentKills;

    public AncientBottleResearch(PlayerResearch playerResearch){
        this(playerResearch, null);
    }

    public AncientBottleResearch(PlayerResearch playerResearch, FileConfiguration fileConfiguration){
        this.playerResearch = playerResearch;
        maxTypeIntegerMap = research.getCountOfMaxMobs();
        currentKills = new int[maxTypeIntegerMap.size()];
        for(int i = 0; i < maxTypeIntegerMap.size(); i++){
            currentKills[i] = 0;
        }
        if(fileConfiguration != null)loadResearchData(fileConfiguration);
        updateKilled();
        update(TypeOfResearchQuest.LEVEL_UP, playerResearch.getPlayer().getLevel(), 0);
    }

    @Override
    public void update(TypeOfResearchQuest typeOfResearchQuest, int number, int position){//may be callable
        switch (typeOfResearchQuest){
            case MOB_KIll:
                currentKills[position] += number;
                updateKilled();
                break;
            case LEVEL_UP:
                if(currentLvl < number){
                    currentLvl = number;
                }
                updateLvl();
                break;
        }
        upgrade();
    }

    private void updateKilled(){
        boolean currentCheck = true;
        for(int i = 0;  i < maxTypeIntegerMap.size(); i++){
            if(currentKills[i] < (int)maxTypeIntegerMap.values().toArray()[i]){
                currentCheck = false;
                break;
            }
        }
        killed = currentCheck;
    }

    private void updateLvl(){
        if(currentLvl >= research.getCountOfMaxLvl()){
            lvl = true;
        }
    }

    private void upgrade(){
        updateDataInItem();
        if(killed && lvl) {
            playerResearch.endResearch(research);
        }
    }

    private void updateDataInItem(){
        List<String> lore = new ArrayList<>();
        updateKills(lore, maxTypeIntegerMap, currentKills);
        lore.add(ChatColor.AQUA + LangVar.q_uyl.getVar() + ": " + currentLvl + "/" + research.getCountOfMaxLvl());
        updateItemInInventory(playerResearch, research, lore);
    }

    @Override
    public Research getResearch() {
        return research;
    }

    @Override
    public void saveResearchData(FileConfiguration fileConfiguration){
        fileConfiguration.set(research.name() + ".CurrentKills", currentKills);
        fileConfiguration.set(research.name() + ".CurrentLvl", currentLvl);
    }

    private void loadResearchData(FileConfiguration fileConfiguration){
        List<Integer> list = fileConfiguration.getIntegerList(research.name() + ".CurrentKills");
        for(int i = 0; i < list.size(); i++){
            currentKills[i] = list.get(i);
        }
        currentLvl = fileConfiguration.getInt(research.name() + ".CurrentLvl");
    }
}
