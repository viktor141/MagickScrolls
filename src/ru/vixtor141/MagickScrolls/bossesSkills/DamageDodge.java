package ru.vixtor141.MagickScrolls.bossesSkills;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.bosses.Boss;
import ru.vixtor141.MagickScrolls.interfaces.BossSkill;

import java.util.Random;

public class DamageDodge implements BossSkill {

    private final Skill skill = Skill.DAMAGE_DODGE;

    public DamageDodge(EntityDamageByEntityEvent event, Boss boss){
        int percentage = new Random().nextInt(Main.getPlugin().getConfig().getInt(boss + ".skills.DAMAGE_DODGE.maxDodge")) + 1;
        double newDamage = event.getDamage()*(double)(percentage/100);
        event.setDamage(newDamage);
    }
}
