package ru.vixtor141.MagickScrolls.scrolls;

import org.bukkit.*;
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

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.getPlayerMana;
import static ru.vixtor141.MagickScrolls.Misc.CheckUp.itemConsumer;

public class Necromancy implements Scroll, Runnable {

    private final Player player;
    private final ItemStack item;
    private final Main plugin = Main.getPlugin();
    private final List<LivingEntity> mobs = new ArrayList<>();
    private LazyMetadataValue metadataValue;
    private String mobNames;
    private Player target;

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

        target = (Player) playerEntity.get();
        Mana playerMana = getPlayerMana(player);
        CDSystem.Scrolls scroll = CDSystem.Scrolls.NECROMANCY;

        if(!playerMana.getCdSystem().CDStat(scroll , true))return;

        Location location = player.getLocation();

        metadataValue = new LazyMetadataValue(Main.getPlugin(), player::getName);
        mobNames = LangVar.name_nec_scroll_mobs.getVar() + player.getName();

        for(int i = 0; i < 3; i++) {
            spawnMob(EntityType.ZOMBIE, location.clone().add(-2, 0, i - 1));

            spawnMob(EntityType.SKELETON, location.clone().add(2, 0, i - 1));

            spawnMob(EntityType.SPIDER, location.clone().add(i - 1, 0, 2));

            spawnMob(EntityType.WITHER_SKELETON, location.clone().add(i-1, 0, -2));
        }

        itemConsumer(player, item);

        playerMana.getExistMobs().addAll(mobs);
        CleanUpTask cleanUpTask = new CleanUpTask();
        cleanUpTask.mob(mobs, playerMana);
    }

    private void spawnMob(EntityType type, Location spawnLocation){
        Creature creature = (Creature) player.getWorld().spawnEntity(spawnLocation, type);
        creature.setCustomName(mobNames);
        creature.setCustomNameVisible(true);
        creature.setTarget(target);
        creature.setMetadata( "magicscrolls", metadataValue);
        mobs.add(creature);

        spawnLocation.getWorld().spawnParticle(Particle.SMOKE_LARGE, spawnLocation.clone().add(0,1.5,0), 5,1,1,1, 0.1);
    }
}
