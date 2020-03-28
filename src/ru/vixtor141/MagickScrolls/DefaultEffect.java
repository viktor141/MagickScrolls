package ru.vixtor141.MagickScrolls;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class DefaultEffect implements Runnable{
    private Player player;
    private Item entityItem;
    private BukkitTask KillerItemTask;
    private static List<DefaultEffect> defaultEffectList = new ArrayList<>();

    public DefaultEffect(Player player){
        this.player = player;
    }

    public void defaultEffectOfScrolls(){
        player.getWorld().spawnParticle(Particle.DRAGON_BREATH, player.getLocation().add(0, 1.7,0), 30, 0.35,0.35,0.35, 0.03);
        player.getWorld().playSound(player.getLocation().add(0,1,0), Sound.ENTITY_ILLUSION_ILLAGER_CAST_SPELL, 5, (float) 0.5);
        entityItem = player.getWorld().dropItem(player.getLocation().add(0, 2, 0), new ItemStack(Material.PAPER));
        entityItem.setVelocity(entityItem.getVelocity().add(new Vector(0,- 0.1,0)));
        entityItem.setGravity(false);
        entityItem.setPickupDelay(81);
        KillerItemTask = Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getPlugin(), this, 70);
        defaultEffectList.add(this);
    }

    @Override
    public void run() {
        entityItem.remove();
        defaultEffectList.remove(this);
    }

    public BukkitTask getKillerItemTask() {
        return KillerItemTask;
    }

    public Item getEntityItem() {
        return entityItem;
    }

    public static List<DefaultEffect> getDefaultEffectList() {
        return defaultEffectList;
    }
}
