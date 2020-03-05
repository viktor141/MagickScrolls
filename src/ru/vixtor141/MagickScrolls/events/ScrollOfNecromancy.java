package ru.vixtor141.MagickScrolls.events;

import net.minecraft.server.v1_12_R1.*;
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
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.tasks.CleanUpTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ScrollOfNecromancy implements Listener {

    @EventHandler
    public void use(PlayerInteractEvent event) {
        if (event.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getPlayer().getInventory().getItemInMainHand().getType() != Material.PAPER) return;
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if (!item.getItemMeta().hasLore()) return;
        if (!item.getItemMeta().getLore().get(0).equals("Necromancy scroll")) return;

        Player player = event.getPlayer();
        Optional<Entity> playerEntity = player.getNearbyEntities(10,10,10).stream().filter(e -> e instanceof Player).findFirst();

        if(!playerEntity.isPresent()){
            player.sendMessage("Target not defined");
            return;
        }

        Player target = (Player) playerEntity.get();
        Mana playerMana = Mana.getPlayerMap().get(player);

        if (!playerMana.consumeMana(50)) return;

        spawnMinions(player, target);

        if(!player.getGameMode().equals(GameMode.CREATIVE)) {
            item.setAmount(item.getAmount() - 1);
            event.getPlayer().getInventory().setItemInMainHand(item);
        }
    }

    @EventHandler
    public void targetCorrect(EntityTargetLivingEntityEvent event){
        if (event.getEntity().getName().equals("huy")){
            event.setCancelled(true);
        }
    }

    private void spawnMinions(Player player, Player target){
        Location location = player.getLocation();
        List<LivingEntity> Mobs = new ArrayList<>();

        for(int i = 0; i < 3; i++) {
            CraftZombie craftZombie = (CraftZombie) player.getWorld().spawnEntity(new Location(location.getWorld(), location.getX() - 2, location.getY(), location.getZ() + i - 1), EntityType.ZOMBIE);
            craftZombie.setCustomName(player.getName() + "'s Evil Spirit");
            craftZombie.setCustomNameVisible(true);
            craftZombie.getHandle().setGoalTarget((EntityLiving) ((CraftPlayer) target).getHandle(), EntityTargetEvent.TargetReason.CUSTOM, true);
            Mobs.add(craftZombie);

            CraftSkeleton craftSkeleton = (CraftSkeleton) player.getWorld().spawnEntity(new Location(location.getWorld(), location.getX() + 2, location.getY(), location.getZ() + i - 1), EntityType.SKELETON);
            craftSkeleton.setCustomName(player.getName() + "'s Evil Spirit");
            craftSkeleton.setCustomNameVisible(true);
            craftSkeleton.getHandle().setGoalTarget((EntityLiving) ((CraftPlayer) target).getHandle(), EntityTargetEvent.TargetReason.CUSTOM, true);
            Mobs.add(craftSkeleton);

            CraftSpider craftSpider = (CraftSpider) player.getWorld().spawnEntity(new Location(location.getWorld(), location.getX() + i - 1, location.getY(), location.getZ() + 2), EntityType.SPIDER);
            craftSpider.setCustomName(player.getName() + "'s Evil Spirit");
            craftSpider.setCustomNameVisible(true);
            craftSpider.getHandle().setGoalTarget((EntityLiving) ((CraftPlayer) target).getHandle(), EntityTargetEvent.TargetReason.CUSTOM, true);
            Mobs.add(craftSpider);

            CraftWitherSkeleton craftWitherSkeleton = (CraftWitherSkeleton) player.getWorld().spawnEntity(new Location(location.getWorld(), location.getX() + i - 1, location.getY(), location.getZ() - 2), EntityType.WITHER_SKELETON);
            craftWitherSkeleton.setCustomName(player.getName() + "'s Evil Spirit");
            craftWitherSkeleton.setCustomNameVisible(true);
            craftWitherSkeleton.getHandle().setGoalTarget((EntityLiving) ((CraftPlayer) target).getHandle(), EntityTargetEvent.TargetReason.CUSTOM, true);
            Mobs.add(craftWitherSkeleton);
        }

        CleanUpTask cleanUpTask = new CleanUpTask();
        cleanUpTask.mob(Mobs);
    }
}
