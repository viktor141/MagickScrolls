package ru.vixtor141.MagickScrolls;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import ru.vixtor141.MagickScrolls.Misc.RitualEnum;
import ru.vixtor141.MagickScrolls.Misc.StartEffectForSpectralShield;
import ru.vixtor141.MagickScrolls.interfaces.Ritual;
import ru.vixtor141.MagickScrolls.lang.LangVar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Mana implements Runnable{

    private final Main plugin = Main.getPlugin();
    private final Player player;
    private double currentMana, maxMana;
    private double manaRegenUnit = plugin.getConfig().getDouble("manaregenunit");
    private final BukkitTask bukkitTask;
    private long tupaFixCalledTwice; // fixed a bug when teleport scroll used twice
    private final CDSystem cdSystem;
    private List<LivingEntity> existMobs = new ArrayList<>();
    private Inventory inventory;
    private ItemStack trapScroll;
    private Ritual ritual = null;
    private boolean inRitualChecker = false;
    private int spectralShieldSeconds = 0;
    private final AtomicBoolean spectralShield = new AtomicBoolean(false);
    private BukkitTask spectralShieldEffectTask;

    public Mana(Player player) {
        this.player = player;
        this.cdSystem = new CDSystem(player, this);
        plugin.getPlayerMap().put(player, this);
        bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getPlugin(), this, 20, 20);
    }

    public void setSpectralShieldSeconds(int spectralShieldSeconds) {
        if(spectralShieldSeconds > 0){
            spectralShieldEffectTask = new StartEffectForSpectralShield(player).getBukkitTask();
        }
        spectralShield.set(true);
        this.spectralShieldSeconds = spectralShieldSeconds;
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

    public void setRitual(String ritual){
        try {
            this.ritual = RitualEnum.Rituals.valueOf(ritual).getRitual(this);
        }catch (IllegalArgumentException e){
            player.sendMessage(ChatColor.RED + LangVar.msg_wnor.getVar());
        }
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
                player.sendMessage(LangVar.msg_ymn.getVar() + currentMana);
            }
            return true;
        }else{
            double youNeed = amount - currentMana;
            if(plugin.getManaMessage()) {
                player.sendMessage(LangVar.msg_ydnhm.getVar() + youNeed);
            }
            return false;
        }
    }

    public void addMana(double amount){
        this.currentMana += amount;
    }

    @Override
    public void run(){
        if((currentMana + manaRegenUnit) < maxMana) {
            addMana(manaRegenUnit);
        }else{
            addMana(manaRegenUnit - ((currentMana + manaRegenUnit) - maxMana));
        }
        if(spectralShieldSeconds > 1){
            spectralShieldSeconds--;
        }else if(spectralShieldSeconds == 1){
            spectralShieldSeconds--;
            spectralShield.set(false);
            spectralShieldEffectTask.cancel();
        }
        cdSystem.CDUpdate();
    }

    public void cancelTask(){
        bukkitTask.cancel();
    }
}
