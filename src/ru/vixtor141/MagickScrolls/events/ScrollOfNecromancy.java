package ru.vixtor141.MagickScrolls.events;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;

import org.bukkit.craftbukkit.v1_12_R1.entity.*;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.LazyMetadataValue;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Crafts;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.tasks.CleanUpTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.vixtor141.MagickScrolls.Main.readingLangFile;
import static ru.vixtor141.MagickScrolls.Misc.CheckUp.checkScrollEvent;

public class ScrollOfNecromancy implements Listener,Runnable {

    private Player player;
    private ItemStack item;

    @EventHandler
    public void use(PlayerInteractEvent event) {
        if(checkScrollEvent(event))return;
        item = event.getPlayer().getInventory().getItemInMainHand();
        if(!Crafts.ScrollsCrafts.NECROMANCY.craftScroll(false).getItemMeta().getLore().equals(item.getItemMeta().getLore())) return;

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
        }
    }

    @EventHandler
    public void mobKillWhenOwnerDie(EntityDeathEvent event){
        if(event.getEntity() instanceof Player){
            Mana playerMana = Mana.getPlayerMap().get(event.getEntity());
            playerMana.getExistMobs().parallelStream().forEach(Entity::remove);
            playerMana.getExistMobs().clear();
        }
    }

    @Override
    public void run() {
        Optional<Entity> playerEntity = player.getNearbyEntities(10,10,10).parallelStream().filter(e -> e instanceof Player).findFirst();

        if(!playerEntity.isPresent()){
            player.sendMessage(ChatColor.YELLOW + readingLangFile.msg_tind);
            return;
        }

        Player target = (Player) playerEntity.get();
        Mana playerMana = Mana.getPlayerMap().get(player);
        if(!playerMana.getCdSystem().CDStat(CDSystem.Scrolls.NECROMANCY, playerMana, 50, 120))return;

        Location location = player.getLocation();
        List<LivingEntity> mobs = new ArrayList<>();
        LazyMetadataValue metadataValue = new LazyMetadataValue(Main.getPlugin(), () -> player.getName());

        for(int i = 0; i < 3; i++) {
            CraftZombie craftZombie = (CraftZombie) player.getWorld().spawnEntity(new Location(location.getWorld(), location.getX() - 2, location.getY(), location.getZ() + i - 1), EntityType.ZOMBIE);
            craftZombie.setCustomName(readingLangFile.name_nec_scroll_mobs + player.getName());
            craftZombie.setCustomNameVisible(true);
            craftZombie.getHandle().setGoalTarget((EntityLiving) ((CraftPlayer) target).getHandle(), EntityTargetEvent.TargetReason.CUSTOM, true);
            craftZombie.setMetadata( "magicscrolls", metadataValue);
            mobs.add(craftZombie);

            CraftSkeleton craftSkeleton = (CraftSkeleton) player.getWorld().spawnEntity(new Location(location.getWorld(), location.getX() + 2, location.getY(), location.getZ() + i - 1), EntityType.SKELETON);
            craftSkeleton.setCustomName(readingLangFile.name_nec_scroll_mobs + player.getName());
            craftSkeleton.setCustomNameVisible(true);
            craftSkeleton.getHandle().setGoalTarget((EntityLiving) ((CraftPlayer) target).getHandle(), EntityTargetEvent.TargetReason.CUSTOM, true);
            craftSkeleton.setMetadata( "magicscrolls", metadataValue);
            mobs.add(craftSkeleton);

            CraftSpider craftSpider = (CraftSpider) player.getWorld().spawnEntity(new Location(location.getWorld(), location.getX() + i - 1, location.getY(), location.getZ() + 2), EntityType.SPIDER);
            craftSpider.setCustomName(readingLangFile.name_nec_scroll_mobs + player.getName());
            craftSpider.setCustomNameVisible(true);
            craftSpider.getHandle().setGoalTarget((EntityLiving) ((CraftPlayer) target).getHandle(), EntityTargetEvent.TargetReason.CUSTOM, true);
            craftSpider.setMetadata( "magicscrolls", metadataValue);
            mobs.add(craftSpider);

            CraftWitherSkeleton craftWitherSkeleton = (CraftWitherSkeleton) player.getWorld().spawnEntity(new Location(location.getWorld(), location.getX() + i - 1, location.getY(), location.getZ() - 2), EntityType.WITHER_SKELETON);
            craftWitherSkeleton.setCustomName(readingLangFile.name_nec_scroll_mobs + player.getName());
            craftWitherSkeleton.setCustomNameVisible(true);
            craftWitherSkeleton.getHandle().setGoalTarget((EntityLiving) ((CraftPlayer) target).getHandle(), EntityTargetEvent.TargetReason.CUSTOM, true);
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
