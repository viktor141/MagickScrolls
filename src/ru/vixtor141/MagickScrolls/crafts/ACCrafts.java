package ru.vixtor141.MagickScrolls.crafts;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.interfaces.Scroll;
import ru.vixtor141.MagickScrolls.scrolls.*;

import java.util.ArrayList;
import java.util.List;

public class ACCrafts {

    private final static FileConfiguration langF = Main.getPlugin().getLangCF();

    public enum ItemsCauldronCrafts{
        MAGIC_STAFF{
            public Material craftCauldronGetMaterial(){
                return Material.STICK;
            }

            public CraftsOfScrolls[] getScroll(){
                return new CraftsOfScrolls[0];
            }

        },
        ARROWSTORM_MAGICSEAL{
            public Material craftCauldronGetMaterial(){
                return Material.ARROW;
            }

            public CraftsOfScrolls[] getScroll(){
                return new CraftsOfScrolls[]{CraftsOfScrolls.ARROWSTORM};
            }
        },
        LIGHTNING_MAGICSEAL{
            public Material craftCauldronGetMaterial(){
                return Material.NETHER_STAR;
            }

            public CraftsOfScrolls[] getScroll(){
                return new CraftsOfScrolls[]{CraftsOfScrolls.LIGHTNINGONE, CraftsOfScrolls.LIGHTNINGTWO, CraftsOfScrolls.LIGHTNINGTHREE};
            }
        },
        NECROMANCY_MAGICSEAL{
            public Material craftCauldronGetMaterial(){
                return Material.BONE;
            }

            public CraftsOfScrolls[] getScroll(){
                return new CraftsOfScrolls[]{CraftsOfScrolls.NECROMANCY};
            }
        },
        TELEPORTATION_MAGICSEAL{
            public Material craftCauldronGetMaterial(){
                return Material.ENDER_PEARL;
            }

            public CraftsOfScrolls[] getScroll(){
                return new CraftsOfScrolls[]{CraftsOfScrolls.TELEPORTATION};
            }
        },
        VAMPIRIC_MAGICSEAL{
            public Material craftCauldronGetMaterial(){
                return Material.GHAST_TEAR;
            }

            public CraftsOfScrolls[] getScroll(){
                return new CraftsOfScrolls[]{CraftsOfScrolls.VAMPIRIC};
            }
        },
        VORTEX_MAGICSEAL{
            public Material craftCauldronGetMaterial(){
                return Material.DIAMOND;
            }

            public CraftsOfScrolls[] getScroll(){
                return new CraftsOfScrolls[]{CraftsOfScrolls.VORTEX};
            }
        },
        SPIDERWEB_MAGICSEAL{
            public Material craftCauldronGetMaterial(){
                return Material.SPIDER_EYE;
            }

            public CraftsOfScrolls[] getScroll(){
                return new CraftsOfScrolls[]{CraftsOfScrolls.SPIDERWEB};
            }
        },
        TRAP_MAGICSEAL{
            public Material craftCauldronGetMaterial(){
                return Material.EYE_OF_ENDER;
            }

            public CraftsOfScrolls[] getScroll(){
                return new CraftsOfScrolls[]{CraftsOfScrolls.TRAP};
            }
        },
        EARTH_MAGICSEAL{
            public Material craftCauldronGetMaterial(){
                return Material.DIRT;
            }

            public CraftsOfScrolls[] getScroll(){
                return new CraftsOfScrolls[]{CraftsOfScrolls.EARTH};
            }
        },
        ASTRAL_PET_MAGICSEAL{
            public Material craftCauldronGetMaterial(){
                return Material.SHULKER_SHELL;
            }

            public CraftsOfScrolls[] getScroll(){
                return new CraftsOfScrolls[]{CraftsOfScrolls.ASTRAL_PET};
            }
        },
        METEORITE_MAGICSEAL{
            public Material craftCauldronGetMaterial(){
                return Material.NETHERRACK;
            }

            public CraftsOfScrolls[] getScroll(){
                return new CraftsOfScrolls[]{CraftsOfScrolls.METEORITE};
            }
        },
        WITCH_ARTIFACT{
            public Material craftCauldronGetMaterial() {
                return Material.EMERALD;
            }

            public CraftsOfScrolls[] getScroll() {
                return new CraftsOfScrolls[0];
            }

            @Override
            public ItemStack craftCauldronGetItem(){
                ItemStack item = new ItemStack(this.craftCauldronGetMaterial());
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.DARK_PURPLE + langF.getString(this.name() + ".name"));
                List<String> loreL = new ArrayList<String>(langF.getStringList(this.name() + ".lore"));
                loreL.add("0");
                loreL.add(ChatColor.BLUE + this.name());
                loreL.add(ChatColor.YELLOW + "Magick Scrolls");
                meta.setLore(loreL);
                meta.addEnchant(Enchantment.DURABILITY, 1,true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                item.setItemMeta(meta);

                return item;
            }
        },
        RITUAL_BOOK{
            public Material craftCauldronGetMaterial() {
                return Material.BOOK;
            }

            public CraftsOfScrolls[] getScroll() {
                return new CraftsOfScrolls[0];
            }

            @Override
            public ItemStack craftCauldronGetItem(){
                return Main.getPlugin().getRitualBook();
            }
        },
        RITUAL_ALTAR_BUILDER{
            public Material craftCauldronGetMaterial() {
                return Material.BLAZE_ROD;
            }

            public CraftsOfScrolls[] getScroll() {
                return new CraftsOfScrolls[0];
            }
        },
        RITUAL_BOOK_REWRITER{
            public Material craftCauldronGetMaterial() {
                return Material.WRITTEN_BOOK;
            }

            public CraftsOfScrolls[] getScroll() {
                return new CraftsOfScrolls[0];
            }

            @Override
            public ItemStack craftCauldronGetItem(){
                return Main.getPlugin().getRitualBook();
            }
        };

