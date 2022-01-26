package ru.vixtor141.MagickScrolls.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;

import java.util.Collection;
import java.util.List;

import static ru.vixtor141.MagickScrolls.events.InteractWithPedestal.PedestalThing.*;

public class InteractWithPedestal implements Listener {

    public enum PedestalThing {
        PEDESTAL_TOP("MagickScrolls_pedestalT", 0.0){
        },
        PEDESTAL_BOTTOM("MagickScrolls_pedestalB", 0.5){
        },
        PEDESTAL_ITEM("MagickScrolls_pedestalI", 0.0){

            @Override
            protected ItemStack getItemFromStand(ArmorStand armorStand) {
                armorStand.remove();
                return armorStand.getHelmet();
            }
        };


        private final String name;
        private final double high;
        PedestalThing(String name, double high){
            this.name = name;
            this.high = high;
        }

        public String getName(){
            return name;
        }

        private double getHigh(){
            return high;
        }

        protected ItemStack getItemFromStand(ArmorStand armorStand){
            Location location = armorStand.getLocation();
            if(this.equals(PEDESTAL_BOTTOM)){
                location.add(0,0.62,0);
            }
            location.add(0,1.15,0);
            for(Entity entity : location.getWorld().getNearbyEntities(location, 0.2,0.4,0.53)){
                if(entity.getName().equals(PEDESTAL_ITEM.getName())){
                    entity.remove();
                    return ((ArmorStand)entity).getHelmet();
                }
            }
            return null;
        }

    }


    @EventHandler
    public void placePedestal(PlayerInteractEvent event){
        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))return;
        if(event.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        if(event.getItem() == null)return;
        ItemStack item = event.getItem();

        if(!item.getItemMeta().hasLore() || !(item.getItemMeta().getLore().size() >= 2)) return;

        List<String> lore = item.getItemMeta().getLore();
        if(!lore.get(lore.size() - 2).substring(Main.getPlugin().getSubStr()).equals("PEDESTAL"))return;

        event.setCancelled(true);

        Location location = event.getClickedBlock().getLocation();


        if(!location.clone().add(0,1,0).getBlock().isEmpty() || !location.clone().add(0,2,0).getBlock().isEmpty()){
            event.getPlayer().sendMessage("Not enough space");
            return;
        }

        pedestalSetter(location.clone().add(0.5,-0.375,0.5), new EulerAngle(0, 0, 0), new ItemStack(Material.BONE_BLOCK), PEDESTAL_BOTTOM.getName());
        pedestalSetter(location.clone().add(0.5,0.245,0.5), new EulerAngle(0, 0, 0), new ItemStack(Material.BONE_BLOCK), PEDESTAL_TOP.getName());
        location.clone().add(0,1,0).getBlock().setType(Material.FENCE);
        item.setAmount(item.getAmount() - 1);
    }

    @EventHandler
    public void placeItemOn(PlayerInteractAtEntityEvent event){
        if(event.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        Location location = event.getRightClicked().getLocation();
        if(!event.getRightClicked().getName().equals(PEDESTAL_BOTTOM.getName()) && !event.getRightClicked().getName().equals(PEDESTAL_TOP.getName()))return;
        if(event.getRightClicked().getName().equals(PEDESTAL_BOTTOM.getName())){
            location.add(0,0.62,0);
        }
        event.setCancelled(true);
        ItemStack itemStack = event.getPlayer().getInventory().getItemInMainHand();
        if(itemStack.getType().equals(Material.AIR))return;

        EulerAngle eulerAngle;
        if(itemStack.getType().isSolid()){
            location.add(0,1.320,0);
            eulerAngle = new EulerAngle(0, 0, 0);
        }else {
            location.add(0,1.098,-0.525);
            eulerAngle = new EulerAngle(1.5707963267948966, 0, 0);
        }


        for(Entity entity : location.getWorld().getNearbyEntities(location, 0.2,0.4,0.76)){
            if(entity.getName().equals(PEDESTAL_ITEM.getName()))return;
        }

        ItemStack helmet = itemStack.clone();
        helmet.setAmount(1);
        itemStack.setAmount(itemStack.getAmount() - 1);

        ArmorStand armorStand =(ArmorStand)location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);

        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setInvulnerable(true);
        armorStand.setSmall(true);
        armorStand.setCustomName(PEDESTAL_ITEM.getName());
        armorStand.setCustomNameVisible(false);
        armorStand.setHeadPose(eulerAngle);
        armorStand.setHelmet(helmet);
    }

    @EventHandler
    public void getItemFromPedestalOrPedestal(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof Player))return;
        Player player =(Player)event.getDamager();
        PedestalThing pedestalThing = null;
        for(PedestalThing thing : PedestalThing.values()){
            if(thing.getName().equals(event.getEntity().getName())){
                pedestalThing = thing;
                break;
            }
        }
        if(pedestalThing == null)return;

        ItemStack itemStack = pedestalThing.getItemFromStand((ArmorStand)event.getEntity());

        if(itemStack == null){
            Collection<Entity> collection = player.getWorld().getNearbyEntities(event.getEntity().getLocation(), 0.1,0.623,0.1);

            for(Entity entity : collection){
                if(entity.getName().equals(PEDESTAL_BOTTOM.getName()) && event.getEntity().getLocation().add(0,1.5,0).getBlock().getType().equals(Material.FENCE)){
                    event.getEntity().getLocation().add(0,1.5,0).getBlock().setType(Material.AIR);
                    player.getWorld().dropItem(event.getEntity().getLocation().add(0, 1.5, 0), ACCrafts.ItemsCauldronCrafts.PEDESTAL.craftCauldronGetItem());
                }
                entity.remove();
            }
            return;
        }

        player.getWorld().dropItem(event.getEntity().getLocation().add(0,2.3 + pedestalThing.getHigh(),0), itemStack);

    }

    private void pedestalSetter(Location location, EulerAngle eulerAngle, ItemStack itemStack, String name){
        ArmorStand armorStand = (ArmorStand)location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setInvulnerable(true);
        armorStand.setCustomName(name);
        armorStand.setCustomNameVisible(false);
        armorStand.setHeadPose(eulerAngle);
        armorStand.setHelmet(itemStack);
    }

}

