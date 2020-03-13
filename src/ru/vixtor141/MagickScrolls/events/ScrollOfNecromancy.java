package ru.vixtor141.MagickScrolls.events;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;

import org.bukkit.craftbukkit.v1_12_R1.entity.*;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.LazyMetadataValue;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.tasks.CleanUpTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

public class ScrollOfNecromancy implements Listener,Runnable {

    private Player player;
    private Player target;

    @EventHandler
    public void use(PlayerInteractEvent event) {
        if (event.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getPlayer().getInventory().getItemInMainHand().getType() != Material.PAPER) return;
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if (!item.getItemMeta().hasLore()) return;
        if (!item.getItemMeta().getLore().get(0).equals("Necromancy scroll")) return;

        player = event.getPlayer();
        event.setCancelled(true);
        Optional<Entity> playerEntity = player.getNearbyEntities(10,10,10).parallelStream().filter(e -> e instanceof Player).findFirst();

        if(!playerEntity.isPresent()){
            player.sendMessage("Target not defined");
            return;
        }

        target = (Player) playerEntity.get();
        Mana playerMana = Mana.getPlayerMap().get(player);
        if(!playerMana.getCdSystem().CDStat(CDSystem.Scrolls.NECROMANCY, playerMana, 50, 120))return;

        Bukkit.getScheduler().runTask(Main.getPlugin(), this);

        if(!player.getGameMode().equals(GameMode.CREATIVE)) {
            item.setAmount(item.getAmount() - 1);
            event.getPlayer().getInventory().setItemInMainHand(item);
        }
    }

    @EventHandler
    public void targetCorrect(EntityTargetLivingEntityEvent event){
        if (event.getEntity().hasMetadata("magicscrolls")){
            event.setCancelled(true);
        }
    }

    @Override
    public void run() {
        Location location = player.getLocation();
        List<LivingEntity> Mobs = new ArrayList<>();
        LazyMetadataValue metadataValue = new LazyMetadataValue(Main.getPlugin(), new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        });

        for(int i = 0; i < 3; i++) {
            CraftZombie craftZombie = (CraftZombie) player.getWorld().spawnEntity(new Location(location.getWorld(), location.getX() - 2, location.getY(), location.getZ() + i - 1), EntityType.ZOMBIE);
            craftZombie.setCustomName(player.getName() + "'s Evil Spirit");
            craftZombie.setCustomNameVisible(true);
            craftZombie.getHandle().setGoalTarget((EntityLiving) ((CraftPlayer) target).getHandle(), EntityTargetEvent.TargetReason.CUSTOM, true);
            craftZombie.setMetadata( "magicscrolls", metadataValue);
            Mobs.add(craftZombie);

            CraftSkeleton craftSkeleton = (CraftSkeleton) player.getWorld().spawnEntity(new Location(location.getWorld(), location.getX() + 2, location.getY(), location.getZ() + i - 1), EntityType.SKELETON);
            craftSkeleton.setCustomName(player.getName() + "'s Evil Spirit");
            craftSkeleton.setCustomNameVisible(true);
            craftSkeleton.getHandle().setGoalTarget((EntityLiving) ((CraftPlayer) target).getHandle(), EntityTargetEvent.TargetReason.CUSTOM, true);
            craftSkeleton.setMetadata( "magicscrolls", metadataValue);
            Mobs.add(craftSkeleton);

            CraftSpider craftSpider = (CraftSpider) player.getWorld().spawnEntity(new Location(location.getWorld(), location.getX() + i - 1, location.getY(), location.getZ() + 2), EntityType.SPIDER);
            craftSpider.setCustomName(player.getName() + "'s Evil Spirit");
            craftSpider.setCustomNameVisible(true);
            craftSpider.getHandle().setGoalTarget((EntityLiving) ((CraftPlayer) target).getHandle(), EntityTargetEvent.TargetReason.CUSTOM, true);
            craftSpider.setMetadata( "magicscrolls", metadataValue);
            Mobs.add(craftSpider);

            CraftWitherSkeleton craftWitherSkeleton = (CraftWitherSkeleton) player.getWorld().spawnEntity(new Location(location.getWorld(), location.getX() + i - 1, location.getY(), location.getZ() - 2), EntityType.WITHER_SKELETON);
            craftWitherSkeleton.setCustomName(player.getName() + "'s Evil Spirit");
            craftWitherSkeleton.setCustomNameVisible(true);
            craftWitherSkeleton.getHandle().setGoalTarget((EntityLiving) ((CraftPlayer) target).getHandle(), EntityTargetEvent.TargetReason.CUSTOM, true);
            craftWitherSkeleton.setMetadata( "magicscrolls", metadataValue);
            Mobs.add(craftWitherSkeleton);
        }

        CleanUpTask cleanUpTask = new CleanUpTask();
        cleanUpTask.mob(Mobs);
    }
}
