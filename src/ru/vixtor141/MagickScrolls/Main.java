package ru.vixtor141.MagickScrolls;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import ru.vixtor141.MagickScrolls.Misc.BookCreator;
import ru.vixtor141.MagickScrolls.Misc.RitualsRecipesStorage;
import ru.vixtor141.MagickScrolls.commands.Commands;
import ru.vixtor141.MagickScrolls.crafts.AltarCraftsStorage;
import ru.vixtor141.MagickScrolls.crafts.CauldronCraftsStorage;
import ru.vixtor141.MagickScrolls.events.*;
import ru.vixtor141.MagickScrolls.includeAPI.PAPI;
import ru.vixtor141.MagickScrolls.tasks.CleanUpTask;

import java.util.*;


public class Main extends JavaPlugin {

    private static Main plugin;
    private double manaRegenUnit;
    private final List<LivingEntity> existMobs = new ArrayList<>();
    private final List<CleanUpTask> cleanUpTasks = new ArrayList<>();
    private final List<DefaultEffect> defaultEffectList = new ArrayList<>();
    private IOWork ioWork;
    private ItemStack ritualBook;
    private boolean manaMessage;

    public Main (){
        plugin = this;
    }

    public static Main getPlugin(){
        return plugin;
    }

    public void setManaRegenUnit(double manaRegenUnit){
        this.manaRegenUnit = manaRegenUnit;
    }

    public double getManaRegenUnit() {
        return manaRegenUnit;
    }

    public boolean getManaMessage(){
        return manaMessage;
    }

    public FileConfiguration getRecipesCF() {
        return ioWork.getRecipesCF();
    }

    public List<CleanUpTask> getCleanUpTasks(){
        return cleanUpTasks;
    }

    public List<LivingEntity> getExistMobs(){
        return existMobs;
    }

    public List<DefaultEffect> getDefaultEffectList() {
        return defaultEffectList;
    }

    public ItemStack getRitualBook(){
        return ritualBook;
    }

    public FileConfiguration getLangCF(){
        return ioWork.getLangCF();
    }

    public FileConfiguration getRitualsCF(){
        return ioWork.getRitualsCF();
    }

    public CauldronCraftsStorage getCauldronCraftsStorage(){
        return ioWork.getCauldronCraftsStorage();
    }

    public AltarCraftsStorage getAltarCraftsStorage(){
        return ioWork.getAltarCraftsStorage();
    }

    public RitualsRecipesStorage getRitualsRecipesStorage(){
        return ioWork.getRitualsRecipesStorage();
    }

    public FileConfiguration getCauldronCF() {
        return ioWork.getCauldronCF();
    }

    public IOWork getIoWork(){
        return ioWork;
    }

    @Override
    public void onEnable() {
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new PAPI(this).register();
        }

        initIOWork();

        createBook();

        readManaMessageSetting();

        this.getCommand("magickScrolls").setExecutor(new Commands());

        registerEventListeners();

        this.getLogger().info(ChatColor.YELLOW+"Plugin has been enabled");
    }


    @Override
    public void onDisable() {
        for (Player player: Bukkit.getOnlinePlayers()){
            ioWork.savePlayerStats(player);
        }
        for(CleanUpTask cleanUpTask : getCleanUpTasks()){
            cleanUpTask.getsWebTask().cancel();
            cleanUpTask.sWebCleanUpOnDisable();
        }
        getCleanUpTasks().clear();
        for(DefaultEffect defaultEffect : getDefaultEffectList()){
            defaultEffect.getKillerItemTask().cancel();
            defaultEffect.getEntityItem().remove();
        }
        getDefaultEffectList().clear();
        for(LivingEntity livingEntity: getExistMobs()){
            if(!livingEntity.isDead()){
                livingEntity.remove();
            }
        }
        getExistMobs().clear();
        this.getLogger().info(ChatColor.GOLD+"Plugin has been disabled");
    }

    public void initIOWork(){
        ioWork = new IOWork();
    }

    private void registerEventListeners(){
        Bukkit.getPluginManager().registerEvents(new VampiricScroll(), this);
        Bukkit.getPluginManager().registerEvents(new SaveAndLoad(), this);
        Bukkit.getPluginManager().registerEvents(new ArrowStormScroll(), this);
        Bukkit.getPluginManager().registerEvents(new ScrollOfNecromancy(), this);
        Bukkit.getPluginManager().registerEvents(new SpiderWebScroll(), this);
        Bukkit.getPluginManager().registerEvents(new CraftStartEvent(), this);
        Bukkit.getPluginManager().registerEvents(new TrapScroll(), this);
        Bukkit.getPluginManager().registerEvents(new EarthScroll(), this);
        Bukkit.getPluginManager().registerEvents(new RitualStartEvent(), this);
        Bukkit.getPluginManager().registerEvents(new RitualBreakingEvent(), this);
        Bukkit.getPluginManager().registerEvents(new ScrollUseEvent(), this);
        Bukkit.getPluginManager().registerEvents(new WitchAdd(), this);
        Bukkit.getPluginManager().registerEvents(new StartBuildEvent(), this);
        Bukkit.getPluginManager().registerEvents(new SpectralShieldScroll(), this);
        Bukkit.getPluginManager().registerEvents(new AirTrapScroll(), this);
    }

    public void createBook(){
        ritualBook = new BookCreator().getBook();
    }

    private void readManaMessageSetting(){
        manaMessage = plugin.getConfig().getBoolean("messageAboutMana");
    }

}
