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
import ru.vixtor141.MagickScrolls.artifacts.AncientBottle;
import ru.vixtor141.MagickScrolls.research.Research;
import ru.vixtor141.MagickScrolls.scrolls.*;

import java.util.ArrayList;
import java.util.List;

public class ACCrafts {

    private final static FileConfiguration langF = Main.getPlugin().getLangCF();

    public enum ItemsCauldronCrafts{
        MAGIC_STAFF{
        },
        ARROWSTORM_MAGICSEAL{
            @Override
            public CraftsOfScrolls[] getScroll(){
                return new CraftsOfScrolls[]{CraftsOfScrolls.ARROWSTORM};
            }
        },
        LIGHTNING_MAGICSEAL{
            @Override
            public CraftsOfScrolls[] getScroll(){
                return new CraftsOfScrolls[]{CraftsOfScrolls.LIGHTNINGONE, CraftsOfScrolls.LIGHTNINGTWO, CraftsOfScrolls.LIGHTNINGTHREE};
            }
        },
        NECROMANCY_MAGICSEAL{
            @Override
            public CraftsOfScrolls[] getScroll(){
                return new CraftsOfScrolls[]{CraftsOfScrolls.NECROMANCY};
            }
        },
        TELEPORTATION_MAGICSEAL{
            @Override
            public CraftsOfScrolls[] getScroll(){
                return new CraftsOfScrolls[]{CraftsOfScrolls.TELEPORTATION};
            }
        },
        VAMPIRIC_MAGICSEAL{
            @Override
            public CraftsOfScrolls[] getScroll(){
                return new CraftsOfScrolls[]{CraftsOfScrolls.VAMPIRIC};
            }
        },
        VORTEX_MAGICSEAL{
            @Override
            public CraftsOfScrolls[] getScroll(){
                return new CraftsOfScrolls[]{CraftsOfScrolls.VORTEX};
            }
        },
        SPIDERWEB_MAGICSEAL{
            @Override
            public CraftsOfScrolls[] getScroll(){
                return new CraftsOfScrolls[]{CraftsOfScrolls.SPIDERWEB};
            }
        },
        TRAP_MAGICSEAL{
            @Override
            public CraftsOfScrolls[] getScroll(){
                return new CraftsOfScrolls[]{CraftsOfScrolls.TRAP};
            }
        },
        EARTH_MAGICSEAL{
            @Override
            public CraftsOfScrolls[] getScroll(){
                return new CraftsOfScrolls[]{CraftsOfScrolls.EARTH};
            }
        },
        ASTRAL_PET_MAGICSEAL{
            @Override
            public CraftsOfScrolls[] getScroll(){
                return new CraftsOfScrolls[]{CraftsOfScrolls.ASTRAL_PET};
            }
        },
        METEORITE_MAGICSEAL{
            @Override
            public CraftsOfScrolls[] getScroll(){
                return new CraftsOfScrolls[]{CraftsOfScrolls.METEORITE};
            }
        },
        SPECTRAL_SHIELD_MAGICSEAL{
            @Override
            public CraftsOfScrolls[] getScroll(){
                return new CraftsOfScrolls[]{CraftsOfScrolls.SPECTRAL_SHIELD};
            }
        },
        WITCH_ARTIFACT{
            @Override
            public ItemStack craftCauldronGetItem(){
                ItemStack item = Main.getPlugin().getCauldronCF().getItemStack(this.name() + ".result");
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
            @Override
            public ItemStack craftCauldronGetItem(){
                return Main.getPlugin().getRitualBook();
            }
        },
        RITUAL_ALTAR_BUILDER{
        },
        RITUAL_BOOK_REWRITER{
            @Override
            public ItemStack craftCauldronGetItem(){
                return Main.getPlugin().getRitualBook();
            }
        },
        AIR_TRAP_MAGICSEAL{
            @Override
            public CraftsOfScrolls[] getScroll(){
                return new CraftsOfScrolls[]{CraftsOfScrolls.AIR_TRAP};
            }
        },
        CONFUSED_MAGICSEAL{
            @Override
            public CraftsOfScrolls[] getScroll(){
                return new CraftsOfScrolls[]{CraftsOfScrolls.CONFUSED};
            }
        },
        ANCIENT_BOTTLE{
            @Override
            public Research[] getResearch() {
                return new Research[]{Research.ANCIENT_BOTTLE};
            }

            @Override
            public ItemStack craftCauldronGetItem(){
                ItemStack item = Main.getPlugin().getCauldronCF().getItemStack(this.name() + ".result");
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.DARK_PURPLE + langF.getString(this.name() + ".name"));
                List<String> loreL = new ArrayList<String>(langF.getStringList(this.name() + ".lore"));
                loreL.add(ChatColor.BLUE + "0");
                loreL.add(ChatColor.BLUE + this.name());
                loreL.add(ChatColor.YELLOW + "Magick Scrolls");
                meta.setLore(loreL);
                meta.addEnchant(Enchantment.DURABILITY, 1,true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                item.setItemMeta(meta);

                return item;
            }
        };

        public CraftsOfScrolls[] getScroll(){return new CraftsOfScrolls[0];}

        public Research[] getResearch(){return new Research[0];}

        public Material craftCauldronGetMaterial(){
            return Main.getPlugin().getCauldronCF().getItemStack(this.name() + ".result").getType();
        }

        public ItemStack craftCauldronGetItem(){
            ItemStack item = Main.getPlugin().getCauldronCF().getItemStack(this.name() + ".result");
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.DARK_PURPLE + langF.getString(this.name() + ".name"));
            List<String> loreL = new ArrayList<String>(langF.getStringList( this.name() +".lore"));
            loreL.add(ChatColor.BLUE + this.name());
            loreL.add(ChatColor.YELLOW + "Magick Scrolls");
            meta.setLore(loreL);
            meta.addEnchant(Enchantment.DURABILITY, 1,true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
            return item;
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
        },
        SPECTRAL_SHIELD{
            @Override
            public void start(Player player, ItemStack item) {new SpectralShield(player, item);}
        },
        AIR_TRAP{
            @Override
            public void start(Player player, ItemStack item) {new AirTrap(player, item);}
        },
        CONFUSED{
            @Override
            public void start(Player player, ItemStack item) {new Confused(player, item);}
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

    public enum Artifacts{
        ANCIENT_BOTTLE{
            @Override
            public void start(Player player, ItemStack item) {new AncientBottle(player, item);}
        };
        public abstract void start(Player player, ItemStack item);
    }
}
