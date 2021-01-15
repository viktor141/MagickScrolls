package ru.vixtor141.MagickScrolls.events;

import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.bosses.Imp;
import ru.vixtor141.MagickScrolls.chatPlay.ImpSay;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;
import ru.vixtor141.MagickScrolls.lang.LangVar;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ImpEvent implements Listener {

    @EventHandler
    public void damage(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof Player))return;
        if(event.getEntity().hasMetadata("magickScrolls_IMP")) {
            event.setCancelled(true);
            Player player = (Player) event.getDamager();
            if (!player.getUniqueId().equals(((Player) event.getEntity().getMetadata("magickScrolls_IMP").get(0).value()).getUniqueId())) {
                player.sendMessage(new ImpSay(LangVar.msg_ingtwtoyiweywcm.getVar()).changeMessage());
                return;
            }
            List<String> messagesToPlayer = Main.getPlugin().getLangCF().getStringList("imp.fight");
            String message = messagesToPlayer.get(new Random().nextInt(messagesToPlayer.size()));
            player.sendMessage(new ImpSay(message).changeMessage());
            Location location = event.getEntity().getLocation();
            event.getEntity().remove();
            new Imp(player, location, event.getEntity().getCustomName());
        }else if(event.getEntity().hasMetadata("magickScrolls_IMP_angry")){
            Imp imp = (Imp) event.getEntity().getMetadata("magickScrolls_IMP_angry").get(0).value();
            imp.startSkillPerDamage(event);
        }
    }

    @EventHandler
    public void dead(EntityDeathEvent event){
        if(!event.getEntity().hasMetadata("magickScrolls_IMP_angry"))return;
        Imp imp =(Imp) event.getEntity().getMetadata("magickScrolls_IMP_angry").get(0).value();
        imp.dead();
        if(new Random().nextInt(100) < Main.getPlugin().getConfig().getInt(imp.getBoss().name() + ".drop.chance")){
            Player player = event.getEntity().getKiller();
            if(player == null)return;
            HashMap<Integer, ItemStack> item = player.getInventory().addItem(ACCrafts.ItemsCauldronCrafts.RESEARCH_BOOK.craftCauldronGetItem());
            if(!item.isEmpty()){
                for(ItemStack itemStack: item.values()){
                    player.getWorld().dropItem(player.getLocation(), itemStack);
                }
            }
            player.sendMessage(new ImpSay(LangVar.msg_tyaltbofkgyftpas.getVar()).changeMessage());
        }
    }
}
