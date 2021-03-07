package ru.vixtor141.MagickScrolls.research;

import org.bukkit.ChatColor;
import ru.vixtor141.MagickScrolls.Misc.TypeOfResearchQuest;
import ru.vixtor141.MagickScrolls.interfaces.ResearchI;
import ru.vixtor141.MagickScrolls.lang.LangVar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.updateItemInInventory;


public class MagnetResearch implements ResearchI {

    private final Research research = Research.MAGNET;
    private final PlayerResearch playerResearch;
    private int itemCount = 0;
    private boolean items;

    public MagnetResearch(PlayerResearch playerResearch){
        this(playerResearch, new HashMap<>());
    }

    public MagnetResearch(PlayerResearch playerResearch, HashMap<String, Integer>  map){
        this.playerResearch = playerResearch;
        if(!map.isEmpty())loadResearchData(map);
        updatePickup();
        upgrade();
    }

    @Override
    public Research getResearch() {
        return research;
    }

    @Override
    public void update(TypeOfResearchQuest typeOfResearchQuest, int number, int position) {
        if (typeOfResearchQuest.equals(TypeOfResearchQuest.PICKUP_ITEMS)){
            itemCount += number;
            updatePickup();
        }
        upgrade();
    }

    private void updatePickup(){
        if(itemCount >= research.getCountOfMaxLvl()){
            items = true;
        }
    }

    private void upgrade(){
        if(items) {
            playerResearch.endResearch(research);
        }else {
            updateDataInItem();
        }
    }

    private void updateDataInItem(){
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.AQUA + LangVar.q_uyl.getVar() + ": " + itemCount + "/" + research.getMaxSpecific("handpickedItems"));
        updateItemInInventory(playerResearch, research, lore);
    }

    @Override
    public HashMap<String, Integer> saveResearchData() {
        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put(research.name() + "_itemCount", itemCount);
        return hashMap;
    }

    private void loadResearchData(HashMap<String, Integer>  map){
        itemCount = map.get(research.name() + "_itemCount");
    }
}
