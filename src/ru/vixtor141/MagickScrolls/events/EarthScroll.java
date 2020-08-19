package ru.vixtor141.MagickScrolls.events;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.Misc.CheckUp;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;
import ru.vixtor141.MagickScrolls.tasks.EarthScrollTask;


import static java.lang.Math.*;
import static java.lang.Math.toRadians;
import static ru.vixtor141.MagickScrolls.Misc.CheckUp.checkScrollEvent;

public class EarthScroll implements Listener {

    private final int r = 7;
    private final float angle = 90;
    private Player player;

    @EventHandler
    public void use(PlayerInteractEvent event){
        if(checkScrollEvent(event))return;
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if(!CheckUp.checkItemLore(ACCrafts.CraftsOfScrolls.EARTH, item)) return;

        player = event.getPlayer();
        Mana playerMana = Main.getPlugin().getPlayerMap().get(player);

        if(!playerMana.getCdSystem().CDStat(CDSystem.Scrolls.EARTH , ".consumedMana", ".CDseconds", true))return;

        Location viewLocation = getView(event.getPlayer().getLocation());

        spawnEarthWave(viewLocation, event.getPlayer().getLocation());

        if(!player.getGameMode().equals(GameMode.CREATIVE)) {
            item.setAmount(item.getAmount() - 1);
            event.getPlayer().getInventory().setItemInMainHand(item);
        }
    }

    private Location getView(Location locationOfP){
        double  z = r * cos(toRadians(locationOfP.getYaw())), x = r * sin(toRadians(locationOfP.getYaw()));
        return locationOfP.clone().add( x * ((double)-1), 0, z);
    }

    @EventHandler
    public void onChange(EntityChangeBlockEvent event) {
        if(!event.getEntityType().equals(EntityType.FALLING_BLOCK))return;
        if(!event.getEntity().hasMetadata("magickscrolls_earthScoll"))return;
        event.setCancelled(true);
    }


    private void spawnEarthWave(Location viewLocation, Location locationOfP){
        EarthScrollTask task = new EarthScrollTask( r, angle, locationOfP.clone(), viewLocation.clone().subtract(locationOfP.clone()).toVector(), player);
    }



}
