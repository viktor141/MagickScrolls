package ru.vixtor141.MagickScrolls;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class Mana implements Runnable{

    private final Main plugin = Main.getPlugin();
    private final Player player;
    private double currentMana;
    private double maxMana;
    private double manaRegenUnit = plugin.getConfig().getDouble("manaregenunit");
    private final BukkitTask bukkitTask;
    private long tupaFixCalledTwice; // fixed a bug when teleport scroll used twice
    private final CDSystem cdSystem;
    private List<LivingEntity> existMobs = new ArrayList<>();

    public Mana(Player player) {
        this.player = player;
        this.cdSystem = new CDSystem(player);
        plugin.getPlayerMap().put(player, this);
        bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getPlugin(), this, 20, 20);
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
            if(plugin.getConfig().getBoolean("messageAboutMana")) {
                player.sendMessage(plugin.getReadingLangFile().getMsg("msg_ymn") + currentMana);
            }
            return true;
        }else{
            double youNeed = amount - currentMana;
            player.sendMessage(plugin.getReadingLangFile().getMsg("msg_ydnhm") + youNeed);
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
        cdSystem.CDUpdate();
    }

    public void cancelTask(){
        bukkitTask.cancel();
    }
}
