package ru.vixtor141.MagickScrolls;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class DefaultEffect implements Runnable{
    private Player player;
    private Item entityItem;

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
        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getPlugin(), this, 80);
    }

    @Override
    public void run() {
        entityItem.remove();
    }
}
