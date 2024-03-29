package ru.vixtor141.MagickScrolls.scrolls;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.effects.RandomParticleGenerator;
import ru.vixtor141.MagickScrolls.interfaces.Scroll;
import ru.vixtor141.MagickScrolls.lang.LangVar;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.getPlayerMana;
import static ru.vixtor141.MagickScrolls.Misc.CheckUp.itemConsumer;

public class Confused implements Scroll {

    private final Main plugin = Main.getPlugin();
    private final Player player;
    private final ItemStack item;
    private BukkitTask stop, start, effect;
    private Entity entity;

    public Confused(Player player, ItemStack item){
        this.player = player;
        this.item = item;

        searchTarget();
    }

    private void searchTarget(){
        Location start = player.getEyeLocation().clone();
        Vector dir = player.getEyeLocation().getDirection();
        for(int i = 1; i < 10; i++) {
            dir.normalize();
            dir.multiply(i);
            start.add(dir);

            List<Entity> entityList = player.getWorld().getNearbyEntities(start, 0.5,0.5,0.5).parallelStream().filter(entity -> entity instanceof LivingEntity).collect(Collectors.toList());

            if(!entityList.isEmpty()) {
                for(Entity entity : entityList){
                    if(entity == player){
                        continue;
                    }
                    setTarget(entity);
                    return;
                }
            }
            start.subtract(dir);

        }
        player.sendMessage(ChatColor.YELLOW + LangVar.msg_tind.getVar());
    }

    private void setTarget(Entity entity){
        Mana playerMana = getPlayerMana(player);
        if(!playerMana.getCdSystem().CDStat(CDSystem.Scrolls.CONFUSED ,true))return;
        itemConsumer(player, item);

        this.entity = entity;

        start = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::setRandomVector, 0, 20);
        effect = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::effect, 0, 3);
        stop = Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, this::stop, 201);
    }

    private void setRandomVector(){
        entity.setVelocity(new Vector(new Random().nextInt(10) - 5, new Random().nextInt(4) + 1, new Random().nextInt(10) - 5).normalize());
    }

    private void effect(){
        new RandomParticleGenerator(entity.getLocation(), Particle.SPELL, 12, 7);
    }

    private void stop(){
        start.cancel();
        effect.cancel();
    }
}
