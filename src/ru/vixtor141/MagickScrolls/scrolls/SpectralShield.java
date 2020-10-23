package ru.vixtor141.MagickScrolls.scrolls;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.interfaces.Scroll;
import ru.vixtor141.MagickScrolls.lang.LangVar;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.itemConsumer;


public class SpectralShield implements Scroll {

    public SpectralShield(Player player, ItemStack item){
        Mana playerMana = Main.getPlugin().getPlayerMap().get(player);
        if(playerMana.getSpectralShieldSeconds() != 0){
            player.sendMessage(LangVar.msg_aa.getVar());
            return;
        }
        if(!playerMana.getCdSystem().CDStat(CDSystem.Scrolls.SPECTRAL_SHIELD ,true))return;

        itemConsumer(player, item);
        playerMana.setSpectralShieldSeconds(Main.getPlugin().getConfig().getInt(CDSystem.Scrolls.SPECTRAL_SHIELD.name() + ".seconds"));
    }
}
