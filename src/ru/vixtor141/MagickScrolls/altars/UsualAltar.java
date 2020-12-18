package ru.vixtor141.MagickScrolls.altars;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.RitualHandler;
import ru.vixtor141.MagickScrolls.effects.UsualAltarEffects;
import ru.vixtor141.MagickScrolls.interfaces.AltarFace;
import ru.vixtor141.MagickScrolls.interfaces.Ritual;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.*;

public class UsualAltar implements AltarFace, Runnable {

    private final Location location;
    private double j = 0, k = 45, s = 1;
    private final float r = 6, angle = 360/36F;
    private BukkitTask builderTask;
    private final Material pillar = Material.GOLD_BLOCK, circle = Material.OBSIDIAN;
    private final List<Item> items = new ArrayList<>();
    private final Ritual ritual;
    private UsualAltarEffects usualAltarEffects;
    private final List<ItemStack> ItemStackList = new ArrayList<>();
    private final ItemStack circleBlocks = new ItemStack(Material.OBSIDIAN, 36), pillarBlocks = new ItemStack(Material.GOLD_BLOCK, 8);


    public UsualAltar(Location location, Ritual ritual){
        this.location = location;
        this.ritual = ritual;
    }

    @Override
    public void builder(Player player){
        boolean circle = itemRemover(circleBlocks, player), pillar = itemRemover(pillarBlocks, player);
        if(!circle && !pillar){
            return;
        }else if(!circle){
            player.getInventory().addItem(pillarBlocks.clone());
            return;
        }else if(!pillar){
            player.getInventory().addItem(circleBlocks.clone());
            return;
        }
        builderTask = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(), this::altarBuilder, 0, 7);
    }

    private boolean itemRemover(ItemStack item, Player player){
        int count = item.getAmount(), currentCount;
        for(ItemStack itemStack : player.getInventory().all(item.getType()).values()){
            currentCount = itemStack.getAmount();
            if(count <= currentCount){
                itemStack.setAmount(currentCount - count);
                return true;
            }else{
                itemStack.setAmount(0);
                count -= currentCount;
            }
        }
        player.getInventory().addItem(new ItemStack(item.getType(), item.getAmount() - count));
        return false;
    }

    @Override
    public boolean checker(){
         //(180 * ((PI*r)/28))/(PI * r) |||| (180 * (l / (l - (l % 1))))/(PI * r)
        for(double i = 0; i <= 360; i+=angle){
            if(!location.clone().add(r * cos(toRadians(i)) + 0.5, 0, r * sin(toRadians(i)) + 0.5).getBlock().getType().equals(circle))return false;
        }
        for(int i = 45; i <= 360; i+=90){
            for(int j = 1; j < 3; j++){
                if(!location.clone().add(r * cos(toRadians(i)) + 0.5, j, r * sin(toRadians(i)) + 0.5).getBlock().getType().equals(pillar))return false;
            }
        }
        return true;
    }

    @Override
    public void behavior(RitualHandler ritualHandler, int witches, int[] neededAmounts) {
        for(Item item : items){
            if(!item.isDead()){
                item.setPickupDelay(9000);
            }
        }
        usualAltarEffects = new UsualAltarEffects(ritual, r, location, items, ritualHandler, witches, neededAmounts);
    }

    @Override
    public void ritualBrake(){
        usualAltarEffects.ritualBreaking();
        for(Item item : items){
            if(!item.isDead()){
                item.remove();
            }
        }
        for(ItemStack itemStack : ItemStackList){
            location.getWorld().dropItem(location.clone().add(0,2,0), itemStack);
        }
    }

    @Override
    public List<ItemStack> itemChecker() {
        for(int i = 45; i <= 360; i+=90){
            Optional<Entity> optionalEntity = location.getWorld().getNearbyEntities(location.getBlock().getLocation().add(r * cos(toRadians(i)) + 0.5, 3.5, r * sin(toRadians(i)) + 0.5), 0.7, 0.5,0.7).parallelStream().filter(entity -> entity instanceof Item).findFirst();
            if(optionalEntity.isPresent()){
                items.add((Item)optionalEntity.get());
                ((Item)optionalEntity.get()).setPickupDelay(40);
            }
        }
        items.parallelStream().forEach(item -> ItemStackList.add(item.getItemStack()));
        return ItemStackList;
    }

    private void altarBuilder() {
        if(j > 360){
            pillarBuilder();
            return;
        }
        location.clone().add(r * cos(toRadians(j * 3 + (10 * (int)(j * 3/360)))) + 0.5, 0, r * sin(toRadians(j * 3 + (10 * (int)(j * 3/360)))) + 0.5).getBlock().setType(circle);
        location.getWorld().spawnParticle(Particle.END_ROD, location.clone().add(r * cos(toRadians(j * 3)) + 0.5, 0, r * sin(toRadians(j * 3)) + 0.5), 10, 0,0,0, 0.1);
        j+=angle;
    }

    private void pillarBuilder(){
        if(k > 360){
            builderTask.cancel();
            return;
        }
        if(s < 3) {
            location.clone().add(r * cos(toRadians(k)) + 0.5, s, r * sin(toRadians(k)) + 0.5).getBlock().setType(pillar);
            s++;
            return;
        }
        s = 1;
        k+=90;
    }

    @Override
    public void run() {

    }
}
