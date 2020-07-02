package ru.vixtor141.MagickScrolls.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;

import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.LazyMetadataValue;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;
import ru.vixtor141.MagickScrolls.tasks.CleanUpTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.checkScrollEvent;

public class ScrollOfNecromancy implements Listener,Runnable {

    private Player player;
    private ItemStack item;
    private final Main plugin = Main.getPlugin();

    @EventHandler
    public void use(PlayerInteractEvent event) {
        if(checkScrollEvent(event))return;
        item = event.getPlayer().getInventory().getItemInMainHand();
        if(!ACCrafts.CraftsOfScrolls.NECROMANCY.craftAltarResult().getItemMeta().getLore().get(1).equals(item.getItemMeta().getLore().get(1))) return;

        player = event.getPlayer();
        event.setCancelled(true);

        Bukkit.getScheduler().runTask(Main.getPlugin(), this);
    }

    @EventHandler
    public void targetCorrect(EntityTargetLivingEntityEvent event){
        if (event.getEntity().hasMetadata("magicscrolls")){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void mobDropRemove(EntityDeathEvent event){
        if (event.getEntity().hasMetadata("magicscrolls")){
            event.getDrops().clear();
            event.setDroppedExp(0);
        }
    }

    @EventHandler
    public void mobKillWhenOwnerDie(EntityDeathEvent event){
        if(event.getEntity() instanceof Player){
            Mana playerMana = plugin.getPlayerMap().get(event.getEntity());
            playerMana.getExistMobs().parallelStream().forEach(Entity::remove);
            playerMana.getExistMobs().clear();
        }
    }

    @Override
    public void run() {
        Optional<Entity> playerEntity = player.getNearbyEntities(10,10,10).parallelStream().filter(e -> e instanceof Player).findFirst();

        if(!playerEntity.isPresent()){
            player.sendMessage(ChatColor.YELLOW + plugin.getReadingLangFile().getMsg("msg_tind"));
            return;
        }

        Player target = (Player) playerEntity.get();
        Mana playerMana = plugin.getPlayerMap().get(player);
        CDSystem.Scrolls scroll = CDSystem.Scrolls.NECROMANCY;


        if(!playerMana.getCdSystem().CDStat(scroll, playerMana, plugin.getConfig().getDouble(scroll.name() + ".consumedMana") , plugin.getConfig().getInt(scroll.name() + ".CDseconds"), true))return;

        Location location = player.getLocation();
        List<LivingEntity> mobs = new ArrayList<>();
        LazyMetadataValue metadataValue = new LazyMetadataValue(Main.getPlugin(), () -> player.getName());



        for(int i = 0; i < 3; i++) {
            Creature craftZombie = (Creature) player.getWorld().spawnEntity(new Location(location.getWorld(), location.getX() - 2, location.getY(), location.getZ() + i - 1), EntityType.ZOMBIE);
            craftZombie.setCustomName(plugin.getReadingLangFile().getMsg("name_nec_scroll_mobs") + player.getName());
            craftZombie.setCustomNameVisible(true);
            craftZombie.setTarget(target);
            craftZombie.setMetadata( "magicscrolls", metadataValue);
            mobs.add(craftZombie);

            Creature craftSkeleton = (Creature) player.getWorld().spawnEntity(new Location(location.getWorld(), location.getX() + 2, location.getY(), location.getZ() + i - 1), EntityType.SKELETON);
            craftSkeleton.setCustomName(plugin.getReadingLangFile().getMsg("name_nec_scroll_mobs") + player.getName());
            craftSkeleton.setCustomNameVisible(true);
            craftSkeleton.setTarget(target);
            craftSkeleton.setMetadata( "magicscrolls", metadataValue);
            mobs.add(craftSkeleton);

            Creature craftSpider = (Creature) player.getWorld().spawnEntity(new Location(location.getWorld(), location.getX() + i - 1, location.getY(), location.getZ() + 2), EntityType.SPIDER);
            craftSpider.setCustomName(plugin.getReadingLangFile().getMsg("name_nec_scroll_mobs") + player.getName());
            craftSpider.setCustomNameVisible(true);
            craftSpider.setTarget(target);
            craftSpider.setMetadata( "magicscrolls", metadataValue);
            mobs.add(craftSpider);

            Creature craftWitherSkeleton = (Creature) player.getWorld().spawnEntity(new Location(location.getWorld(), location.getX() + i - 1, location.getY(), location.getZ() - 2), EntityType.WITHER_SKELETON);
            craftWitherSkeleton.setCustomName(plugin.getReadingLangFile().getMsg("name_nec_scroll_mobs") + player.getName());
            craftWitherSkeleton.setCustomNameVisible(true);
            craftWitherSkeleton.setTarget(target);
            craftWitherSkeleton.setMetadata( "magicscrolls", metadataValue);
            mobs.add(craftWitherSkeleton);
        }

        if(!player.getGameMode().equals(GameMode.CREATIVE)) {
            item.setAmount(item.getAmount() - 1);
            player.getInventory().setItemInMainHand(item);
        }

        playerMana.getExistMobs().addAll(mobs);
        CleanUpTask cleanUpTask = new CleanUpTask();
        cleanUpTask.mob(mobs, playerMana);
    }
}
