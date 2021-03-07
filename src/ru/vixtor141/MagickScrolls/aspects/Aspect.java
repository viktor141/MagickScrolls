package ru.vixtor141.MagickScrolls.aspects;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.research.Research;
import ru.vixtor141.MagickScrolls.utils.HeadGetter;

import static ru.vixtor141.MagickScrolls.Misc.CheckUp.getPlayerMana;

@SuppressWarnings("SpellCheckingInspection")
public enum Aspect {
    Experientia(new ItemStack(Material.EXP_BOTTLE)), //Experience
    Mortem(new ItemStack(Material.SKULL_ITEM)), //Death
    Conscientia(new ItemStack(Material.BOOK)), //Mind
    Cerebrum(new HeadGetter("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmRkM2IwMWVjMzJmM2Y1ZWEyNmFkNGUyMzMwNTQwMWY3M2QxM2YzYzE2YjNmYWNlZmU3MGQ5MjE3NmIzMTEifX19").getHead()), //Brain
    Adtonitus(new ItemStack(Material.NETHER_STAR)), //lightning/thunder-struck
    Penna(new ItemStack(Material.FEATHER)), //Feather
    Aqua(new ItemStack(Material.WATER_BUCKET)), //Water
    Terra(new ItemStack(Material.GRASS)), //Ground
    Aer(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 8)), //Air
    Ignis(new ItemStack(Material.MAGMA_CREAM)), //Fire
    Metallum(new ItemStack(Material.IRON_INGOT)), //Metal
    Herba(new ItemStack(Material.LEAVES)), //Plant
    Sancto(new ItemStack(Material.GHAST_TEAR)), //Spirit
    Lignum(new ItemStack(Material.WOOD)), //Wood
    Veneficus(new ItemStack(Material.BLAZE_POWDER)), //Magic
    Bestia(new HeadGetter("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjgzYTJhYTlkMzczNGI5MTlhYzI0Yzk2NTllNWUwZjg2ZWNhZmJmNjRkNDc4OGNmYTQzM2JiZWMxODllOCJ9fX0").getHead()), //Beast
    Humanus(new ItemStack(Material.SKULL_ITEM, 1, (short) 3)), //Human
    Tenebras(new ItemStack(Material.FIREWORK_CHARGE)), //Darkness
    Venenum(new ItemStack(Material.RAW_FISH, 1, (short) 3)), //Poison
    Instrumentum(new ItemStack(Material.IRON_PICKAXE)), //Instrument
    Messis(new ItemStack(Material.WHEAT)), //Harvest
    Cibus(new ItemStack(Material.APPLE)), //Food
    Captionem(new ItemStack(Material.TRIPWIRE_HOOK)), //Trap
    Infecta(new ItemStack(Material.ROTTEN_FLESH)), //Infected
    Conruptus(new ItemStack(Material.SPIDER_EYE)), //Taint
    Aureus(new ItemStack(Material.GOLDEN_APPLE)), //Golden
    Tempus(new ItemStack(Material.SAND)), //Time
    Stultitia(new ItemStack(Material.DIAMOND_HOE)), //Stupidity, Incompetence
    Magneto(new ItemStack(Material.HOPPER)), //Magnetism, Attraction
    Splendens(new ItemStack(Material.GLOWSTONE_DUST)), //Shining
    Sanctus(new ItemStack(Material.EMERALD)), //Holy
    Enucleatus(new ItemStack(Material.DIRT)), //Easy
    Moles(new ItemStack(Material.DIAMOND)), //Heavy
    Mirum(new ItemStack(Material.GOLDEN_APPLE, 1, (short) 1)), //Amazing
    Ridiculum(new ItemStack(Material.CHORUS_FLOWER)), //Weird
    Vetustus(new ItemStack(Material.MOSSY_COBBLESTONE)), //Ancient
    Alchemiae(new ItemStack(Material.CAULDRON_ITEM)), //Alchemy
    Corium(new ItemStack(Material.CHEST)), //peel/rind/shell/outer cover
    Custodia(new ItemStack(Material.SHIELD));//Protection

    private final ItemStack itemStack;

    Aspect(ItemStack itemStack){
        this.itemStack = itemStack;
    }

    public ItemStack getItem(){
        return  itemStack;
    }

    public void aspectAdd(Player player, int count){
        Mana playerMana = getPlayerMana(player);
        if(playerMana.getPlayerResearch().getResearches().get(Research.BASIC_RESEARCH.ordinal()))
        playerMana.getPlayerAspectsStorage().addAspect(this, count);
    }
}