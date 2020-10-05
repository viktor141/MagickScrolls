package ru.vixtor141.MagickScrolls.scrolls;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.LazyMetadataValue;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.interfaces.Scroll;
import ru.vixtor141.MagickScrolls.lang.LangVar;
import ru.vixtor141.MagickScrolls.tasks.CleanUpTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.itemConsumer;

public class Necromancy implements Scroll, Runnable {

    private final Player player;
    private final ItemStack item;
    private final Main plugin = Main.getPlugin();

    public Necromancy(Player player, ItemStack item){
        this.player = player;
        this.item = item;

        Bukkit.getScheduler().runTask(Main.getPlugin(), this);
    }

    @Override
    public void run() {
        Optional<Entity> playerEntity = player.getNearbyEntities(10,10,10).parallelStream().filter(e -> e instanceof Player).findFirst();

        if(!playerEntity.isPresent()){
            player.sendMessage(ChatColor.YELLOW + LangVar.msg_tind.getVar());
            return;
        }

        Player target = (Player) playerEntity.get();
        Mana playerMana = plugin.getPlayerMap().get(player);
        CDSystem.Scrolls scroll = CDSystem.Scrolls.NECROMANCY;


        if(!playerMana.getCdSystem().CDStat(scroll , ".consumedMana", ".CDseconds", true))return;

        Location location = player.getLocation();
        List<LivingEntity> mobs = new ArrayList<>();
        LazyMetadataValue metadataValue = new LazyMetadataValue(Main.getPlugin(), () -> player.getName());
        String mobNames = LangVar.name_nec_scroll_mobs.getVar() + player.getName();



        for(int i = 0; i < 3; i++) {
            Creature craftZombie = (Creature) player.getWorld().spawnEntity(new Location(location.getWorld(), location.getX() - 2, location.getY(), location.getZ() + i - 1), EntityType.ZOMBIE);
            craftZombie.setCustomName(mobNames);
            craftZombie.setCustomNameVisible(true);
            craftZombie.setTarget(target);
            craftZombie.setMetadata( "magicscrolls", metadataValue);
            mobs.add(craftZombie);

            Creature craftSkeleton = (Creature) player.getWorld().spawnEntity(new Location(location.getWorld(), location.getX() + 2, location.getY(), location.getZ() + i - 1), EntityType.SKELETON);
            craftSkeleton.setCustomName(mobNames);
            craftSkeleton.setCustomNameVisible(true);
            craftSkeleton.setTarget(target);
            craftSkeleton.setMetadata( "magicscrolls", metadataValue);
            mobs.add(craftSkeleton);

            Creature craftSpider = (Creature) player.getWorld().spawnEntity(new Location(location.getWorld(), location.getX() + i - 1, location.getY(), location.getZ() + 2), EntityType.SPIDER);
            craftSpider.setCustomName(mobNames);
            craftSpider.setCustomNameVisible(true);
            craftSpider.setTarget(target);
            craftSpider.setMetadata( "magicscrolls", metadataValue);
            mobs.add(craftSpider);

            Creature craftWitherSkeleton = (Creature) player.getWorld().spawnEntity(new Location(location.getWorld(), location.getX() + i - 1, location.getY(), location.getZ() - 2), EntityType.WITHER_SKELETON);
            craftWitherSkeleton.setCustomName(mobNames);
            craftWitherSkeleton.setCustomNameVisible(true);
            craftWitherSkeleton.setTarget(target);
            craftWitherSkeleton.setMetadata( "magicscrolls", metadataValue);
            mobs.add(craftWitherSkeleton);
        }

        itemConsumer(player, item);

        playerMana.getExistMobs().addAll(mobs);
        CleanUpTask cleanUpTask = new CleanUpTask();
        cleanUpTask.mob(mobs, playerMana);
    }
}
