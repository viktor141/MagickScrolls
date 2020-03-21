package ru.vixtor141.MagickScrolls.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_12_R1.CraftArt;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.LazyMetadataValue;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Crafts;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.tasks.CleanUpTask;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.checkScrollEvent;

public class SpiderWebScroll implements Listener {


    @EventHandler
    public void use(PlayerInteractEvent event){
        if(checkScrollEvent(event))return;
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if(!Crafts.ScrollsCrafts.SPIDERWEB.craftScroll(false).getItemMeta().getLore().equals(item.getItemMeta().getLore())) return;

        Player player = event.getPlayer();

        Mana playerMana = Mana.getPlayerMap().get(player);
        if(!playerMana.getCdSystem().CDStat(CDSystem.Scrolls.SPIDERWEB, playerMana, 1, 4))return;

        Snowball snowball = player.launchProjectile(Snowball.class);
        snowball.setMetadata("magickscrolls", new LazyMetadataValue(Main.getPlugin(), player::getName));

    }


    @EventHandler
    public void hitEvent(ProjectileHitEvent event){
        if(event.getHitBlock() != null)return;
        if(event.getEntity().hasMetadata("magickscrolls")){
            Entity entity = event.getHitEntity();
            if(entity instanceof ItemFrame || entity instanceof CraftArt)return;
            if(!(event.getEntity().getShooter() instanceof Player))return;
            Player player =(Player) event.getEntity().getShooter();
            Mana playerMana = Mana.getPlayerMap().get(player);
            BlockState[] blockStates = new BlockState[2];
            Location location = event.getHitEntity().getLocation();
            Location setBlockLoc;
            for(int i = 0; i < 2; i++) {
                setBlockLoc = new Location(location.getWorld(), location.getX(), location.getY() + i, location.getZ());
                blockStates[i] = setBlockLoc.getBlock().getState();
                if(blockStates[i].getType().equals(Material.AIR)) {
                    setBlockLoc.getBlock().setType(Material.WEB);
                }else {
                    blockStates[i] = null;
                }
            }
            if(blockStates[0] == null && blockStates[1] == null)return;
            playerMana.getCdSystem().CDSet(CDSystem.Scrolls.SPIDERWEB, 0);
            if(!playerMana.getCdSystem().CDStat(CDSystem.Scrolls.SPIDERWEB, playerMana, 7, 20))return;
            CleanUpTask cleanUpTask = new CleanUpTask();
            cleanUpTask.sWeb(location, blockStates);
        }
    }
}
