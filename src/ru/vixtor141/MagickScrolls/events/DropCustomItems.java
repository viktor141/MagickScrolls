package ru.vixtor141.MagickScrolls.events;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;

import java.util.Random;

public class DropCustomItems implements Listener {
    private final EntityType[] entityType = {EntityType.ZOMBIE, EntityType.VILLAGER, EntityType.ZOMBIE_VILLAGER, EntityType.EVOKER, EntityType.WITCH, EntityType.HUSK};
    private final boolean[] ent = new boolean[EntityType.values().length];

    public DropCustomItems(){
        for(EntityType entityType: entityType){
            ent[entityType.ordinal()] = true;
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event){
        if(ent[event.getEntityType().ordinal()]){
            if(new Random().nextInt(100) < Main.getPlugin().getConfig().getInt("dropBrainChance")){
                event.getDrops().add(ACCrafts.ItemsCauldronCrafts.BRAIN.craftCauldronGetItem());
            }
        }
    }
}
