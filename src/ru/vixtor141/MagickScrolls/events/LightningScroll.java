package ru.vixtor141.MagickScrolls.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.Misc.CheckUp;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;

import java.util.List;
import java.util.stream.Collectors;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.checkScrollEvent;

public class LightningScroll implements Listener, Runnable {

    private Player player;
    private PlayerInteractEvent event;
    private double bound;
    private int numberOfEntities;
    private ItemStack item;
    private List<LivingEntity> entitesInLocation;
    private final Main plugin = Main.getPlugin();

    Mana playerMana;
    @EventHandler
    public void use(PlayerInteractEvent event){
        if(checkScrollEvent(event))return;
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

        player = event.getPlayer();

        this.event = event;
        this.item = item;

        switch (checkTypeOfScroll(item)){

            case 1: bound = 5;
            numberOfEntities = 1;
            break;

            case 2: bound = 8;
                numberOfEntities = 4;
            break;

            case 3: bound = 10;
                numberOfEntities = 8;
            break;

            case 0: return;
        }

        getEntities();

    }

    private int checkTypeOfScroll(ItemStack item){

        if(CheckUp.checkItemLore(ACCrafts.CraftsOfScrolls.LIGHTNINGONE, item)){
            return 1;
        }else if(CheckUp.checkItemLore(ACCrafts.CraftsOfScrolls.LIGHTNINGTWO, item)){
            return 2;
        }else if(CheckUp.checkItemLore(ACCrafts.CraftsOfScrolls.LIGHTNINGTHREE, item)){
            return 3;
        }else {
            return 0;
        }
    }

    public void getEntities() {
        event.setCancelled(true);

        entitesInLocation = (List<LivingEntity>)(List<?>) player.getNearbyEntities(bound,bound,bound).parallelStream().filter(entity -> (entity instanceof LivingEntity) && !(entity instanceof ArmorStand)).collect(Collectors.toList());

        entitesInLocation = entitesInLocation.parallelStream().filter(livingEntity -> !livingEntity.hasPotionEffect(PotionEffectType.INVISIBILITY)).collect(Collectors.toList());

        this.playerMana = plugin.getPlayerMap().get(player);
        if (entitesInLocation.isEmpty()) {
            player.sendMessage(ChatColor.YELLOW + plugin.getReadingLangFile().getMsg("msg_nmay"));
            return;
        }
        Bukkit.getScheduler().runTask(Main.getPlugin(), this::end);

    }

    @Override
    public void run(){

    }


    private void end(){
        Entity entity;

        CDSystem.Scrolls scroll = CDSystem.Scrolls.LIGHTNING;

        if(!playerMana.getCdSystem().CDStatE(scroll, playerMana, ".consumedMana", ".CDseconds", numberOfEntities, (int) (30 * Math.log10(numberOfEntities)), true))return;

        for(int i = 0; i < numberOfEntities; i++) {

            entity = entitesInLocation.get(i);
            entity.getLocation().getWorld().strikeLightning(entity.getLocation()).setSilent(true);
            if(i == entitesInLocation.size()-1){
                if(!player.getGameMode().equals(GameMode.CREATIVE)) {
                    item.setAmount(item.getAmount() - 1);
                    event.getPlayer().getInventory().setItemInMainHand(item);
                }
                return;
            }
        }
    }
}
