package ru.vixtor141.MagickScrolls.bosses;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.LazyMetadataValue;
import org.bukkit.scheduler.BukkitTask;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.bossesSkills.AstralPet;
import ru.vixtor141.MagickScrolls.bossesSkills.DamageDodge;
import ru.vixtor141.MagickScrolls.bossesSkills.DodgeMove;
import ru.vixtor141.MagickScrolls.bossesSkills.Skill;
import ru.vixtor141.MagickScrolls.chatPlay.ImpSay;
import ru.vixtor141.MagickScrolls.effects.RandomParticleGenerator;
import ru.vixtor141.MagickScrolls.lang.LangVar;

import java.util.Random;

public class Imp {

    private final Main plugin = Main.getPlugin();
    private final EntityType[] entityTypes = {EntityType.BLAZE, EntityType.EVOKER, EntityType.WITHER_SKELETON, EntityType.PIG_ZOMBIE, EntityType.ZOMBIE_VILLAGER, EntityType.SILVERFISH, EntityType.WITCH};
    private final BukkitTask bukkitTask, repeater, particle;
    private final Creature angryIMP;
    private final Player player;
    private final Boss boss = Boss.IMP;
    private DodgeMove dodgeMove;
    private AstralPet astralPet;

    public Imp(Player player, Location location, String name){
        this.player = player;
        angryIMP = (Creature) location.getWorld().spawnEntity(location.clone().add(0.5,1,0.5),entityTypes[new Random().nextInt(entityTypes.length)]);
        AttributeInstance health = angryIMP.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        health.setBaseValue(plugin.getConfig().getDouble(boss.name() + ".health"));
        angryIMP.setHealth(health.getValue());
        AttributeInstance armor = angryIMP.getAttribute(Attribute.GENERIC_ARMOR);
        armor.setBaseValue(plugin.getConfig().getDouble(boss.name() + ".armor"));
        angryIMP.setTarget(player);
        angryIMP.setCustomName(name);
        angryIMP.setCustomNameVisible(true);
        angryIMP.setMetadata("magickScrolls_IMP_angry", new LazyMetadataValue(plugin, this::getImp));
        bukkitTask = Bukkit.getScheduler().runTaskLater(plugin, this::removeIMP, 20 * 60 * 10);
        repeater = Bukkit.getScheduler().runTaskTimer(plugin, this::startSkillPassive, 0, 20 * 60);
        particle = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::particleEffect, 0, 5);
    }

    private void removeIMP(){
        if(!angryIMP.isDead()){
            if(player == null || !player.isOnline())return;
            player.sendMessage(new ImpSay(LangVar.msg_ywidwtwamtoy.getVar()).changeMessage());
        }
        dead();
    }

    private Imp getImp(){
        return this;
    }

    public void startSkillPerDamage(EntityDamageByEntityEvent event){
       int chance = new Random().nextInt(100);
       if(chance < Skill.MOVE_DODGE.getChance(boss)){
           if(dodgeMove == null || dodgeMove.getBukkitTask().isCancelled()) {
               dodgeMove = new DodgeMove(angryIMP, boss, event.getDamager());
           }
       }
       if(chance < Skill.DAMAGE_DODGE.getChance(boss)){
           new DamageDodge(event, boss);
       }
    }

    private void startSkillPassive(){
        int chance = new Random().nextInt(100);
        if(chance < Skill.ASTRAL_PET_SKILL.getChance(boss)){
            astralPet = new AstralPet(angryIMP, boss, player);
        }
    }

    public void dead(){
        bukkitTask.cancel();
        repeater.cancel();
        particle.cancel();
        if(astralPet != null)astralPet.stop();
        if(dodgeMove != null)dodgeMove.getBukkitTask().cancel();
    }

    private void particleEffect(){
        new RandomParticleGenerator(angryIMP.getLocation(), Particle.END_ROD, 20, 5);
    }

    public Boss getBoss() {
        return boss;
    }
}
