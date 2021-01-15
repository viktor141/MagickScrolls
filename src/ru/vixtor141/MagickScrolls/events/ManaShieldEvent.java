package ru.vixtor141.MagickScrolls.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;
import ru.vixtor141.MagickScrolls.levels.ManaShieldLevel;
import ru.vixtor141.MagickScrolls.levels.ShieldManaLevels;

import java.util.List;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.getPlayerMana;

public class ManaShieldEvent implements Listener {

    @EventHandler
    public void damage(EntityDamageByEntityEvent event){
        if(!(event.getEntity() instanceof Player))return;
        Player player =(Player) event.getEntity();
        if(!player.isBlocking())return;

        ItemStack itemStack = player.getInventory().getItemInOffHand();
        if(!itemStack.getType().equals(Material.SHIELD))return;
        if(!itemStack.getItemMeta().hasLore() || !(itemStack.getItemMeta().getLore().size() >= 2)) return;
        List<String> lore = itemStack.getItemMeta().getLore();
        if(!lore.get(lore.size() - 2).substring(Main.getPlugin().getSubStr()).equals(ACCrafts.Artifacts.MANA_SHIELD.name()))return;

        Mana playerMana = getPlayerMana(player);
        ShieldManaLevels shieldManaLevels = playerMana.getPlayerResearch().getShieldManaLevels();
        shieldManaLevels.update(1);

        ManaShieldLevel manaShieldLevel = shieldManaLevels.getManaShieldLevel();

        playerMana.addMana(event.getDamage() * manaShieldLevel.getDamageAndMana(ManaShieldLevel.TypeDamageOrMana.MANA));
        player.damage(event.getDamage() * manaShieldLevel.getDamageAndMana(ManaShieldLevel.TypeDamageOrMana.DAMAGE), event.getDamager());

        player.sendMessage(event.getDamage() +" " + event.getFinalDamage());
        player.sendMessage(manaShieldLevel.getDamageAndMana(ManaShieldLevel.TypeDamageOrMana.DAMAGE) +" " + event.getDamage() * manaShieldLevel.getDamageAndMana(ManaShieldLevel.TypeDamageOrMana.DAMAGE));
    }
}
