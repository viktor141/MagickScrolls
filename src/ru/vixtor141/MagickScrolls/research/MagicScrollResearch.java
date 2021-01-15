package ru.vixtor141.MagickScrolls.research;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import ru.vixtor141.MagickScrolls.Misc.TypeOfResearchQuest;
import ru.vixtor141.MagickScrolls.interfaces.ResearchI;
import ru.vixtor141.MagickScrolls.lang.LangVar;

import java.util.ArrayList;
import java.util.List;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.updateItemInInventory;

public class MagicScrollResearch implements ResearchI {

    private final Research research = Research.MAGIC_SCROLL;
    private final PlayerResearch playerResearch;
    private int count = 0;

    public MagicScrollResearch(PlayerResearch playerResearch){
        this.playerResearch = playerResearch;
        upgrade();
    }

    public MagicScrollResearch(PlayerResearch playerResearch, FileConfiguration fileConfiguration){
        this.playerResearch = playerResearch;
        loadResearchData(fileConfiguration);
        upgrade();
    }

    @Override
    public Research getResearch() {
        return research;
    }

    @Override
    public void update(TypeOfResearchQuest typeOfResearchQuest, int number, int position) {
        if(typeOfResearchQuest.equals(TypeOfResearchQuest.USE_SCROLL)){
            count += number;
        }
        upgrade();
    }

    private void upgrade(){
        updateDataInItem();
        if(count >= research.getMaxUses()){
            playerResearch.endResearch(research);
        }
    }

    private void updateDataInItem(){
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.AQUA + LangVar.q_ntu.getVar() + ": " +  count + "/" + research.getMaxUses());
        updateItemInInventory(playerResearch, research, lore);
    }

    @Override
    public void saveResearchData(FileConfiguration fileConfiguration) {
        fileConfiguration.set(research.name() + ".uses", count);
    }

    private void loadResearchData(FileConfiguration fileConfiguration){
        count = fileConfiguration.getInt(research.name() + ".uses");
    }
}
