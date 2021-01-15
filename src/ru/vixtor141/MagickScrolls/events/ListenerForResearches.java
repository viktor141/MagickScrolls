package ru.vixtor141.MagickScrolls.events;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.Misc.TypeOfResearchQuest;
import ru.vixtor141.MagickScrolls.interfaces.ResearchI;
import ru.vixtor141.MagickScrolls.research.PlayerResearch;
import ru.vixtor141.MagickScrolls.research.Research;

import java.util.Iterator;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.getPlayerMana;

public class ListenerForResearches implements Listener {
    private final Research[] mobKillResearches = {Research.ANCIENT_BOTTLE, Research.MANA_SHIELD, Research.MAGNET};
    private final Research[] levelUpResearches = {Research.ANCIENT_BOTTLE};

    @EventHandler
    public void mobKill(EntityDeathEvent event){
        LivingEntity deadEntity = event.getEntity();
        if(deadEntity.getKiller() == null)return;

        Player player = deadEntity.getKiller();
        Mana playerMana = getPlayerMana(player);

        PlayerResearch playerResearch = playerMana.getPlayerResearch();

        for(Research research: mobKillResearches) {
            if(playerResearch.checkResearch(research)){
                Iterator<EntityType> entityTypeIterator = research.getCountOfMaxMobs().keySet().iterator();
                for(int i = 0; i < research.getCountOfMaxMobs().size(); i++) {
                    if(entityTypeIterator.next().equals(deadEntity.getType())){
                        ResearchI researchI = playerResearch.getResearch(research);
                        researchI.update(TypeOfResearchQuest.MOB_KIll, 1, i);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void levelUp(PlayerLevelChangeEvent event){
        Player player = event.getPlayer();
        Mana playerMana = getPlayerMana(player);
        PlayerResearch playerResearch = playerMana.getPlayerResearch();

        for(Research research: levelUpResearches) {
            if(playerResearch.checkResearch(research)){
                ResearchI researchI = playerResearch.getResearch(research);
                researchI.update(TypeOfResearchQuest.LEVEL_UP, event.getNewLevel(), 0);
            }
        }
    }

    @EventHandler
    public void blocking(EntityDamageByEntityEvent event){
        if(!(event.getEntity() instanceof Player))return;
        Player player =(Player) event.getEntity();
        if(!player.isBlocking())return;
        Mana playerMana = getPlayerMana(player);
        PlayerResearch playerResearch = playerMana.getPlayerResearch();

        Research research = Research.MANA_SHIELD;
        if (playerResearch.checkResearch(research)) {
            ResearchI researchI = playerResearch.getResearch(research);
            researchI.update(TypeOfResearchQuest.BLOCKED_DAMAGE, 1, 0);
        }

    }

    @EventHandler
    public void pickup(EntityPickupItemEvent event){
        if(!(event.getEntity() instanceof Player))return;
        Mana playerMana = getPlayerMana((Player) event.getEntity());
        PlayerResearch playerResearch = playerMana.getPlayerResearch();

        Research research = Research.MAGNET;
        if (playerResearch.checkResearch(research)) {
            ResearchI researchI = playerResearch.getResearch(research);
            researchI.update(TypeOfResearchQuest.PICKUP_ITEMS, 1, 0);
        }
    }
}