        public abstract Material craftCauldronGetMaterial();
        public abstract CraftsOfScrolls[] getScroll();

        public ItemStack craftCauldronGetItem(){
            return setItemMeta(langF.getString(this.name() + ".name"), langF.getStringList( this.name() +".lore"), this.name(), this.craftCauldronGetMaterial());
        }

    }



    public enum CraftsOfScrolls{
        ARROWSTORM {
            @Override
            public void start(Player player, ItemStack item) {new ArrowStorm(player, item);}
        },
        LIGHTNINGONE {
            @Override
            public void start(Player player, ItemStack item) {new Lightning(player, item, Lightning.Type.ONE);}
        },
        LIGHTNINGTWO {
            @Override
            public void start(Player player, ItemStack item) {new Lightning(player, item, Lightning.Type.TWO);}
        },
        LIGHTNINGTHREE {
            @Override
            public void start(Player player, ItemStack item) {new Lightning(player, item, Lightning.Type.THREE);}
        },
        NECROMANCY{
            @Override
            public void start(Player player, ItemStack item) {new Necromancy(player, item);}
        },
        TELEPORTATION{
            @Override
            public void start(Player player, ItemStack item) {new Teleportation(player, item);}
        },
        VAMPIRIC{
            @Override
            public void start(Player player, ItemStack item) {}
        },
        VORTEX{
            @Override
            public void start(Player player, ItemStack item) {new Vortex(player, item);}
        },
        SPIDERWEB{
            @Override
            public void start(Player player, ItemStack item) {new SpiderWeb(player, item);}
        },
        TRAP{
            @Override
            public void start(Player player, ItemStack item) {new Trap(player, item);}
        },
        EARTH{
            @Override
            public void start(Player player, ItemStack item) {new Earth(player, item);}
        },
        ASTRAL_PET{
            @Override
            public void start(Player player, ItemStack item) {new AstralPet(player, item); }
        },
        METEORITE{
            @Override
            public void start(Player player, ItemStack item) {new Meteorite(player, item);}
        };

        public abstract void start(Player player, ItemStack item);

        public ItemStack craftAltarResult(){
            return setItemMeta(langF.getString(this.name() + ".name"), langF.getStringList(this.name() + ".lore"), this.name(), Material.PAPER);
        }
    }

    private static ItemStack setItemMeta(String displayName, List<String> lore, String uName, Material material){
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE + displayName);
        List<String> loreL = new ArrayList<String>(lore);
        loreL.add(ChatColor.BLUE + uName);
        loreL.add(ChatColor.YELLOW + "Magick Scrolls");
        meta.setLore(loreL);
        meta.addEnchant(Enchantment.DURABILITY, 1,true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);

        return item;
    }
}
