package ru.vixtor141.MagickScrolls.events;

import net.minecraft.server.v1_12_R1.Block;
import net.minecraft.server.v1_12_R1.BlockPlant;
import org.apache.logging.log4j.core.net.Priority;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.LazyMetadataValue;
import org.bukkit.util.EulerAngle;
import ru.vixtor141.MagickScrolls.Main;

public class InteractWithPedestal implements Listener {


    @EventHandler(priority = EventPriority.HIGHEST)
    public void placeItem(PlayerInteractEvent event){
        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))return;
        Location location = event.getClickedBlock().getLocation();
        ItemStack itemStack = event.getItem();

        EulerAngle eulerAngle;
        event.getPlayer().sendMessage(itemStack.toString());
        if(itemStack.getType().isSolid()){
            location.add(0.5,-0.4,0.5);
            eulerAngle = new EulerAngle(0, 0, 0);
        }else {
            location.add(0.5,-0.69,-0.25);
            eulerAngle = new EulerAngle(1.5707963267948966, 0, 0);
        }
        event.setCancelled(true);

        ArmorStand armorStand =(ArmorStand)location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);

        armorStand.setMetadata("MagickScrolls_pedestalA",  new LazyMetadataValue(Main.getPlugin(), this::getI));
        armorStand.setVisible(true);
        armorStand.setGravity(false);
        armorStand.setCustomName("MagickScrolls_pedestalA");
        armorStand.setCustomNameVisible(true);
        armorStand.setHeadPose(eulerAngle);
        armorStand.setHelmet(itemStack);
    }

    @EventHandler
    public void getFromItem(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof Player))return;
        if(!event.getEntity().hasMetadata("MagickScrolls_pedestalA"))return;
        event.getEntity().remove();
        event.getDamager().sendMessage("per");
    }


    public void placePedestal(BlockPlaceEvent event){
        if(!event.getItemInHand().getType().equals(Material.GRAVEL))return;
        event.getBlockReplacedState().setMetadata("MagickScrolls_pedestal",  new LazyMetadataValue(Main.getPlugin(), this::getI));
    }


    public void breakBlock(BlockBreakEvent event){
        if(event.getBlock().getState().hasMetadata("MagickScrolls_pedestal")) {
            event.getPlayer().sendMessage("pidr");
            event.getBlock().getState().removeMetadata("MagickScrolls_pedestal", Main.getPlugin());
        }
    }

    private int getI(){
        return 1;
    }
}
