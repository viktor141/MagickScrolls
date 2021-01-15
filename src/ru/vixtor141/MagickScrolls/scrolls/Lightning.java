package ru.vixtor141.MagickScrolls.scrolls;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.effects.RandomParticleGenerator;
import ru.vixtor141.MagickScrolls.interfaces.Scroll;
import ru.vixtor141.MagickScrolls.lang.LangVar;

import java.util.List;
import java.util.stream.Collectors;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.getPlayerMana;
import static ru.vixtor141.MagickScrolls.Misc.CheckUp.itemConsumer;


public class Lightning implements Scroll, Runnable {

    private final Player player;
    private final ItemStack item;
    private final double bound;
    private final int numberOfEntities;
    private List<LivingEntity> entitesInLocation;
    private final Mana playerMana;

    public enum Type{
        ONE(new int[]{5, 1}),
        TWO(new int[]{8, 4}),
        THREE(new int[]{10, 8});

        private final int[] i;
        Type(int[] i){
            this.i = i;
        }
        public int[] getData(){
            return i;
        }
    }

    public Lightning(Player player, ItemStack item, Type type ){
        this.player = player;
        this.item = item;

        playerMana = getPlayerMana(player);

        bound = type.getData()[0];
        numberOfEntities = type.getData()[1];

        getEntities();

    }

    public void getEntities() {

        entitesInLocation = (List<LivingEntity>)(List<?>) player.getNearbyEntities(bound,bound,bound).parallelStream().filter(entity -> (entity instanceof LivingEntity) && !(entity instanceof ArmorStand) && !(entity instanceof Villager)).collect(Collectors.toList());

        entitesInLocation = entitesInLocation.parallelStream().filter(livingEntity -> !livingEntity.hasPotionEffect(PotionEffectType.INVISIBILITY)).collect(Collectors.toList());

        if (entitesInLocation.isEmpty()) {
            player.sendMessage(ChatColor.YELLOW + LangVar.msg_nmay.getVar());
            return;
        }
        Bukkit.getScheduler().runTask(Main.getPlugin(), this::end);

    }

    @Override
    public void run(){

    }


    private void end(){
        Entity entity;

        if(!playerMana.getCdSystem().CDStat(CDSystem.Scrolls.LIGHTNING , numberOfEntities, (int) (30 * Math.log10(numberOfEntities)), true))return;

        for(int i = 0; i < numberOfEntities; i++) {

            entity = entitesInLocation.get(i);
            entity.getLocation().getWorld().strikeLightning(entity.getLocation()).setSilent(true);
            new RandomParticleGenerator(entity.getLocation().clone().add(0,1.5,0), 10, 15, (double)143/255, (double)220/255, (double)255/255);
            if(i == entitesInLocation.size()-1){
                itemConsumer(player, item);
                return;
            }
        }
    }
}
