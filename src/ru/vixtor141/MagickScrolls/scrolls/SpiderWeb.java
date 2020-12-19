package ru.vixtor141.MagickScrolls.scrolls;

import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.LazyMetadataValue;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.interfaces.Scroll;


public class SpiderWeb implements Scroll {

    private final CDSystem.Scrolls scroll = CDSystem.Scrolls.SPIDERWEB;
    private final ItemStack item;

    public SpiderWeb(Player player, ItemStack item){
        this.item = item;
        if(!player.hasMetadata("MagickScrollsMana")){
            player.sendMessage(ChatColor.RED + "WARNING!!! Player: " + player.getDisplayName() + " lost a plugin meta.");
            return;
        }
        Mana playerMana =(Mana) player.getMetadata("MagickScrollsMana").get(0).value();
        if(!playerMana.getCdSystem().CDStat(scroll, ".perThrowConsumedMana", ".perThrowCDseconds", true))return;

        Snowball snowball = player.launchProjectile(Snowball.class);
        snowball.setMetadata("magickscrolls", new LazyMetadataValue(Main.getPlugin(), this::getItem));
    }

    private ItemStack getItem(){
        return item;
    }
}
