package ru.vixtor141.MagickScrolls.rituals;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.altars.UsualAltar;
import ru.vixtor141.MagickScrolls.effects.RandomParticleGenerator;
import ru.vixtor141.MagickScrolls.interfaces.AltarFace;
import ru.vixtor141.MagickScrolls.interfaces.Ritual;
import ru.vixtor141.MagickScrolls.ritual.RitualE;

import java.util.ArrayList;
import java.util.List;

public class Warrior implements Ritual {

    private final RitualE ritual =  RitualE.WARRIOR;
    private final CDSystem.RitualsCD ritualCD = CDSystem.RitualsCD.WARRIOR;
    private final List<ItemStack> reqItems = new ArrayList<>(ritual.getReqItems());
    private AltarFace altar;
    private Location location;
    private final Mana playerMana;

    public Warrior(Mana playerMana){
        this.playerMana = playerMana;
    }

    @Override
    public void action() {
        Player player= playerMana.getPlayer();
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 60 * 15, 2), true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 60 * 15, 1), true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 60 * 15, 2), true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 20 * 60 * 15, 2), true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20 * 60 * 15, 1), true);
    }

    @Override
    public List<ItemStack> getRequiredItems() {
        return reqItems;
    }

    @Override
    public AltarFace getAltar(Location location) {
        this.location = location;
        altar = new UsualAltar(location, this);
        return altar;
    }

    @Override
    public AltarFace getAltar() {
        return altar;
    }

    @Override
    public RitualE getEnumRitual() {
        return ritual;
    }

    @Override
    public boolean ObjectIsPlayer() {
        return true;
    }

    @Override
    public boolean canExec() {
        return playerMana.getCdSystem().CDStat(ritualCD);
    }

    @Override
    public void repeatingEffect() {
        new RandomParticleGenerator(location.clone().add(0.5,2.5,0.5), Particle.CRIT_MAGIC, 10, 10);
    }
}
