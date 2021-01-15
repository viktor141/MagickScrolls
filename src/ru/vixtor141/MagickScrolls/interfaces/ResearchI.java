package ru.vixtor141.MagickScrolls.interfaces;

import org.bukkit.configuration.file.FileConfiguration;
import ru.vixtor141.MagickScrolls.Misc.TypeOfResearchQuest;
import ru.vixtor141.MagickScrolls.research.Research;

public interface ResearchI {

    Research getResearch();
    void update(TypeOfResearchQuest typeOfResearchQuest, int number, int position);
    void saveResearchData(FileConfiguration fileConfiguration);
}
