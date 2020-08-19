package ru.vixtor141.MagickScrolls.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.Misc.CheckUp;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;
import ru.vixtor141.MagickScrolls.tasks.AstralPetTask;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.checkScrollEvent;

public class AstralPetScroll implements Listener {

    @EventHandler
    public void use(PlayerInteractEvent event){
        if(checkScrollEvent(event))return;
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if(!CheckUp.checkItemLore(ACCrafts.CraftsOfScrolls.ASTRAL_PET, item)) return;

        Player player = event.getPlayer();

        Mana playerMana = Main.getPlugin().getPlayerMap().get(player);
        if(!playerMana.getCdSystem().CDStat(CDSystem.Scrolls.ASTRAL_PET ,".consumedMana", ".CDseconds", true))return;

        AstralPetTask astralPetTask = new AstralPetTask(player);
    }

}
