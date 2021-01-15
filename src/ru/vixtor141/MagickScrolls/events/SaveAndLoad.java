package ru.vixtor141.MagickScrolls.events;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.LazyMetadataValue;
import org.bukkit.scheduler.BukkitTask;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;
import ru.vixtor141.MagickScrolls.research.Research;

import java.util.List;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.getPlayerMana;

public class SaveAndLoad implements Listener {

    private final Main plugin = Main.getPlugin();

    @EventHandler
    public void load(PlayerJoinEvent event){
        Player player = event.getPlayer();
        FileConfiguration playerStats = plugin.getIoWork().loadPlayerStats(player.getUniqueId().toString());
        Mana playerMana = new Mana(player);
        player.setMetadata("MagickScrollsMana", new LazyMetadataValue(Main.getPlugin(), playerMana::getMana));
        playerMana.setCurrentMana(playerStats.getDouble("CurrentMana"));
        playerMana.setMaxMana(playerStats.getDouble("MaxMana"));
        playerMana.getSpectralShield().set(playerStats.getBoolean("SpectralShield"));
        playerMana.setSpectralShieldSeconds(playerStats.getInt("SpectralShieldSeconds"));
        List<Integer> CDsList = playerStats.getIntegerList("CDSystem");
        for(int i = 0; i < CDSystem.CDsValuesLength(); i++){
            if(CDsList.size() <= i){//fill up empty CD
                playerMana.getCdSystem().getCDs().add(i,0);
            }else {
                playerMana.getCdSystem().getCDs().add(i, CDsList.get(i));
            }
        }
        List<Boolean> researchList = playerStats.getBooleanList("Research");
        for(int i = 0; i < Research.values().length; i++){
            if(researchList.size() <= i){//fill up empty Research
                playerMana.getPlayerResearch().getResearches().add(i,false);
            }else {
                playerMana.getPlayerResearch().getResearches().add(i, researchList.get(i));
            }
        }
        playerMana.getPlayerResearch().bookUpdate();
        List<String> activeResearch = playerStats.getStringList("ActiveResearch");
        if(!activeResearch.isEmpty()) {
            for (String research : activeResearch) {
                Research.valueOf(research).startFromLoad(playerMana.getPlayerResearch(), playerStats);
            }
        }
        playerMana.getPlayerResearch().getShieldManaLevels().setCount(playerStats.getInt("ShieldLevelCount", 0));
        playerMana.getPlayerResearch().getShieldManaLevels().setManaShieldLevel(playerStats.getString("ManaShieldLevel", "ZERO"));
        List<ItemStack> itemStacks = (List<ItemStack>) playerStats.getList("AccessoriesInventory");
        if(itemStacks != null && !itemStacks.isEmpty()) {
            for (ItemStack itemAdd : itemStacks) {
                if(itemAdd != null) {
                    playerMana.getPlayerResearch().getAccessoriesInventory().getInventory().addItem(itemAdd);
                    if (!itemAdd.getItemMeta().hasLore()) continue;
                    List<String> lore = itemAdd.getItemMeta().getLore();
                    String name = lore.get(lore.size() - 2).substring(Main.getPlugin().getSubStr());
                    if (ACCrafts.AccessoryArtefact.isArtefact(name)) {
                        playerMana.getWearingArtefact().set(ACCrafts.AccessoryArtefact.valueOf(name).ordinal(), true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void save(PlayerQuitEvent event){
        Mana playerMana = getPlayerMana(event.getPlayer());
        BukkitTask bukkitTask = playerMana.getSpectralShieldEffectTask();
        if(bukkitTask != null && !bukkitTask.isCancelled())bukkitTask.cancel();
        if(playerMana.getInRitualChecker()) {
            playerMana.setInRitualChecker(false);
            playerMana.getRitual().getAltar().ritualBrake();
        }
        plugin.getIoWork().savePlayerStats(event.getPlayer());
    }

}