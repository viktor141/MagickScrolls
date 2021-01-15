package ru.vixtor141.MagickScrolls.scrolls;

import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.LazyMetadataValue;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.interfaces.Scroll;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.getPlayerMana;


public class SpiderWeb implements Scroll {

    private final CDSystem.Scrolls scroll = CDSystem.Scrolls.SPIDERWEB;
    private final ItemStack item;

    public SpiderWeb(Player player, ItemStack item){
        this.item = item;
        Mana playerMana = getPlayerMana(player);
        if(!playerMana.getCdSystem().CDStat(scroll, ".perThrowConsumedMana", ".perThrowCDseconds", true))return;

        Snowball snowball = player.launchProjectile(Snowball.class);
        snowball.setMetadata("magickscrolls", new LazyMetadataValue(Main.getPlugin(), this::getItem));
    }

    private ItemStack getItem(){
        return item;
    }
}
