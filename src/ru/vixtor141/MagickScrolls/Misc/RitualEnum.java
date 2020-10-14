package ru.vixtor141.MagickScrolls.Misc;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.configuration.file.FileConfiguration;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.interfaces.Ritual;
import ru.vixtor141.MagickScrolls.rituals.Cook;
import ru.vixtor141.MagickScrolls.rituals.ManaUpFirst;
import ru.vixtor141.MagickScrolls.rituals.ManaUpSecond;
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
                return new TextComponent(get("rb_titleMUF"));
            }

            @Override
            public TextComponent getDescription() {
                return new TextComponent(get("rb_DescriptionMUF"));
            }

            @Override
            public TextComponent getNeed() {
                return new TextComponent(get("rb_needMUF"));
            }

        },
        VILLAGER_CAST{
            @Override
            public Ritual getRitual(Mana playerMana) {
                return new VillagerCast();
            }

            @Override
            public TextComponent getTitle() {
                return new TextComponent(get("rb_titleVC"));
            }

            @Override
            public TextComponent getDescription() {
                return new TextComponent(get("rb_DescriptionVC"));
            }

            @Override
            public TextComponent getNeed() {
                return new TextComponent(get("rb_needVC"));
            }

        },
        COOK{
            @Override
            public Ritual getRitual(Mana playerMana) {
                return new Cook();
            }

            @Override
            public TextComponent getTitle() {
                return new TextComponent(get("rb_titleC"));
            }

            @Override
            public TextComponent getDescription() {
                return new TextComponent(get("rb_DescriptionC"));
            }

            @Override
            public TextComponent getNeed() {
                return new TextComponent(get("rb_needC"));
            }

        },
        MANAUPSECOND{
            @Override
            public Ritual getRitual(Mana playerMana) {
                return new ManaUpSecond(playerMana);
            }

            @Override
            public TextComponent getTitle() {
                return new TextComponent(get("rb_titleMUS"));
            }

            @Override
            public TextComponent getDescription() {
                return new TextComponent(get("rb_DescriptionMUS"));
            }

            @Override
            public TextComponent getNeed() {
                return new TextComponent(get("rb_needMUS"));
            }

        };

        public abstract TextComponent getTitle();
        public abstract TextComponent getDescription();
        public abstract TextComponent getNeed();
        public abstract Ritual getRitual(Mana playerMana);
    }

    private static String get(String string){
        return lang.getString(string).replace("\\n", "\n");
    }
}
