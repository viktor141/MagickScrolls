package ru.vixtor141.MagickScrolls.research;

import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResearchDataSaver {

    private final List<Map<EntityType, Integer>> entitiesKills;

    public ResearchDataSaver(){
        entitiesKills = new ArrayList<>();
        for(int i = 0; i<Research.values().length; i++){
            entitiesKills.add(new HashMap<>());
        }
    }

    public void put(Research research, EntityType entityType, int count){
        entitiesKills.get(research.ordinal()).put(entityType, count);
    }

    public Map<EntityType, Integer> getMapForResearch(Research research) {
        return entitiesKills.get(research.ordinal());
    }
}
