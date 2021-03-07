package ru.vixtor141.MagickScrolls.research;

import ru.vixtor141.MagickScrolls.aspects.Aspect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResearchDataSaver {

    private final List<Map<Aspect, Integer>> aspectsNeeded;

    public ResearchDataSaver(){
        aspectsNeeded = new ArrayList<>();
        for(int i = 0; i<Research.values().length; i++){
            aspectsNeeded.add(new HashMap<>());
        }
    }

    public void put(Research research, Aspect aspect, int count){
        aspectsNeeded.get(research.ordinal()).put(aspect, count);
    }

    public Map<Aspect, Integer> getMapForResearch(Research research) {
        return aspectsNeeded.get(research.ordinal());
    }
}
