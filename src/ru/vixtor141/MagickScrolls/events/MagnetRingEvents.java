package ru.vixtor141.MagickScrolls.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import ru.vixtor141.MagickScrolls.Misc.FlyingItemsForPlayer;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.getPlayerMana;

public class MagnetRingEvents implements Listener {

    @EventHandler
    public void dropFromDead(EntityDeathEvent event){
        Player player = event.getEntity().getKiller();
        if(player == null)return;
        setItemsFly(player, event.getEntity().getLocation());
    }

    @EventHandler
    public void blockBreak(BlockBreakEvent event){
        Player player =event.getPlayer();
        if(player == null)return;
        setItemsFly(player, event.getBlock().getLocation());
    }

    private void setItemsFly(Player player, Location location){
        if(!getPlayerMana(player).getWearingArtefact().get(ACCrafts.AccessoryArtefact.MAGNET.ordinal()))return;
        if(!getPlayerMana(player).consumeManaForMagnet(2))return;
        new FlyingItemsForPlayer(getPlayerMana(player), location);
    }

}
