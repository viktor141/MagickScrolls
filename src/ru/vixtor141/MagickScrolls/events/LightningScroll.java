package ru.vixtor141.MagickScrolls.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.Crafts;
import ru.vixtor141.MagickScrolls.Misc.LightningScrollThread;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.checkScrollEvent;

public class LightningScroll implements Listener {

    @EventHandler
    public void use(PlayerInteractEvent event){
        if(checkScrollEvent(event)){
            return;
        }
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

        Player player = event.getPlayer();

        LightningScrollThread lightningScrollThread = null;

        switch (checkTypeOfScroll(item)){

            case 1: lightningScrollThread = new LightningScrollThread(event, player, 5, 1, item);
            break;

            case 2: lightningScrollThread = new LightningScrollThread(event, player, 8, 4, item);
            break;

            case 3: lightningScrollThread = new LightningScrollThread(event, player, 10, 8, item);
            break;

            case 0: return;
        }
        if(lightningScrollThread != null) lightningScrollThread.start();
    }

    private int checkTypeOfScroll(ItemStack item){
        if(Crafts.ScrollsCrafts.LIGHTNINGONE.craftScroll(false).getItemMeta().getLore().equals(item.getItemMeta().getLore())){
            return 1;
        }else if(Crafts.ScrollsCrafts.LIGHTNINGTWO.craftScroll(false).getItemMeta().getLore().equals(item.getItemMeta().getLore())){
            return 2;
        }else if(Crafts.ScrollsCrafts.LIGHTNINGTHREE.craftScroll(false).getItemMeta().getLore().equals(item.getItemMeta().getLore())){
            return 3;
        }else {
            return 0;
        }
    }
}
