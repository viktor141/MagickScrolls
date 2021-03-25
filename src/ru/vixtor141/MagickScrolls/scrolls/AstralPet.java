package ru.vixtor141.MagickScrolls.scrolls;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;
import ru.vixtor141.MagickScrolls.interfaces.Scroll;

import java.util.List;
import java.util.stream.Collectors;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.getPlayerMana;
import static ru.vixtor141.MagickScrolls.Misc.CheckUp.itemConsumer;

public class AstralPet implements Scroll, Runnable {

    private final Player player;
    private int count = 0;
    private final int high = 6;
    private BukkitTask liveTask, damageTask;
    private boolean damageCheck = false;
    private final Main plugin = Main.getPlugin();
    private final double damage = plugin.getConfig().getDouble(ACCrafts.CraftsOfScrolls.ASTRAL_PET.name() + ".damage");
    private final int countMax = plugin.getConfig().getInt(ACCrafts.CraftsOfScrolls.ASTRAL_PET.name() + ".secondsLive") * 4;

    public AstralPet (Player player, ItemStack item){
        this.player = player;
        Mana playerMana = getPlayerMana(player);
        if(!playerMana.getCdSystem().CDStat(CDSystem.Scrolls.ASTRAL_PET , true))return;

        liveTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getPlugin(), this::liveTask, 0, 5);
        damageTask = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(), this::damageTask, 0, 20);

        itemConsumer(player, item);
    }

    private void liveTask() {
        if(count >= countMax){
            damageCheck = true;
            liveTask.cancel();
        }
        player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation().add(0, high,0), 20, 0.4,0.4,0.4, 0.0001);
        player.getWorld().spawnParticle(Particle.FLAME, player.getLocation().add(0, high,0), 5, 0,0,0, 0.05);
        player.getWorld().spawnParticle(Particle.DRAGON_BREATH, player.getLocation().add(0, high,0), 5, 0,0,0, 0.05);
        count++;
    }

    private void damageTask(){
        if(damageCheck)damageTask.cancel();
        List<Entity> entityList = player.getNearbyEntities(8,8,8).stream().filter(entity1 -> entity1 instanceof LivingEntity && !(entity1 instanceof ArmorStand)).collect(Collectors.toList());
        if(entityList.isEmpty())return;

        LivingEntity entity = null;

        for(Entity entityCheck : entityList){
            if(!entityCheck.isDead()){
                entity = (LivingEntity) entityCheck;
                break;
            }
        }

        if(entity == null)return;

        final Location toEntity = entity.getEyeLocation();

        Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), () -> {
            Location start = player.getLocation().clone().add(0, high,0);
            Vector dir = toEntity.clone().subtract(start).toVector();
            int amountOfSteps = 10;
            float distance = (float) start.distance(toEntity),  step = distance/amountOfSteps;
            for(float i = step; i < distance; i+= step) {
                dir.normalize();
                dir.multiply(i);
                start.add(dir);
                player.getWorld().spawnParticle(Particle.SPELL_WITCH , start, 1);
                start.subtract(dir);
            }
        });

        entity.damage(damage, player);
    }

    @Override
    public void run() {

    }
}
