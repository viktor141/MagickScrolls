package ru.vixtor141.MagickScrolls.Misc;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.interfaces.Ritual;
import ru.vixtor141.MagickScrolls.rituals.*;

import java.util.List;

public class RitualEnum {

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
                return new Cook(playerMana.getPlayer());
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

        },
        HEALING{
            @Override
            public Ritual getRitual(Mana playerMana) {
                return new Healing(playerMana);
            }

            @Override
            public TextComponent getTitle() {
                return new TextComponent(get("rb_titleH"));
            }

            @Override
            public TextComponent getDescription() {
                return new TextComponent(get("rb_DescriptionH"));
            }

            @Override
            public TextComponent getNeed() {
                return new TextComponent(get("rb_needH"));
            }

        },
        DIVER{
            @Override
            public Ritual getRitual(Mana playerMana) {
                return new Diver(playerMana);
            }

            @Override
            public TextComponent getTitle() {
                return new TextComponent(get("rb_titleD"));
            }

            @Override
            public TextComponent getDescription() {
                return new TextComponent(get("rb_DescriptionD"));
            }

            @Override
            public TextComponent getNeed() {
                return new TextComponent(get("rb_needD"));
            }

        },
        MINER{
            @Override
            public Ritual getRitual(Mana playerMana) {
                return new Miner(playerMana);
            }

            @Override
            public TextComponent getTitle() {
                return new TextComponent(get("rb_titleM"));
            }

            @Override
            public TextComponent getDescription() {
                return new TextComponent(get("rb_DescriptionM"));
            }

            @Override
            public TextComponent getNeed() {
                return new TextComponent(get("rb_needM"));
            }

        },
        TRAVELER{
            @Override
            public Ritual getRitual(Mana playerMana) {
                return new Traveler(playerMana);
            }

            @Override
            public TextComponent getTitle() {
                return new TextComponent(get("rb_titleT"));
            }

            @Override
            public TextComponent getDescription() {
                return new TextComponent(get("rb_DescriptionT"));
            }

            @Override
            public TextComponent getNeed() {
                return new TextComponent(get("rb_needT"));
            }

        },
        WARRIOR{
            @Override
            public Ritual getRitual(Mana playerMana) {
                return new Warrior(playerMana);
            }

            @Override
            public TextComponent getTitle() {
                return new TextComponent(get("rb_titleW"));
            }

            @Override
            public TextComponent getDescription() {
                return new TextComponent(get("rb_DescriptionW"));
            }

            @Override
            public TextComponent getNeed() {
                return new TextComponent(get("rb_needW"));
            }

        };

        public abstract TextComponent getTitle();
        public abstract TextComponent getDescription();
        public abstract TextComponent getNeed();
        public abstract Ritual getRitual(Mana playerMana);

        public List<ItemStack> getReqItems(){
            return Main.getPlugin().getRitualsRecipesStorage().getRecipes().get(this.ordinal());
        }
    }

    private static String get(String string){
        return Main.getPlugin().getLangCF().getString(string).replace("\\n", "\n");
    }
}
