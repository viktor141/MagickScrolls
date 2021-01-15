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


public class MagnetResearch implements ResearchI {

    private final Research research = Research.MAGNET;
    private final PlayerResearch playerResearch;
    private int itemCount = 0;
    private final Map<EntityType, Integer> maxTypeIntegerMap;
    private final int[] currentKills;
    private boolean killed, items;

    public MagnetResearch(PlayerResearch playerResearch){
        this(playerResearch, null);
    }

    public MagnetResearch(PlayerResearch playerResearch, FileConfiguration fileConfiguration){
        this.playerResearch = playerResearch;
        maxTypeIntegerMap = research.getCountOfMaxMobs();
        currentKills = new int[maxTypeIntegerMap.size()];
        for(int i = 0; i < maxTypeIntegerMap.size(); i++){
            currentKills[i] = 0;
        }
        if(fileConfiguration != null)loadResearchData(fileConfiguration);
        updateKilled();
        updatePickup();
        upgrade();
    }

    @Override
    public Research getResearch() {
        return research;
    }

    @Override
    public void update(TypeOfResearchQuest typeOfResearchQuest, int number, int position) {
        switch (typeOfResearchQuest){
            case MOB_KIll:
                currentKills[position] += number;
                updateKilled();
                break;
            case PICKUP_ITEMS:
                itemCount += number;
                updatePickup();
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

    private void updatePickup(){
        if(itemCount >= research.getCountOfMaxLvl()){
            items = true;
        }
    }

    private void upgrade(){
        updateDataInItem();
        if(killed && items) {
            playerResearch.endResearch(research);
        }
    }

    private void updateDataInItem(){
        List<String> lore = new ArrayList<>();
        updateKills(lore, maxTypeIntegerMap, currentKills);
        lore.add(ChatColor.AQUA + LangVar.q_uyl.getVar() + ": " + itemCount + "/" + research.getMaxSpecific("handpickedItems"));
        updateItemInInventory(playerResearch, research, lore);
    }

    @Override
    public void saveResearchData(FileConfiguration fileConfiguration) {
        fileConfiguration.set(research.name() + ".CurrentKills", currentKills);
        fileConfiguration.set(research.name() + ".itemCount", itemCount);
    }

    private void loadResearchData(FileConfiguration fileConfiguration){
        List<Integer> list = fileConfiguration.getIntegerList(research.name() + ".CurrentKills");
        for(int i = 0; i < list.size(); i++){
            currentKills[i] = list.get(i);
        }
        itemCount = fileConfiguration.getInt(research.name() + ".itemCount");
    }
}
