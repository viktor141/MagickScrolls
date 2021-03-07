package ru.vixtor141.MagickScrolls.research;

import org.bukkit.ChatColor;
import ru.vixtor141.MagickScrolls.Misc.TypeOfResearchQuest;
import ru.vixtor141.MagickScrolls.interfaces.ResearchI;
import ru.vixtor141.MagickScrolls.lang.LangVar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.updateItemInInventory;

public class ManaShieldResearch implements ResearchI {

    private final Research research = Research.MANA_SHIELD;
    private final PlayerResearch playerResearch;
    private boolean blocked = false;
    private int blockedDamage = 0;

    public ManaShieldResearch(PlayerResearch playerResearch){
        this(playerResearch, new HashMap<>());
    }

    public ManaShieldResearch(PlayerResearch playerResearch, HashMap<String, Integer>  map){
        this.playerResearch = playerResearch;
        if(!map.isEmpty())loadResearchData(map);
        updateBlockedDamage();
        upgrade();
    }

    @Override
    public Research getResearch() {
        return research;
    }

    @Override
    public void update(TypeOfResearchQuest typeOfResearchQuest, int number, int position) {
        if(typeOfResearchQuest == TypeOfResearchQuest.BLOCKED_DAMAGE) {
            blockedDamage += number;
            updateBlockedDamage();
        }
        upgrade();
    }

    private void updateBlockedDamage(){
        if(blockedDamage >= research.getMaxSpecific("blockedDamage")){
            blocked = true;
        }
    }

    private void upgrade () {
        if (blocked) {
            playerResearch.endResearch(research);
        }else {
            updateDataInItem();
        }
    }

    private void updateDataInItem () {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.AQUA + LangVar.q_ntba.getVar() + ": " + blockedDamage + "/" + research.getMaxSpecific("blockedDamage"));
        updateItemInInventory(playerResearch, research, lore);
    }

    @Override
    public HashMap<String, Integer> saveResearchData(){
        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put(research.name() + "_BlockedDamage", blockedDamage);
        return hashMap;
    }

    private void loadResearchData (HashMap<String, Integer>  map){
        blockedDamage = map.get(research.name() + "_BlockedDamage");
    }
}
