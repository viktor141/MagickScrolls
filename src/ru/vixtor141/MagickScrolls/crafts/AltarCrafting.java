package ru.vixtor141.MagickScrolls.crafts;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.LazyMetadataValue;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.effects.ShootingStarEffect;
import ru.vixtor141.MagickScrolls.research.PlayerResearch;

import java.util.*;

import static java.lang.Math.*;

public class AltarCrafting{

    private final List<Item> nearbyItemsIng = new ArrayList<>(0);
    private final Main plugin = Main.getPlugin();
    private final Location location;
    private BukkitTask craftCheckTask, effectTask;
    private final List<Item>  droppedItems = new ArrayList<>(0);
    private final Map<List<ItemStack>, ACCrafts.CraftsOfScrolls> recipe = new HashMap<>();
    private Item ing;
    private Item paper;
    private boolean failCheck = true;
    private ACCrafts.CraftsOfScrolls scroll;
    private int j = 0;
    private final int radiusOfSpawnMeteor = 15;
    private final PlayerResearch playerResearch;


    public AltarCrafting(Block block, PlayerResearch playerResearch){
        this.location = block.getLocation();
        this.playerResearch = playerResearch;

        location.getWorld().getNearbyEntities(new Location(location.getWorld(), location.getX(), location.getY() + 0.875, location.getZ()),1,0.25, 1).stream().filter(entity -> entity instanceof Item).forEach(entity -> nearbyItemsIng.add(((Item)entity)));
        if(nearbyItemsIng.size() != 2)return;
        for(Item ingredient : nearbyItemsIng){
            if(ingredient.hasMetadata("magickscrolls_altar_in_crafting_item")){
                return;
            }
        }
        for(Item ingredient : nearbyItemsIng){
            ingredient.setPickupDelay(2000);
        }
        Optional<Item> optionalItemStack = nearbyItemsIng.parallelStream().filter(item -> !(item.getItemStack().getType().equals(Material.PAPER))).findFirst();
        Optional<Item> optionalItemStackPaper = nearbyItemsIng.parallelStream().filter(item -> (item.getItemStack().getType().equals(Material.PAPER))).findFirst();

        if(!optionalItemStack.isPresent() || !optionalItemStackPaper.isPresent()){
            for(Item ingredient : nearbyItemsIng){
                ingredient.setPickupDelay(0);
            }
            return;
        }

        ing = optionalItemStack.get();
        paper = optionalItemStackPaper.get();
        setMetadata();

        boolean checkup = false;

        if(ing.getItemStack().getItemMeta().getLore() == null){
            setPickupDelayToZero();
            return;
        }

        for(ACCrafts.ItemsCauldronCrafts itemsCauldronCrafts: ACCrafts.ItemsCauldronCrafts.values()){
            List<String> ingLore = ing.getItemStack().getItemMeta().getLore();
            if(ingLore.get(ingLore.size() - 2).substring(Main.getPlugin().getSubStr()).equals(itemsCauldronCrafts.name())){
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
        unSetMetadata();
    }

    private void craftStart(ACCrafts.ItemsCauldronCrafts itemsCauldronCrafts){
        for(ACCrafts.CraftsOfScrolls scroll : itemsCauldronCrafts.getScroll()){
            if(plugin.getAltarCraftsStorage().getRecipes().get(scroll.ordinal()).isEmpty())continue;
            recipe.put(new ArrayList<> (plugin.getAltarCraftsStorage().getRecipes().get(scroll.ordinal())), scroll);
        }
        craftCheckTask = Bukkit.getScheduler().runTask(plugin, this::itemCheckUp);
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
                ItemStack realItem = itemFrame.getItem();
                for(int i = 0; i< itemStacks.size(); i++){
                    ItemStack recipeItem = itemStacks.get(i);
                    if(realItem.getType().equals(recipeItem.getType()) && realItem.getDurability() == recipeItem.getDurability()){
                        if((!realItem.getItemMeta().hasLore() && !recipeItem.getItemMeta().hasLore()) || (realItem.getItemMeta().hasLore() && recipeItem.getItemMeta().hasLore() && recipeItem.getItemMeta().getLore().get(0).equals(realItem.getItemMeta().getLore().get(realItem.getItemMeta().getLore().size() - 2).substring(Main.getPlugin().getSubStr())))){
                            itemStacks.remove(i);
                            found = true;
                            break;
                        }
                    }
                }
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
        if(!failCheck){
            Bukkit.getScheduler().runTaskLater(plugin, this::craftFinishing, 228);
            effectTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::craftResultSuccessfulEffects, 30, 4);
        }else{
            craftResultUnsuccessful();
        }
    }

    private void equalsLists(List<ItemStack> itemStacks, List<ItemStack> itemStacks1, ACCrafts.CraftsOfScrolls craftsOfScrolls){
        if(itemStacks == itemStacks1){
            scroll = craftsOfScrolls;
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
        int countPaper = playerResearch.getCountOfPaper();

        ing.getItemStack().setAmount(ing.getItemStack().getAmount() - 1);
        for(Item item : droppedItems){
            item.remove();
        }
        if(paperItemStack.getAmount() <= countPaper){
            result.setAmount(paper.getItemStack().getAmount());
            paper.remove();
        }else{
            result.setAmount(countPaper);
            paperItemStack.setAmount(paperItemStack.getAmount() - countPaper);
        }

        effectTask.cancel();
        setPickupDelayToZero();
        Item droppedScroll = location.getWorld().dropItem(new Location(location.getWorld(), location.getX() + 0.5, location.getY() + 3, location.getZ() + 0.5), result);
        droppedScroll.setVelocity(new Vector(0,0,0));
        droppedScroll.setGravity(false);
        craftResultSuccessful();
    }

    private void craftResultSuccessfulEffects(){
        new ShootingStarEffect(location.clone().add(0.5,3.3,0.5).clone().add(radiusOfSpawnMeteor * sin(toRadians(new Random().nextInt(181) - 90)), radiusOfSpawnMeteor, radiusOfSpawnMeteor * sin(toRadians(new Random().nextInt(181) - 90))), location.clone().add(0.5,3.3,0.5),14);

        location.getWorld().spawnParticle(Particle.END_ROD, location.getX(), location.getY() + 4, location.getZ(), 4, 1.5, 1, 1.5, 0.1);
        location.getWorld().spawnParticle(Particle.PORTAL, location.getX(), location.getY() + 4, location.getZ(), 4, 1.5, 1, 1.5, 0.1);
        location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location.getX(), location.getY() + 4, location.getZ(), 7, 1.5, 1, 1.5, 0.1);
        location.getWorld().spawnParticle(Particle.FLAME, location.getX(), location.getY() + 4, location.getZ(), 7, 1.5, 1, 1.5, 0.1);



            if(j > 360){
                j = 0;
            }
                for (int i = 0; i < 360; i += 20) {

                    Location start = new Location(location.getWorld(), location.getX() + (1.5 * sin(toRadians(j)) * cos(toRadians(i))) + 0.5, location.getY() + (1.5 * sin(toRadians(i))) + 3.5, location.getZ() + (1.5 * cos(toRadians(j)) * cos(toRadians(i))) + 0.5);

                    location.getWorld().spawnParticle(Particle.SPELL_WITCH, start, 0, 0 , 0 , 0 ,0.1);

                }
                j+=20;
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
        setPickupDelayToZero();
    }

    private void setMetadata(){
        ing.setMetadata("magickscrolls_altar_in_crafting_item",  new LazyMetadataValue(Main.getPlugin(), this::getAltarCrafting));
        paper.setMetadata("magickscrolls_altar_in_crafting_item", new LazyMetadataValue(Main.getPlugin(), this::getAltarCrafting));
    }

    private void unSetMetadata(){
        ing.removeMetadata("magickscrolls_altar_in_crafting_item", plugin);
        paper.removeMetadata("magickscrolls_altar_in_crafting_item", plugin);
    }

    private AltarCrafting getAltarCrafting(){
        return this;
    }
}