/*
        pedestal[0] = pedestalSetter(location.clone().add(0.5,-0.4,0.5), new EulerAngle(0, 0, 0), new ItemStack(Material.STEP), "MagickScrolls_pedestalB");
        pedestal[1] = pedestalSetter(location.clone().add(0.5,-0.085,0.5), new EulerAngle(0, 0, 0), new ItemStack(Material.COBBLE_WALL), "MagickScrolls_pedestalM1");
        pedestal[2] = pedestalSetter(location.clone().add(0.5,-0.085,0.5), new EulerAngle(0, 1.5707963267948966, 0), new ItemStack(Material.COBBLE_WALL), "MagickScrolls_pedestalM2");
        pedestal[3] = pedestalSetter(location.clone().add(0.5,0.422,0.5), new EulerAngle(0, 0, 0), new ItemStack(Material.STEP), "MagickScrolls_pedestalT");
*/

/*
        pedestalSetter(location.clone().add(0.5,-0.4,0.5), new EulerAngle(0, 0, 0), new ItemStack(Material.QUARTZ_BLOCK, 1, (short) 2), "MagickScrolls_pedestalB");
        pedestalSetter(location.clone().add(0.5,0.225,0.5), new EulerAngle(0, 0, 0), new ItemStack(Material.QUARTZ_BLOCK,1, (short) 1), "MagickScrolls_pedestalT");

 */

/*
    public void placeItemP(PlayerInteractEvent event){
        if(event.getHand().equals(EquipmentSlot.OFF_HAND)) return;
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
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setCustomName("MagickScrolls_pedestalA");
        armorStand.setCustomNameVisible(true);
        armorStand.setHeadPose(eulerAngle);
        armorStand.setHelmet(itemStack);
    }

 */