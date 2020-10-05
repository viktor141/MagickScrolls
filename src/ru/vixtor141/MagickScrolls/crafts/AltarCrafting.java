package ru.vixtor141.MagickScrolls.crafts;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import ru.vixtor141.MagickScrolls.Main;

import java.util.*;

import static java.lang.Math.*;
import static java.lang.Math.toRadians;

public class AltarCrafting implements Runnable{

    private List<Item> nearbyItemsIng = new ArrayList<>(0);
    private final Main plugin = Main.getPlugin();
    private final Location location;
    private BukkitTask craftCheckTask, effectTask;
    private List<Item>  droppedItems = new ArrayList<>(0);
    private final Map<List<ItemStack>, ACCrafts.CraftsOfScrolls> recipe = new HashMap<>();
    private Item ing;
    private Item paper;
    private boolean failCheck = true;
    private ACCrafts.CraftsOfScrolls scroll;
    private double k = 0.01;


    public AltarCrafting(Block block){
        this.location = block.getLocation();

        location.getWorld().getNearbyEntities(new Location(location.getWorld(), location.getX(), location.getY() + 0.875, location.getZ()),1,0.25, 1).stream().filter(entity -> entity instanceof Item).forEach(entity -> nearbyItemsIng.add(((Item)entity)));
        if(nearbyItemsIng.size() != 2)return;
        for(Item ingr : nearbyItemsIng){
            ingr.setPickupDelay(2000);
        }
        Optional<Item> optionalItemStack = nearbyItemsIng.parallelStream().filter(item -> !(item.getItemStack().getType().equals(Material.PAPER))).findFirst();
        Optional<Item> optionalItemStackPaper = nearbyItemsIng.parallelStream().filter(item -> (item.getItemStack().getType().equals(Material.PAPER))).findFirst();

        if(!optionalItemStack.isPresent() || !optionalItemStackPaper.isPresent()){
            for(Item ingr : nearbyItemsIng){
                ingr.setPickupDelay(0);
            }
            return;
        }

        ing = optionalItemStack.get();
        paper = optionalItemStackPaper.get();


        List<String> checkingItemLore;
        boolean checkup = false;

        if(ing.getItemStack().getItemMeta().getLore() == null){
            setPickupDelayToZero();
            return;
        }

        for(ACCrafts.ItemsCauldronCrafts itemsCauldronCrafts: ACCrafts.ItemsCauldronCrafts.values()){
            checkingItemLore = itemsCauldronCrafts.craftCauldronGetItem().getItemMeta().getLore();
            List<String> ingLore = ing.getItemStack().getItemMeta().getLore();
            if(ingLore.get(ingLore.size() - 2).equals(checkingItemLore.get(checkingItemLore.size() - 2))){
                craftStart(itemsCauldronCrafts);
                checkup = true;
                break;
            }
        }
        if(!checkup)setPickupDelayToZero();

    }

    private void setPickupDelayToZero(){
        ing.setPickupDelay(0);
        paper.setPickupDelay(0);
    }

