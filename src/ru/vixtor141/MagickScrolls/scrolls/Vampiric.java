package ru.vixtor141.MagickScrolls.scrolls;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.interfaces.Scroll;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.itemConsumer;

public class Vampiric implements Scroll {

    public Vampiric(Player player, ItemStack item, EntityDamageByEntityEvent event){
        Mana playerMana =  Main.getPlugin().getPlayerMap().get(player);

        if(player.getHealth() == player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())return;
        CDSystem.Scrolls scroll = CDSystem.Scrolls.VAMPIRIC;

        if(!playerMana.getCdSystem().CDStat(scroll, false))return;

        event.setDamage(Main.getPlugin().getConfig().getDouble(scroll.name() + ".damge"));

        if(player.getHealth() <= player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() - 1.5) {
            player.setHealth(player.getHealth() + 1.5);
        }else{
            player.setHealth(player.getHealthScale());
        }

        itemConsumer(player, item);

    }
}
