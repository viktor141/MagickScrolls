package ru.vixtor141.MagickScrolls.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.metadata.LazyMetadataValue;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import ru.vixtor141.MagickScrolls.Main;

import static java.lang.Math.*;

public class EarthScrollTask implements Runnable {

    private final BukkitTask earthScrollTask;
    private int i = 0;
    private final int r;
    private final Location start;
    private final Vector dir;
    private final double angle, l1;
    private final Player player;

    public EarthScrollTask(int r, double angle,Location start, Vector dir, Player player){
        this.r = r;
        this.start = start;
        this.dir = dir;
        this.player = player;
        this.angle = angle;
        l1 = (PI/180)*angle;
        earthScrollTask = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(), this, 0, 2);
    }

    @Override
    public void run() {
        i++;
        dir.normalize();
        dir.multiply(i);
        start.add(dir);

        double l = ((PI*i)/180)*angle, z, x, movedAngle;

        for(double j = 0; j < l; j+= l1/2){
            movedAngle = (j/(PI*i))*PI;
            z = i * cos(toRadians(start.getYaw() - angle/2 + toDegrees(movedAngle)));
            x = i * sin(toRadians(start.getYaw() - angle/2 + toDegrees(movedAngle)));
            FallingBlock fallingBlock = start.getWorld().spawnFallingBlock(start.clone().add(x * ((double)-1), 0, z), start.clone().add(x * ((double)-1), -1, z).getBlock().getState().getData());
            fallingBlock.setVelocity(new Vector(0, 1.1, 0));
            fallingBlock.setDropItem(false);
            fallingBlock.setMetadata("magickscrolls_earthScoll", new LazyMetadataValue(Main.getPlugin(), () -> player));
            start.getWorld().getNearbyEntities(start.clone().add(x * ((double)-1), 0, z ), 2, 2,2).parallelStream().filter(entity -> !entity.getType().equals(EntityType.FALLING_BLOCK) && !entity.equals(player)).forEach(entity -> entity.setVelocity(dir.clone().add(new Vector(0,1.3,0)).multiply(1.5)));
        }

        start.subtract(dir);
        if(i >= r){
            earthScrollTask.cancel();
        }
    }
}