    private void craftStart(ACCrafts.ItemsCauldronCrafts itemsCauldronCrafts){
        for(ACCrafts.CraftsOfScrolls scroll : itemsCauldronCrafts.getScroll()){
            if((plugin.getRecipesCF().getList(scroll.name())).isEmpty())continue;
            recipe.put(new ArrayList<> ((List<ItemStack>) plugin.getRecipesCF().getList(scroll.name())), scroll);
        }
        craftCheckTask = Bukkit.getScheduler().runTask(plugin, this::itemCheckUp);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, this);
    }

    private void dropItemFromItemFrame(ItemFrame itemFrame){
        ItemStack itemFromIF = itemFrame.getItem();
        if(itemFromIF.getType().equals(Material.AIR))return;
        itemFrame.setItem(new ItemStack(Material.AIR));
        Item entityItemFromIF = itemFrame.getWorld().dropItem(new Location(location.getWorld(), location.getX() + 0.5, location.getY() + 3, location.getZ() + 0.5), itemFromIF);
        entityItemFromIF.setGravity(false);
        entityItemFromIF.setPickupDelay(2000);
        entityItemFromIF.setVelocity(new Vector(0,0,0));
        entityItemFromIF.setGravity(false);
        droppedItems.add(entityItemFromIF);
    }

    private void itemCheckUp(){
        Optional<Entity> optionalEntity;
        for(int i = -3; i <= 3; i= i+6){
            for(int j = - 2; j <= 2; j= j+2){
                optionalEntity = location.getWorld().getNearbyEntities(new Location(location.getWorld(), location.getX() + i + 0.5, location.getY() + 1, location.getZ() + j + 0.5), 1,1,1).parallelStream().filter(entity -> entity instanceof ItemFrame).findFirst();
                searchingProcess(optionalEntity, location.getX() + i + 0.5, location.getY() + 1 + 0.5, location.getZ() + j + 0.5);
                optionalEntity = location.getWorld().getNearbyEntities(new Location(location.getWorld(), location.getX() + j + 0.5, location.getY() + 1, location.getZ() + i + 0.5), 1,1,1).parallelStream().filter(entity -> entity instanceof ItemFrame).findFirst();
                searchingProcess(optionalEntity, location.getX() + j + 0.5, location.getY() + 1 + 0.5, location.getZ() + i + 0.5);
            }
        }
        craftCheckupFinish();
        craftCheckTask.cancel();
    }

    private void searchingProcess(Optional<Entity> optionalEntity, double x, double y, double z){
        boolean found = false;
        if(optionalEntity.isPresent()){
            ItemFrame itemFrame = (ItemFrame) optionalEntity.get();
            for (List<ItemStack> itemStacks : recipe.keySet()) {
                if(itemStacks.remove(itemFrame.getItem()))found = true;
            }
            if(found){
                dropItemFromItemFrame(itemFrame);
                spawnParticleEffect(x,y,z);
            }
        }
    }

    private void craftCheckupFinish(){
        for (List<ItemStack> itemStacks : recipe.keySet()){
            if(!itemStacks.isEmpty())continue;

            recipe.forEach((itemStacks1, craftsOfScrolls1) -> equalsLists(itemStacks, itemStacks1, craftsOfScrolls1));
        }
        if(failCheck){
            craftResultUnsuccessful();
        }
    }

    private void equalsLists(List<ItemStack> itemStacks, List<ItemStack> itemStacks1, ACCrafts.CraftsOfScrolls craftsOfScrolls){
        if(itemStacks == itemStacks1){
            scroll = craftsOfScrolls;
            Bukkit.getScheduler().runTaskLater(plugin, this::craftFinishing, 138);
            effectTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::craftResultSuccessfulEffects, 30, 5);
            failCheck = false;
        }
    }


    private void spawnParticleEffect(double x, double y, double z){
        location.getWorld().spawnParticle(Particle.END_ROD, x, y, z, 6, 0,0,0, 0.1);
        location.getWorld().spawnParticle(Particle.SPELL_INSTANT, x, y, z, 6, 0,0,0, 0.1);
        location.getWorld().spawnParticle(Particle.DRAGON_BREATH, x, y, z, 6, 0.5,0.5,0.5, 0.1);
    }

    private void craftFinishing(){
        ItemStack result = scroll.craftAltarResult();
        ItemStack paperItemStack = paper.getItemStack();

        ing.getItemStack().setAmount(ing.getItemStack().getAmount() - 1);
        for(Item item : droppedItems){
            item.remove();
        }
        if(paperItemStack.getAmount() < 17){
            result.setAmount(paper.getItemStack().getAmount());
            paper.remove();
        }else{
            result.setAmount(16);
            paperItemStack.setAmount(paperItemStack.getAmount() - 16);
        }

        effectTask.cancel();
        ing.setPickupDelay(0);
        paper.setPickupDelay(0);
        Item droppedScroll = location.getWorld().dropItem(new Location(location.getWorld(), location.getX() + 0.5, location.getY() + 3, location.getZ() + 0.5), result);
        droppedScroll.setVelocity(new Vector(0,0,0));
        droppedScroll.setGravity(false);
        craftResultSuccessful();
    }

    private void craftResultSuccessfulEffects(){
        location.getWorld().spawnParticle(Particle.END_ROD, location.getX(), location.getY() + 4, location.getZ(), 4, 1.5, 1, 1.5, 0.1);
        location.getWorld().spawnParticle(Particle.PORTAL, location.getX(), location.getY() + 4, location.getZ(), 4, 1.5, 1, 1.5, 0.1);
        location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location.getX(), location.getY() + 4, location.getZ(), 7, 1.5, 1, 1.5, 0.1);
        location.getWorld().spawnParticle(Particle.FLAME, location.getX(), location.getY() + 4, location.getZ(), 7, 1.5, 1, 1.5, 0.1);


        if(k < 3.51) {
            for (int i = 0; i < 360; i += 36) {
                for (int j = 0; j < 360; j += 36) {

                    Location start = new Location(location.getWorld(), location.getX() + (3.2 * sin(toRadians(j)) * cos(toRadians(i))) + 0.5, location.getY() + (3.2 * sin(toRadians(i))) + 3.5, location.getZ() + (3.2 * cos(toRadians(j)) * cos(toRadians(i))) + 0.5);

                    Vector dir = new Location(location.getWorld(), location.getX() +  0.5, location.getY() + 3.5, location.getZ() + 0.5).subtract(start).toVector();
                    dir.normalize();
                    dir.multiply(k);
                    start.add(dir);
                    location.getWorld().spawnParticle(Particle.SPELL_WITCH, start, 1, 0,0,0);

                }
            }
            k+=0.5;
        }else{

            k = 0.01;
        }
    }

    private void craftResultSuccessful(){
        Location particleL =  location.clone();
        particleL.add(0.5,3.5,0.5);
        location.getWorld().spawnParticle(Particle.END_ROD, particleL, 20, 0,0,0, 0.3);
        location.getWorld().spawnParticle(Particle.SPELL_INSTANT, particleL, 20, 0,0,0, 0.3);
        location.getWorld().spawnParticle(Particle.DRAGON_BREATH, particleL, 20, 0,0,0, 0.3);
        location.getWorld().spawnParticle(Particle.SPELL_WITCH, particleL, 20, 0,0,0, 0.3);
    }

    private void craftResultUnsuccessful(){
        for(Item item : droppedItems){
            item.setPickupDelay(0);
        }
        ing.setPickupDelay(0);
        paper.setPickupDelay(0);
    }

    @Override
    public void run() {
    }
}