package ru.vixtor141.MagickScrolls.crafts;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import ru.vixtor141.MagickScrolls.Main;

import java.util.Collection;
import java.util.HashMap;

public class CauldronCrafting implements Runnable{

    private final HashMap<Material, Entity> listItems = new HashMap<>();
    private final Location location;
    private ItemStack craftResult, itemInHand;
    private BukkitTask bukkitTask;
    private int taskCounter = 0;

    public CauldronCrafting(Collection<Entity> collection, Location location, ItemStack itemInHand){
        this.location = location;
        this.itemInHand = itemInHand;
        if(collection.isEmpty())return;
        if(checking(collection))startCraftingProcess();
    }

    private boolean checking(Collection<Entity> entities){
        entities.parallelStream().filter(entityI -> entityI instanceof Item).forEach(entity -> listItems.put(((Item) entity).getItemStack().getType(), entity));
        return !listItems.isEmpty();
    }

    private void startCraftingProcess(){
        if(!(listItems.containsKey(Material.REDSTONE) && listItems.containsKey(Material.GLOWSTONE_DUST)))return;
        int ing1, ingR, ingG, maxIFC;
        Item redstone = (Item)listItems.remove(Material.REDSTONE), glowstone = (Item)listItems.remove(Material.GLOWSTONE_DUST), item3;
        ACCrafts.ItemsCauldronCrafts itemsCauldronCrafts = isIngredientExist();

        if(itemsCauldronCrafts == null)return;

        item3 =(Item) listItems.get(itemsCauldronCrafts.craftCauldronGetMaterial());
        ingR = redstone.getItemStack().getAmount();
        ingG = glowstone.getItemStack().getAmount();
        ing1 = item3.getItemStack().getAmount();


        if(ingR < ingG){
            maxIFC = ingR;
        }else maxIFC = Math.min(ingG, ing1);

        redstone.getItemStack().setAmount(ingR - maxIFC);
        glowstone.getItemStack().setAmount(ingG - maxIFC);
        item3.getItemStack().setAmount(ing1 - maxIFC);

        craftResult = itemsCauldronCrafts.craftCauldronGetItem();
        craftResult.setAmount(maxIFC);
        itemRemoveFromHand();

        bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getPlugin(), this::smokeEffect ,0,7);
        Bukkit.getScheduler().runTaskLater(Main.getPlugin(), this,27);
    }

    private void smokeEffect(){
        if(taskCounter < 3) {
            taskCounter++;
            location.getWorld().spawnParticle(Particle.SMOKE_LARGE, location.getX(), location.getY() - 0.5, location.getZ(), 6, 0, 0.2, 0, 0.05);
        }else {
            bukkitTask.cancel();
        }
    }

    @Override
    public void run() {
        location.add(0,0.3,0);
        location.getWorld().spawnParticle(Particle.END_ROD, location, 15, 0,0,0, 0.1);
        location.getWorld().spawnParticle(Particle.SPELL_INSTANT, location, 15, 0,0,0, 0.1);
        location.getWorld().spawnParticle(Particle.DRAGON_BREATH, location, 15, 0,0,0, 0.1);
        location.getWorld().spawnParticle(Particle.SPELL_WITCH, location, 15, 0,0,0, 0.1);
        location.add(0,-0.3,0);
        Item entityItem = location.getWorld().dropItem(location, craftResult);
        entityItem.setGravity(false);
        entityItem.setVelocity(new Vector(0,0,0));
    }

    private void itemRemoveFromHand(){
        if(Math.random() * 10 > 5 )return;
        itemInHand.setAmount(itemInHand.getAmount() - 1);
    }

    private ACCrafts.ItemsCauldronCrafts isIngredientExist(){
        for(ACCrafts.ItemsCauldronCrafts itemsCauldronCrafts : ACCrafts.ItemsCauldronCrafts.values()){
            if(listItems.containsKey(itemsCauldronCrafts.craftCauldronGetMaterial())){
                return itemsCauldronCrafts;
            }
        }
        return null;
    }
}
