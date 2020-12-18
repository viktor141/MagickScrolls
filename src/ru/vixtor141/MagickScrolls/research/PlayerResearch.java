package ru.vixtor141.MagickScrolls.research;

import org.bukkit.entity.Player;
import ru.vixtor141.MagickScrolls.Mana;

import java.util.ArrayList;
import java.util.List;

public class PlayerResearch{

    private List<Boolean> Researches;

    public PlayerResearch(){
        this.Researches = new ArrayList<>(Research.values().length);
    }

    public List<Boolean> getResearches() {
        return Researches;
    }
}
