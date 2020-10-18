package ru.vixtor141.MagickScrolls.rituals;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.Misc.RitualEnum;
import ru.vixtor141.MagickScrolls.altars.UsualAltar;
import ru.vixtor141.MagickScrolls.effects.RandomParticleGenerator;
import ru.vixtor141.MagickScrolls.interfaces.AltarFace;
import ru.vixtor141.MagickScrolls.interfaces.Ritual;

import java.util.ArrayList;
import java.util.List;

import static ru.vixtor141.MagickScrolls.Misc.RitualEnum.Rituals.HEALING;

public class Healing implements Ritual {

    private final RitualEnum.Rituals ritual = HEALING;
    private final List<ItemStack> reqItems;
    private AltarFace altar;
    private Location location;
    private final Mana playerMana;

    public Healing(Mana playerMana){
        this.playerMana = playerMana;
        reqItems = new ArrayList<>((List<ItemStack>) Main.getPlugin().getRitualsCF().getList(ritual.name()));
    }

    @Override
    public void action() {
        playerMana.setCurrentMana(playerMana.getMaxMana());
        Player player = playerMana.getPlayer();
        for(PotionEffect potionEffect : player.getActivePotionEffects())player.removePotionEffect(potionEffect.getType());
        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
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
    public RitualEnum.Rituals getEnumRitual() {
        return ritual;
    }

    @Override
    public boolean ObjectIsPlayer() {
        return true;
    }

    @Override
    public boolean canExec() {
        return true;
    }

    @Override
    public void repeatingEffect() {
        new RandomParticleGenerator(location.clone().add(0.5,2.5,0.5), Particle.HEART, 10, 10);
    }
}
