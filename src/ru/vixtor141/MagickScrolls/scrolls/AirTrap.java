package ru.vixtor141.MagickScrolls.scrolls;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.LazyMetadataValue;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.effects.CylEffect;
import ru.vixtor141.MagickScrolls.interfaces.Scroll;
import ru.vixtor141.MagickScrolls.lang.LangVar;

import java.util.List;
import java.util.stream.Collectors;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.itemConsumer;

public class AirTrap implements Scroll {

    private final Main plugin = Main.getPlugin();
    private final Player player;
    private final ItemStack item;
    private Entity entity;
    private BukkitTask bukkitTask, stopTask;
    private CylEffect[] cylEffects = new CylEffect[2];


    public AirTrap(Player player, ItemStack item){
        this.player = player;
        this.item = item;

        Bukkit.getScheduler().runTaskAsynchronously(plugin, this::searchTarget);
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
        if(!player.hasMetadata("MagickScrollsMana")){
            player.sendMessage(ChatColor.RED + "WARNING!!! Player: " + player.getDisplayName() + " lost a plugin meta.");
            return;
        }
        Mana playerMana =(Mana) player.getMetadata("MagickScrollsMana").get(0).value();
        if(!playerMana.getCdSystem().CDStat(CDSystem.Scrolls.AIR_TRAP ,true))return;
        itemConsumer(player, item);
        entity.setGravity(false);
        entity.setVelocity(new Vector(0,0.035,0));
        entity.setMetadata("magickscrolls_Air_Trapped", new LazyMetadataValue(Main.getPlugin(), this::getInstance));
        this.entity = entity;
        bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::effect, 0, 18);
        stopTask = Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, this::stop, 200);
    }

    private void effect(){
        cylEffects[0] = new CylEffect(entity, 0.8f, 0f, 0.2f, 2, 9);
        cylEffects[1] = new CylEffect(entity, 0.8f, 0f, 0.2f, 2, -9);
    }

    public void stop(){
        if(!stopTask.isCancelled()){
            stopTask.cancel();
        }
        for(CylEffect cylEffect: cylEffects){
            cylEffect.getTask().cancel();
        }
        bukkitTask.cancel();
        entity.removeMetadata("magickscrolls_Air_Trapped", Main.getPlugin());
        entity.setGravity(true);
        Vector vector = entity.getLocation().subtract(player.getLocation()).toVector();
        vector.normalize();
        vector.multiply(2.5);
        vector.setY(0.5);

        entity.setVelocity(vector);
    }

    private AirTrap getInstance(){
        return this;
    }

}
