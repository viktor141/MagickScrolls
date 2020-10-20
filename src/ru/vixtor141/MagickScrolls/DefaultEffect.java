package ru.vixtor141.MagickScrolls;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.LazyMetadataValue;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class DefaultEffect implements Runnable{

    private Item entityItem;
    private BukkitTask KillerItemTask;
    private final Main plugin = Main.getPlugin();

    public DefaultEffect(Player player){
        player.getWorld().spawnParticle(Particle.DRAGON_BREATH, player.getLocation().add(0, 1.7,0), 30, 0.35,0.35,0.35, 0.03);
        player.getWorld().playSound(player.getLocation().add(0,1,0), Sound.BLOCK_CHEST_OPEN, 5, (float) 0.5);
        entityItem = player.getWorld().dropItem(player.getLocation().add(0, 2, 0), new ItemStack(Material.PAPER));
        entityItem.setMetadata("magickscrolls", new LazyMetadataValue(Main.getPlugin(), player::getName));
        entityItem.setVelocity(entityItem.getVelocity().add(new Vector(0,- 0.1,0)));
        entityItem.setGravity(false);
        entityItem.setPickupDelay(81);
        KillerItemTask = Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getPlugin(), this, 70);
        plugin.getDefaultEffectList().add(this);
    }

    @Override
    public void run() {
        if( Math.random() * 10 > 2) {
            entityItem.remove();
        }
        plugin.getDefaultEffectList().remove(this);
    }

    public BukkitTask getKillerItemTask() {
        return KillerItemTask;
    }

    public Item getEntityItem() {
        return entityItem;
    }

}
