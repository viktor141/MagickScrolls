package ru.vixtor141.MagickScrolls;

import org.bukkit.Bukkit;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.LazyMetadataValue;
import org.bukkit.scheduler.BukkitTask;
import ru.vixtor141.MagickScrolls.Misc.AncientBottleInventory;
import ru.vixtor141.MagickScrolls.Misc.FlyingItemsForPlayer;
import ru.vixtor141.MagickScrolls.Misc.StartEffectForSpectralShield;
import ru.vixtor141.MagickScrolls.aspects.PlayerAspectStorage;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;
import ru.vixtor141.MagickScrolls.interfaces.Ritual;
import ru.vixtor141.MagickScrolls.lang.LangVar;
import ru.vixtor141.MagickScrolls.research.PlayerResearch;
import ru.vixtor141.MagickScrolls.ritual.PlayerRitualInventory;
import ru.vixtor141.MagickScrolls.ritual.RitualE;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Mana implements Runnable{

    private final Main plugin = Main.getPlugin();
    private final Player player;
    private double currentMana = 50, maxMana = 50;
    private final BukkitTask bukkitTask, saveTask;
    private long tupaFixCalledTwice; // fixed a bug when teleport scroll used twice
    private final CDSystem cdSystem;
    private final List<LivingEntity> existMobs = new ArrayList<>();
    private Inventory inventory;
    private ItemStack trapScroll;
    private Ritual ritual = null;
    private boolean inRitualChecker = false, ritualStarted = false;
    private int spectralShieldSeconds = 0, individualID;
    private final AtomicBoolean spectralShield = new AtomicBoolean(false);
    private BukkitTask spectralShieldEffectTask;
    private final PlayerResearch playerResearch;
    private final AncientBottleInventory ancientBottleInventory;
    private final PlayerRitualInventory playerRitualInventory;
    private final List<FlyingItemsForPlayer> flyingItemsForPlayer = new ArrayList<>();
    private final List<Boolean> wearingArtefact = new ArrayList<>(ACCrafts.AccessoryArtefact.values().length);
    private final PlayerAspectStorage playerAspectsStorage;

    public Mana(Player player) {
        this.player = player;
        this.cdSystem = new CDSystem(player, this);
        this.playerResearch = new PlayerResearch(this);
        this.ancientBottleInventory = new AncientBottleInventory(player);
        this.playerRitualInventory = new PlayerRitualInventory(this);
        this.playerAspectsStorage = new PlayerAspectStorage(this);
        for(int i = 0; i < ACCrafts.AccessoryArtefact.values().length; i++){
            wearingArtefact.add(false);
        }
        bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this, 20, 20);
        saveTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::save, plugin.getTimeInterval() * 20, plugin.getTimeInterval() * 20);
        player.setMetadata("MagickScrollsMana", new LazyMetadataValue(Main.getPlugin(), this::getMana));
    }
    
    public Mana getMana(){
        return this;
    }

    public int getIndividualID(){
        return individualID;
    }

    public void setIndividualID(int individualID){
        this.individualID = individualID;
    }

    public PlayerAspectStorage getPlayerAspectsStorage() {
        return playerAspectsStorage;
    }

    public List<Boolean> getWearingArtefact(){
        return wearingArtefact;
    }

    public List<FlyingItemsForPlayer> getFlyingItemsForPlayer(){
        return flyingItemsForPlayer;
    }

    public PlayerRitualInventory getPlayerRitualInventory() {
        return playerRitualInventory;
    }

    public AncientBottleInventory getAncientBottleInventory() {
        return ancientBottleInventory;
    }

    public boolean getRitualStarted(){
        return ritualStarted;
    }

    public void setRitualStarted(boolean flag){
        ritualStarted = flag;
    }

    public PlayerResearch getPlayerResearch() {
        return playerResearch;
    }

    public void setSpectralShieldSeconds(int spectralShieldSeconds) {
        if(spectralShieldSeconds > 0){
            spectralShieldEffectTask = new StartEffectForSpectralShield(player).getBukkitTask();
            spectralShield.set(true);
            this.spectralShieldSeconds = spectralShieldSeconds;
        }
    }

    public BukkitTask getSpectralShieldEffectTask(){
        return spectralShieldEffectTask;
    }

    public int getSpectralShieldSeconds(){
        return spectralShieldSeconds;
    }

    public AtomicBoolean getSpectralShield(){
        return spectralShield;
    }

    public boolean getInRitualChecker(){
        return inRitualChecker;
    }

    public void setInRitualChecker(boolean check){
        inRitualChecker = check;
    }

    public Ritual getRitual() {
        return ritual;
    }

    public void setRitual(RitualE ritual){
        this.ritual = ritual.getRitual(this);
    }

    public ItemStack getTrapScroll() {
        return trapScroll;
    }

    public void setTrapScroll(ItemStack trapScroll) {
        this.trapScroll = trapScroll;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory){
        this.inventory = inventory;
    }

    public List<LivingEntity> getExistMobs() {
        return existMobs;
    }

    public CDSystem getCdSystem() {
        return cdSystem;
    }

    public long getTupaFixCalledTwice() {
        return tupaFixCalledTwice;
    }

    public void setTupaFixCalledTwice(long tupaFixCalledTwice) {
        this.tupaFixCalledTwice = tupaFixCalledTwice;
    }

    public Player getPlayer() {
        return player;
    }

    public double getCurrentMana() {
        return currentMana;
    }

    public double getMaxMana() {
        return maxMana;
    }

    public void setCurrentMana(double currentMana) {
        this.currentMana = currentMana;
    }

    public void setMaxMana(double maxMana) {
        this.maxMana = maxMana;
    }

    public boolean consumeMana(double amount){
        if(amount <= this.currentMana){
            this.currentMana -= amount;
            if(plugin.getManaMessage()) {
                player.sendMessage(ChatColor.GRAY + LangVar.msg_ymn.getVar() + currentMana);
            }
            return true;
        }else{
            if(plugin.getManaMessage()) {
                player.sendMessage(ChatColor.GRAY + LangVar.msg_ydnhm.getVar() + (amount - currentMana));
            }
            return false;
        }
    }

    public boolean consumeManaForMagnet(double amount){
        if(amount <= this.currentMana){
            this.currentMana -= amount;
            return true;
        }else{
            if(plugin.getManaMessage()) {
                player.sendMessage(ChatColor.GRAY + LangVar.msg_ydnhm.getVar() + (amount - currentMana));
            }
            return false;
        }
    }

    public void addMana(double amount){
        if((currentMana + amount) < maxMana) {
            currentMana += amount;
        }else{
            currentMana += amount - ((currentMana + amount) - maxMana);
        }
    }

    @Override
    public void run(){
        addMana(plugin.getManaRegenUnit());
        if(spectralShield.get()) {
            if (spectralShieldSeconds > 0) {
                spectralShieldSeconds--;
            } else {
                spectralShield.set(false);
                spectralShieldEffectTask.cancel();
            }
        }
        cdSystem.CDUpdate();
    }

    private void save(){
        if(Main.getPlugin().getConfig().getBoolean("useDB")){
            plugin.getDataBase().saveDataToDB(this);
        }else {
            plugin.getIoWork().savePlayerStats(player);
        }
    }

    public void cancelTask(){
        saveTask.cancel();
        bukkitTask.cancel();
    }
}
