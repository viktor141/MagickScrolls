package ru.vixtor141.MagickScrolls.events;

import net.minecraft.server.v1_12_R1.EntityItem;
import net.minecraft.server.v1_12_R1.ItemEnderEye;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_12_R1.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Crafts;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static ru.vixtor141.MagickScrolls.Main.readingLangFile;
import static ru.vixtor141.MagickScrolls.Misc.CheckUp.checkScrollEvent;

public class LightningScroll implements Listener, Runnable {

    private Player player;
    private PlayerInteractEvent event;
    private double bound;
    private int numberOfEntities;
    private ItemStack item;
    private List<LivingEntity> entitesInLocation;

    Mana playerMana;
    @EventHandler
    public void use(PlayerInteractEvent event){
        if(checkScrollEvent(event))return;
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

        player = event.getPlayer();

        this.event = event;
        this.item = item;

        switch (checkTypeOfScroll(item)){

            case 1: bound = 5;
            numberOfEntities = 1;
            break;

            case 2: bound = 8;
                numberOfEntities = 4;
            break;

            case 3: bound = 10;
                numberOfEntities = 8;
            break;

            case 0: return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), this);

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

    @Override
    public void run() {
        event.setCancelled(true);

        entitesInLocation = (List<LivingEntity>)(List<?>) player.getNearbyEntities(bound,bound,bound).parallelStream().filter(entity -> (entity instanceof LivingEntity) && !(entity instanceof CraftArmorStand)).collect(Collectors.toList());

        entitesInLocation = entitesInLocation.parallelStream().filter(livingEntity -> !livingEntity.hasPotionEffect(PotionEffectType.INVISIBILITY)).collect(Collectors.toList());

        this.playerMana = Mana.getPlayerMap().get(player);
        if (entitesInLocation.isEmpty()) {
            player.sendMessage(ChatColor.YELLOW + readingLangFile.msg_nmay);
            return;
        }
        Bukkit.getScheduler().runTask(Main.getPlugin(), this::end);

    }


    private void end(){
        Entity entity;

        CDSystem.Scrolls scroll = CDSystem.Scrolls.LIGHTNING;
        Main plugin = Main.getPlugin();

        if(!playerMana.getCdSystem().CDStat(scroll, playerMana, numberOfEntities * plugin.getConfig().getDouble(scroll.name() + ".consumedMana"), (int) (plugin.getConfig().getDouble(scroll.name() + ".CDseconds") + 30 * Math.log10(numberOfEntities)), true))return;

        for(int i = 0; i < entitesInLocation.size(); i++) {

            entity = entitesInLocation.get(i);
            entity.getLocation().getWorld().strikeLightning(entity.getLocation()).setSilent(true);
            if(i == numberOfEntities-1){
                if(!player.getGameMode().equals(GameMode.CREATIVE)) {
                    item.setAmount(item.getAmount() - 1);
                    event.getPlayer().getInventory().setItemInMainHand(item);
                }
                return;
            }
        }
    }
}
