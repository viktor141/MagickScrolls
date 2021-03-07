package ru.vixtor141.MagickScrolls.crafts;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.aspects.Aspect;
import ru.vixtor141.MagickScrolls.effects.EndrodSnowEffect;

import java.util.*;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.getPlayerMana;

public class AspectCraftingCauldron {

    private final HashMap<ItemStack, Entity> listItems = new HashMap<>();
    private final Collection<Entity> collection;
    private final Location location;
    private final Mana playerMana;
    private final Block block;

    public AspectCraftingCauldron(Collection<Entity> collection, Location location, Player player, Block block){
        this.collection = collection;
        this.location = location;
        this.playerMana = getPlayerMana(player);
        this.block = block;
        location.add(0,-0.6,0);
        Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), this::crafting);
    }

    private void crafting(){
        if(collection.isEmpty())return;
        for(Entity entity : collection){
            if(entity instanceof Item){
                ((Item) entity).setPickupDelay(4000);
                listItems.put(((Item) entity).getItemStack(), entity);
            }
        }

        if(!listItems.isEmpty()){
            HashMap<ItemStack, Map<Aspect, Integer>> aspectItems = Main.getPlugin().getIoWork().getAspectsInItems().get();
            for(ItemStack itemStack : listItems.keySet()){
                ItemStack newItem;
                if(itemStack.getItemMeta().hasLore()){
                    newItem = new ItemStack(Material.STICK, 1);
                    ItemMeta itemMeta = newItem.getItemMeta();
                    List<String> lore = itemStack.getItemMeta().getLore();
                    itemMeta.setLore(Collections.singletonList(lore.get(lore.size() - 2).substring(Main.getPlugin().getSubStr())));
                    newItem.setItemMeta(itemMeta);
                }else {
                    newItem = new ItemStack(itemStack.getType(), 1, itemStack.getData().getData());
                }
                Map<Aspect, Integer> aspectIntegerMap = aspectItems.get(newItem);
                Item  item = (Item)listItems.get(itemStack);

                if(aspectIntegerMap != null){
                    aspectIntegerMap.forEach((aspect, integer) -> addAndRemove(aspect, integer * itemStack.getAmount()));
                    item.remove();
                }else {
                    item.setPickupDelay(0);
                }
            }
        }
        Bukkit.getScheduler().runTask(Main.getPlugin(), () -> block.setData((byte)( block.getData() - 1)));
    }


    private void addAndRemove(Aspect aspect, int integer){
        playerMana.getPlayerAspectsStorage().addAspect(aspect,integer);
        new EndrodSnowEffect(4, 4, location.clone());
        location.getWorld().spawnParticle(Particle.SPELL_WITCH, location, 5, 0.2,0.2,0.2, 0.1);
    }

}
