package ru.vixtor141.MagickScrolls.Misc;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.configuration.file.FileConfiguration;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.interfaces.Ritual;
import ru.vixtor141.MagickScrolls.rituals.ManaUpFirst;
import ru.vixtor141.MagickScrolls.rituals.VillagerCast;

public class RitualEnum {

    private static final FileConfiguration lang = Main.getPlugin().getLangCF();

    public enum Rituals{
        MANAUPFIRST{
            @Override
            public Ritual getRitual(Mana playerMana) {
                return new ManaUpFirst(playerMana);
            }

            @Override
            public TextComponent getTitle() {
                return new TextComponent(lang.getString("rb_titleMUF").replace("\\n", "\n"));
            }

            @Override
            public TextComponent getNeed() {
                return new TextComponent(lang.getString("rb_needMUF").replace("\\n", "\n"));
            }

        },
        VILLAGER_CAST{
            @Override
            public Ritual getRitual(Mana playerMana) {
                return new VillagerCast();
            }

            @Override
            public TextComponent getTitle() {
                return new TextComponent(lang.getString("rb_titleVC").replace("\\n", "\n"));
            }

            @Override
            public TextComponent getNeed() {
                return new TextComponent(lang.getString("rb_needVC").replace("\\n", "\n"));
            }

        };

        public abstract TextComponent getTitle();
        public abstract TextComponent getNeed();
        public abstract Ritual getRitual(Mana playerMana);
    }
}
