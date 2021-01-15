package ru.vixtor141.MagickScrolls.scrolls;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.LazyMetadataValue;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.effects.RandomParticleGenerator;
import ru.vixtor141.MagickScrolls.interfaces.Scroll;

import static java.lang.Math.*;
import static ru.vixtor141.MagickScrolls.Misc.CheckUp.getPlayerMana;
import static ru.vixtor141.MagickScrolls.Misc.CheckUp.itemConsumer;

public class Earth implements Scroll, Runnable {

    private final Player player;
    private final int r = 7;
    private final float angle = 90;
    private  BukkitTask earthScrollTask;
    private int i = 0;
    private Location start;
    private  Vector dir;
    private  double l1;

    public Earth(Player player, ItemStack item){
        this.player = player;
        Mana playerMana = getPlayerMana(player);

        if(!playerMana.getCdSystem().CDStat(CDSystem.Scrolls.EARTH, true))return;

        Location locationOfP = player.getLocation();

        Location viewLocation = locationOfP.clone().add( r * sin(toRadians(locationOfP.getYaw())) * ((double)-1), 0, r * cos(toRadians(locationOfP.getYaw())));

        this.start = locationOfP.clone();
        this.l1 = (PI/180)*angle;
        this.dir = viewLocation.clone().subtract(locationOfP.clone()).toVector();
        earthScrollTask = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(), this, 0, 2);

        itemConsumer(player, item);
    }

    @Override
    public void run() {
        i++;
        dir.normalize();
        dir.multiply(i);
        start.add(dir);

        double l = ((PI*i)/180)*angle, z, x, movedAngle;

        for(double j = 0; j < l; j+= l1/2){
            movedAngle = j/i;
            z = i * cos(toRadians(start.getYaw() - angle/2 + toDegrees(movedAngle)));
            x = i * sin(toRadians(start.getYaw() - angle/2 + toDegrees(movedAngle)));
            FallingBlock fallingBlock = start.getWorld().spawnFallingBlock(start.clone().add(x * ((double)-1), 0, z), start.clone().add(x * ((double)-1), -1, z).getBlock().getState().getData());
            fallingBlock.setVelocity(new Vector(0, 1.1, 0));
            fallingBlock.setDropItem(false);
            fallingBlock.setMetadata("magickscrolls_earthScoll", new LazyMetadataValue(Main.getPlugin(), () -> player));
            dustEffect(start.clone().add(x * ((double)-1), 1, z));
            start.getWorld().getNearbyEntities(start.clone().add(x * ((double)-1), 0, z ), 2, 2,2).parallelStream().filter(entity -> !entity.getType().equals(EntityType.FALLING_BLOCK) && !entity.equals(player)).forEach(entity -> entity.setVelocity(dir.clone().add(new Vector(0,1.3,0)).multiply(1.5)));
        }

        start.subtract(dir);
        if(i >= r){
            earthScrollTask.cancel();
        }
    }

    private void dustEffect(Location location){
        new RandomParticleGenerator(location,10, 3, (double)102/255, (double)51/255, 0.0);
    }
}
