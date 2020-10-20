package ru.vixtor141.MagickScrolls.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.BlockState;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.lang.LangVar;
import ru.vixtor141.MagickScrolls.tasks.CleanUpTask;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.itemConsumer;

public class SpiderWebScroll implements Listener {

    private final Main plugin = Main.getPlugin();
    private final CDSystem.Scrolls scroll = CDSystem.Scrolls.SPIDERWEB;

    @EventHandler
    public void hitEvent(ProjectileHitEvent event){
        if(event.getHitBlock() != null)return;
        if(event.getEntity().hasMetadata("magickscrolls")){
            Entity entity = event.getHitEntity();
            if(entity instanceof ItemFrame || entity instanceof Painting)return;
            if(!(event.getEntity().getShooter() instanceof Player))return;
            Player player =(Player) event.getEntity().getShooter();
            Mana playerMana = plugin.getPlayerMap().get(player);
            BlockState[] blockStates = new BlockState[2];
            Location location = event.getHitEntity().getLocation();
            Location setBlockLoc;
            if(playerMana.getCurrentMana() < plugin.getConfig().getDouble(scroll.name() + ".consumedMana")){
                player.sendMessage(LangVar.msg_ydnhm.getVar() + playerMana.getCurrentMana());
                return;
            }
            for(int i = 0; i < 2; i++) {
                setBlockLoc = new Location(location.getWorld(), location.getX(), location.getY() + i, location.getZ());
                blockStates[i] = setBlockLoc.getBlock().getState();
                if(blockStates[i].getType().equals(Material.AIR)) {
                    setBlockLoc.getBlock().setType(Material.WEB);
                    setBlockLoc.getWorld().spawnParticle(Particle.END_ROD, setBlockLoc, 10, 0,0,0, 0.2);
                }else {
                    blockStates[i] = null;
                }
            }
            if(blockStates[0] == null && blockStates[1] == null)return;
            playerMana.getCdSystem().CDSet(CDSystem.Scrolls.SPIDERWEB, 0);
            if(!playerMana.getCdSystem().CDStat(scroll, false))return;
            itemConsumer(player, (ItemStack) event.getEntity().getMetadata("magickscrolls").get(0).value());
            CleanUpTask cleanUpTask = new CleanUpTask();
            cleanUpTask.sWeb(location, blockStates, playerMana);
        }
    }
}
