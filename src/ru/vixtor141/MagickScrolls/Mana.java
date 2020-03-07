package ru.vixtor141.MagickScrolls;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class Mana implements Runnable{
    private Player player;
    private double currentMana;
    private double maxMana;
    private Mana manaInst;
    private static Map<Player, Mana> PlayerMap = new HashMap<Player, Mana>();
    private double manaRegenUnit = 1.5;
    private BukkitTask bukkitTask;
    private long tupaFixCalledTwice; // fixed a bug when teleport scroll used twice
    private DefaultEffect defaultEffect = new DefaultEffect();

    public DefaultEffect getDefaultEffect() {
        return defaultEffect;
    }

    public long getTupaFixCalledTwice() {
        return tupaFixCalledTwice;
    }

    public void setTupaFixCalledTwice(long tupaFixCalledTwice) {
        this.tupaFixCalledTwice = tupaFixCalledTwice;
    }

    public static Map<Player, Mana> getPlayerMap() {
        return PlayerMap;
    }

    public Mana getManaInst() {
        return manaInst;
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

    public Mana(Player player) {
        this.player = player;
        this.manaInst = this;
        PlayerMap.put(player, this);
        bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getPlugin(), this, 20, 60);
    }

    public boolean consumeMana(double amount){
        if(amount <= this.currentMana){
            this.currentMana -= amount;
            player.sendMessage(Double.toString(currentMana));
            return true;
        }else{
            player.sendMessage("you don't have mana " + currentMana);
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
    }

    public void cancelTask(){
        bukkitTask.cancel();
    }
}
