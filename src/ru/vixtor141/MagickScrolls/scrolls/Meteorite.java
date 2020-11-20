package ru.vixtor141.MagickScrolls.scrolls;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.effects.CircleEffect;
import ru.vixtor141.MagickScrolls.effects.ShootingStarEffect;
import ru.vixtor141.MagickScrolls.interfaces.Scroll;
import ru.vixtor141.MagickScrolls.lang.LangVar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.*;
import static java.lang.Math.toRadians;
import static ru.vixtor141.MagickScrolls.Misc.CheckUp.itemConsumer;

public class Meteorite implements Scroll, Runnable {

    private final Main plugin = Main.getPlugin();
    private final Player player;
    private final ItemStack item;
    private final int radiusSearchOfPos = 20, radiusOfSpawnMeteor = 50, amountOfSteps = 10, rndSpawn = 2, rndGen = rndSpawn*2;
    private Location meteorEnd;
    private BukkitTask rayV, meteoriteRay, circleEffect;
    private Location startRay;
    private int j=0, d;
    private float k, distance, step;
    private final float r = 5;
    private Vector dirRay;
    private final List<BukkitTask> circleEffects = new ArrayList<>();
    private final Mana playerMana;


    public Meteorite(Player player, ItemStack item){
        this.player = player;
        this.item = item;
        playerMana = plugin.getPlayerMap().get(player);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, this);
    }

    @Override
    public void run() {
        Location start = player.getEyeLocation();
        Vector dir = player.getEyeLocation().getDirection();
        dir.multiply(radiusSearchOfPos);
        for(int i = 1; i < radiusSearchOfPos; i++) {
            dir.normalize();
            dir.multiply(i);
            start.add(dir);

            if(!start.getBlock().getType().equals(Material.AIR) && !start.getBlock().getType().equals(Material.WATER) && !start.getBlock().getType().equals(Material.STATIONARY_WATER)){
                meteorEnd = start.clone();
                if(playerMana.getCdSystem().CDStat(CDSystem.Scrolls.METEORITE, true)){
                    itemConsumer(player, item);
                    meteoriteEffects();
                }
                return;
            }

            start.subtract(dir);
        }

        player.sendMessage(ChatColor.RED + LangVar.msg_il.getVar());
    }

    private void meteoriteEffects(){
        startRay = meteorEnd.clone().add(radiusOfSpawnMeteor * sin(toRadians(new Random().nextInt(181) - 90)), radiusOfSpawnMeteor, radiusOfSpawnMeteor * sin(toRadians(new Random().nextInt(181) - 90)));

        circleEffect = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::circleEffect, 0, 8);

        dirRay = meteorEnd.clone().subtract(startRay).toVector();
        distance = (float) meteorEnd.distance(startRay);
        step = distance/amountOfSteps;
        k = step;

        rayV = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::rayV, 0, 20);
    }

    private void rayV(){
        for(int i = 0; i < 3; i++){
            new ShootingStarEffect(startRay.clone().add(new Random().nextInt(rndGen) - rndSpawn,new Random().nextInt(rndGen) - rndSpawn,new Random().nextInt(rndGen) - rndSpawn), meteorEnd, 20, 0.04f, 3, 5);
        }
        if(j > 3){
            rayV.cancel();
            meteoriteRay = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::meteoriteRay, 20, 5);
            return;
        }
        j++;
    }

    private void meteoriteRay(){
        dirRay.normalize();
        dirRay.multiply(k);
        startRay.add(dirRay);

        startRay.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, startRay, 3, 1,1,1, 0.5);
        startRay.getWorld().spawnParticle(Particle.LAVA, startRay, 5, 3,3,3, 1);

        startRay.subtract(dirRay);
        if(k > distance){
            meteoriteRay.cancel();
            startRay.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, meteorEnd.clone().add(0,1,0), 5, 2,0,2, 0.5);
            startRay.getWorld().spawnParticle(Particle.LAVA, meteorEnd.clone().add(0,3,0), 15, 3,2,3, 1);
            Bukkit.getScheduler().runTask(plugin, this::meteorExplosion);
            return;
        }
        k+=step;
    }

    private void circleEffect(){
        circleEffects.add(new CircleEffect(meteorEnd, r, 0.5f, 1, 5).getTask());
        if(d == 18){
            circleEffect.cancel();
            circleEffects.subList(0, circleEffects.size() - 9).clear();
            for(BukkitTask bukkitTask : circleEffects){
                bukkitTask.cancel();
            }
            return;
        }
        d++;
    }

    private void meteorExplosion(){
        List<Entity> entities = new ArrayList<>(meteorEnd.getWorld().getNearbyEntities(meteorEnd, r+1, r-1, r+1));
        for(Entity entity : entities){
            if(entity instanceof LivingEntity){
                if(entity == player){
                    continue;
                }
                ((LivingEntity)entity).damage(5, player);
            }

            Location epicenter = meteorEnd.clone().add(0,-1,0);
            Vector vector =entity.getLocation().clone().subtract(epicenter).toVector();
            vector.multiply(0.37 * (5/entity.getLocation().distance(epicenter)));
            entity.setVelocity(vector);
        }
    }
}
