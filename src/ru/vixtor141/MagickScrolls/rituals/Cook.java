package ru.vixtor141.MagickScrolls.rituals;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.altars.UsualAltar;
import ru.vixtor141.MagickScrolls.effects.RandomParticleGenerator;
import ru.vixtor141.MagickScrolls.interfaces.AltarFace;
import ru.vixtor141.MagickScrolls.interfaces.Ritual;
import ru.vixtor141.MagickScrolls.lang.LangVar;
import ru.vixtor141.MagickScrolls.ritual.RitualE;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Cook implements Ritual {

    private final RitualE ritual = RitualE.COOK;
    private final List<ItemStack> reqItems = new ArrayList<>(ritual.getReqItems());
    private AltarFace altar;
    private Location location;
    private List<Item> items;
    private int sumOverCooked = 0;
    private ItemStack itemStack;
    private final Player player;

    public Cook(Player player){
        this.player = player;
    }

    @Override
    public void action() {
        for (Item item : items){
            itemStack = item.getItemStack();
            switch (itemStack.getType()){
                case RAW_BEEF: cooking(item, Material.COOKED_BEEF); break;
                case PORK: cooking(item, Material.GRILLED_PORK); break;
                case RAW_CHICKEN: cooking(item, Material.COOKED_CHICKEN); break;
                case RAW_FISH:
                    if(itemStack.getData().equals(new ItemStack(Material.RAW_FISH, itemStack.getAmount(), (byte) 3).getData())){
                        break;
                    }
                    if(itemStack.getData().equals(new ItemStack(Material.RAW_FISH, itemStack.getAmount(), (byte) 2).getData())){
                        break;
                    }
                    cooking(item, Material.COOKED_FISH);
                    break;
                case RABBIT: cooking(item, Material.COOKED_RABBIT); break;
                case MUTTON: cooking(item, Material.COOKED_MUTTON); break;
                case POTATO_ITEM: cooking(item, Material.BAKED_POTATO); break;
                case CHORUS_FRUIT: cooking(item, Material.CHORUS_FRUIT_POPPED); break;
            }
            item.setPickupDelay(0);
        }
        float percentage =(float) 100/(new Random().nextInt(51) + 50);
        location.getWorld().dropItem(location.clone().add(0.5,1.5,0.5), new ItemStack(Material.COAL, (int)(sumOverCooked/percentage)));
    }

    private void cooking(Item item, Material material){
        overCooking();
        itemStack.setType(material);
        item.setItemStack(itemStack);
    }

    private void overCooking(){
        int count = itemStack.getAmount(), overCooked = count/(100/(new Random().nextInt(20) + 1));
        itemStack.setAmount(count - overCooked);
        sumOverCooked += overCooked;
    }


    @Override
    public List<ItemStack> getRequiredItems() {
        return reqItems;
    }

    @Override
    public AltarFace getAltar(Location location) {
        this.location = location;
        altar = new UsualAltar(location, this);
        return altar;
    }

    @Override
    public AltarFace getAltar() {
        return altar;
    }

    @Override
    public RitualE getEnumRitual() {
        return ritual;
    }

    @Override
    public boolean ObjectIsPlayer() {
        return false;
    }

    @Override
    public boolean canExec() {
        items =(List<Item>)(List<?>) location.getWorld().getNearbyEntities(location.clone().add(0.5,1.5,0.5), 1.5,1,1.5).parallelStream().filter(entity -> entity instanceof Item).collect(Collectors.toList());
        if(items.isEmpty()){
            player.sendMessage(LangVar.msg_nffc.getVar());
            return false;
        }
        for (Item item: items)item.setPickupDelay(20 * 30);
        return true;
    }

    @Override
    public void repeatingEffect() {
        new RandomParticleGenerator(location.clone().add(0.5,1.5,0.5), Particle.FLAME, 5, 4);
    }
}
