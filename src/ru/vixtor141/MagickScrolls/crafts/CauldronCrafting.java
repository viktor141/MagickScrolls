package ru.vixtor141.MagickScrolls.crafts;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.research.Research;

import java.util.*;

public class CauldronCrafting implements Runnable{

    private final HashMap<ItemStack, Entity> listItems = new HashMap<>();
    private final Location location;
    private ItemStack craftResult;
    private final ItemStack itemInHand;
    private BukkitTask bukkitTask;
    private int taskCounter = 0;
    private final CauldronCraftsStorage cauldronCraftsStorage = Main.getPlugin().getCauldronCraftsStorage();
    private Map<List<ItemStack>, ACCrafts.ItemsCauldronCrafts> map;
    private ACCrafts.ItemsCauldronCrafts craft;
    private final List<ItemStack> itemRecipe = new ArrayList<>();
    private final Mana playerMana;

    public CauldronCrafting(Collection<Entity> collection, Location location, ItemStack itemInHand, Player player){
        this.location = location;
        this.itemInHand = itemInHand;
        this.playerMana = Main.getPlugin().getPlayerMap().get(player);
        if(checking(collection)) {
            map = cauldronCraftsStorage.getRecipes().get(listItems.size()-1);
            Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), this::check);
        }
    }

    private boolean checking(Collection<Entity> entities){
        if(entities.isEmpty())return false;
        for(Entity entity : entities){
            if(entity instanceof Item){
                listItems.put(((Item) entity).getItemStack(), entity);
            }
            if(listItems.size() == cauldronCraftsStorage.getMaxIngredients())break;
        }

        return !listItems.isEmpty();
    }
    private void startCraftingProcess(){
        List<ItemStack> realItems = new ArrayList<>(listItems.keySet());
        int cur;
        int min = realItems.get(0).getAmount();
        for(int i = 0; i < listItems.size(); i++){
            cur = realItems.get(i).getAmount();
            if(min > (cur/itemRecipe.get(i).getAmount())){
                min = cur/itemRecipe.get(i).getAmount();
            }
        }
        int curCount = 0;
        for(Entity entity : listItems.values()){
            ((Item)entity).setPickupDelay(1000);
            curCount++;
            if(entity.isDead()){
                for(int i = 0; i <=curCount; i++){
                    ((Item)listItems.values().toArray()[i]).setPickupDelay(0);
                }
                return;
            }
        }

        for(int i = 0; i< realItems.size(); i++){
            realItems.get(i).setAmount(realItems.get(i).getAmount() - (min*itemRecipe.get(i).getAmount()));
        }

        for(Entity entity: listItems.values()){
            if(!entity.isDead())((Item)entity).setPickupDelay(0);
        }

        craftResult = craft.craftCauldronGetItem().clone();
        craftResult.setAmount(min * craftResult.getAmount());
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

    private void check(){
        for(List<ItemStack> list : map.keySet()){
            if(checkItem(new ArrayList<>(list))){
                craft = map.get(list);
                for(Research research : craft.getResearch()){
                    if(!playerMana.getPlayerResearch().getResearches().get(research.ordinal())){
                        return;
                    }

                }
                startCraftingProcess();
            }else {
                itemRecipe.clear();
            }
        }
    }

    private boolean checkItem(List<ItemStack> list){
        List<ItemStack> checkList = new ArrayList<>(listItems.keySet());
        for(ItemStack itemStack1 : checkList ){
            for(ItemStack itemStack: list){
                if(itemStack.getType().equals(itemStack1.getType()) && itemStack.getAmount() <= itemStack1.getAmount() && itemStack.getDurability() == itemStack1.getDurability()){
                    if((!itemStack1.getItemMeta().hasLore() && !itemStack.getItemMeta().hasLore()) || (itemStack1.getItemMeta().hasLore() && itemStack.getItemMeta().hasLore() && itemStack.getItemMeta().getLore().get(0).equals(itemStack1.getItemMeta().getLore().get(itemStack1.getItemMeta().getLore().size() - 2).substring(2)))){
                        itemRecipe.add(itemStack);
                        list.remove(itemStack);
                        break;
                    }
                }
            }
        }
        return list.isEmpty();
    }
}
