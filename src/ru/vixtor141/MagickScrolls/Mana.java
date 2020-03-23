package ru.vixtor141.MagickScrolls;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.vixtor141.MagickScrolls.Main.readingLangFile;

public class Mana implements Runnable{

    private Main plugin = Main.getPlugin();
    private Player player;
    private double currentMana;
    private double maxMana;
    private Mana manaInst;
    private static Map<Player, Mana> PlayerMap = new HashMap<>();
    private double manaRegenUnit = plugin.getConfig().getDouble("manaregenunit");
    private BukkitTask bukkitTask;
    private long tupaFixCalledTwice; // fixed a bug when teleport scroll used twice
    private DefaultEffect defaultEffect;
    private CDSystem cdSystem;
    private List<LivingEntity> existMobs = new ArrayList<>();

    public Mana(Player player) {
        this.player = player;
        this.manaInst = this;
        this.cdSystem = new CDSystem(player);
        this.defaultEffect = new DefaultEffect(player);
        PlayerMap.put(player, this);
        bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getPlugin(), this, 20, 20);
    }

    public List<LivingEntity> getExistMobs() {
        return existMobs;
    }

    public CDSystem getCdSystem() {
        return cdSystem;
    }

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

    public boolean consumeMana(double amount){
        if(amount <= this.currentMana){
            this.currentMana -= amount;
            if(plugin.getConfig().getBoolean("messageAboutMana")) {
                player.sendMessage(readingLangFile.msg_ymn + currentMana);
            }
            return true;
        }else{
            player.sendMessage(readingLangFile.msg_ydnhm + currentMana);
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
