package ru.vixtor141.MagickScrolls.ritual;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.interfaces.Ritual;
import ru.vixtor141.MagickScrolls.lang.LangVar;
import ru.vixtor141.MagickScrolls.research.Research;
import ru.vixtor141.MagickScrolls.rituals.*;

import java.util.ArrayList;
import java.util.List;

public enum RitualE {

    MANAUPFIRST{
        @Override
        public Ritual getRitual(Mana playerMana) {
            return new ManaUpFirst(playerMana);
        }

        @Override
        protected Material getMaterial(){
            return Material.STAINED_GLASS;
        }

        @Override
        protected short getSubType() {
            return 3;
        }
    },
    VILLAGER_CAST{
        @Override
        public Ritual getRitual(Mana playerMana) {
            return new VillagerCast();
        }

        @Override
        protected Material getMaterial(){
            return Material.EMERALD;
        }
    },
    COOK{
        @Override
        public Ritual getRitual(Mana playerMana) {
            return new Cook(playerMana.getPlayer());
        }

        @Override
        protected Material getMaterial(){
            return Material.COOKED_BEEF;
        }
    },
    MANAUPSECOND{
        @Override
        public Ritual getRitual(Mana playerMana) {
            return new ManaUpSecond(playerMana);
        }

        @Override
        protected Material getMaterial(){
            return Material.STAINED_GLASS;
        }

        @Override
        protected int getAmount() {
            return 2;
        }

        @Override
        protected short getSubType() {
            return 3;
        }
    },
    HEALING{
        @Override
        public Ritual getRitual(Mana playerMana) {
            return new Healing(playerMana);
        }

        @Override
        protected Material getMaterial(){
            return Material.POTION;
        }

        @Override
        protected short getSubType(){
            return 8197;
        }

    },
    DIVER{
        @Override
        public Ritual getRitual(Mana playerMana) {
            return new Diver(playerMana);
        }

        @Override
        protected Material getMaterial(){
            return Material.CLAY;
        }

    },
    MINER{
        @Override
        public Ritual getRitual(Mana playerMana) {
            return new Miner(playerMana);
        }

        @Override
        protected Material getMaterial(){
            return Material.IRON_PICKAXE;
        }
    },
    TRAVELER{
        @Override
        public Ritual getRitual(Mana playerMana) {
            return new Traveler(playerMana);
        }

        @Override
        protected Material getMaterial(){
            return Material.SADDLE;
        }
    },
    WARRIOR{
        @Override
        public Ritual getRitual(Mana playerMana) {
            return new Warrior(playerMana);
        }

        @Override
        protected Material getMaterial(){
            return Material.IRON_SWORD;
        }

    },
    IMP_SUMMON{
        @Override
        public Ritual getRitual(Mana playerMana) {
            return new ImpSummon(playerMana.getPlayer());
        }

        @Override
        protected Material getMaterial() {
            return Material.NETHERRACK;
        }
    };

    private final Main plugin = Main.getPlugin();

    public abstract Ritual getRitual(Mana playerMana);

    public String getRitualName(){
        return ChatColor.GREEN + "❃ " + ChatColor.BOLD + plugin.getLangCF().getString(this.name() + ".name") + ChatColor.GREEN + " ❃";
    }

    public ItemStack getItem(){
        return standard(this.getMaterial(), this.getAmount(), this.getSubType(), this.getRitualName(), plugin.getLangCF().getStringList(this.name() + ".lore"), plugin.getConfig().getInt(this.name() + ".witchesNeed"), plugin.getConfig().getDouble(this.name() + ".needMana"));
    }

    public Research[] getNeededResearch(){
        return new Research[0];
    }

    private static ItemStack standard(Material type, int amount, short subType, String name, List<String> preLore, int witchCount, double manaNeed){
        ItemStack itemStack = new ItemStack(type, amount, subType);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        List<String> list = new ArrayList<>(preLore);
        list.add(ChatColor.BLUE + "◧ " + LangVar.i_rit.getVar() + " " + manaNeed + " ◨");
        list.add(ChatColor.DARK_PURPLE + "☄ " + LangVar.r_nwitcftr.getVar() + ": " + witchCount + " ☄");
        list.add(ChatColor.GREEN + "" + ChatColor.BOLD + "◯ " + LangVar.rb_sh.getVar() + " ◯");
        itemMeta.setLore(list);
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    protected Material getMaterial(){
        return Material.AIR;
    }

    protected int getAmount(){
        return 1;
    }

    protected short getSubType(){
        return 0;
    }

    public List<ItemStack> getReqItems(){
        return Main.getPlugin().getRitualsRecipesStorage().getRecipes().get(this.ordinal());
    }
}
