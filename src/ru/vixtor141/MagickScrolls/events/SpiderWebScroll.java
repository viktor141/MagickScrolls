package ru.vixtor141.MagickScrolls.events;

import org.bukkit.Art;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.LazyMetadataValue;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.Misc.CheckUp;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;
import ru.vixtor141.MagickScrolls.tasks.CleanUpTask;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.checkScrollEvent;

public class SpiderWebScroll implements Listener {

    private final Main plugin = Main.getPlugin();
    private final CDSystem.Scrolls scroll = CDSystem.Scrolls.SPIDERWEB;

    @EventHandler
    public void use(PlayerInteractEvent event){
        if(checkScrollEvent(event))return;
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if(!CheckUp.checkItemLore(ACCrafts.CraftsOfScrolls.SPIDERWEB, item)) return;

        Player player = event.getPlayer();

        Mana playerMana = plugin.getPlayerMap().get(player);
        if(!playerMana.getCdSystem().CDStat(scroll, playerMana, ".perThrowConsumedMana", ".perThrowCDseconds", true))return;

        Snowball snowball = player.launchProjectile(Snowball.class);
        snowball.setMetadata("magickscrolls", new LazyMetadataValue(Main.getPlugin(), player::getName));

    }


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
                player.sendMessage(plugin.getReadingLangFile().getMsg("msg_ydnhm") + playerMana.getCurrentMana());
                return;
            }
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
            if(!playerMana.getCdSystem().CDStat(scroll, playerMana, ".consumedMana", ".CDseconds", false))return;
            if(!player.getGameMode().equals(GameMode.CREATIVE)) {
                ItemStack item = player.getInventory().getItemInMainHand();
                item.setAmount(item.getAmount() - 1);
                player.getInventory().setItemInMainHand(item);
            }
            CleanUpTask cleanUpTask = new CleanUpTask();
            cleanUpTask.sWeb(location, blockStates, playerMana);
        }
    }
}
