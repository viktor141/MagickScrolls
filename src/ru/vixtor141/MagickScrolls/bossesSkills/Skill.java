package ru.vixtor141.MagickScrolls.bossesSkills;

import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.bosses.Boss;

public enum Skill {
    MOVE_DODGE,
    DAMAGE_DODGE,
    ASTRAL_PET_SKILL;

    private final Main plugin = Main.getPlugin();

    public int getChance(Boss boss){
        return plugin.getConfig().getInt(boss.name() + ".skills." + this.name() + ".chance");
    }

    public int getTime(Boss boss){
        return plugin.getConfig().getInt(boss.name() + ".skills." + this.name() + ".time");
    }
}
