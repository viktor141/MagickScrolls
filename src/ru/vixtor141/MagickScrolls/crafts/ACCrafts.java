package ru.vixtor141.MagickScrolls.crafts;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.vixtor141.MagickScrolls.Main;

import java.util.ArrayList;
import java.util.List;

public class ACCrafts {

    private final static FileConfiguration langF = Main.getPlugin().getReadingLangFile().getLang();

    public enum ItemsCauldronCrafts{
        MAGIC_STAFF{
            public Material craftCauldronGetMaterial(){
                return Material.STICK;
            }

            public ItemStack craftCauldronGetItem(){
                return setItemMeta(langF.getString("MAGIC_STAFF.name"), langF.getStringList("MAGIC_STAFF.lore"), "MAGIC_STAFF", this.craftCauldronGetMaterial());
            }

            public CraftsOfScrolls[] getScroll(){
                return null;
            }

        },
        ARROWSTORM_MAGICSEAL{
            public Material craftCauldronGetMaterial(){
                return Material.ARROW;
            }

            public ItemStack craftCauldronGetItem(){
                return setItemMeta(langF.getString("ARROWSTORM_MAGICSEAL.name"), langF.getStringList("ARROWSTORM_MAGICSEAL.lore"), "ARROWSTORM_MAGICSEAL", this.craftCauldronGetMaterial());
            }

            public CraftsOfScrolls[] getScroll(){
                CraftsOfScrolls[] craftsOfScrolls = new CraftsOfScrolls[1];
                craftsOfScrolls[0] = CraftsOfScrolls.ARROWSTORM;
                return craftsOfScrolls;
            }
        },
        LIGHTNING_MAGICSEAL{
            public Material craftCauldronGetMaterial(){
                return Material.NETHER_STAR;
            }

            public ItemStack craftCauldronGetItem(){
                return setItemMeta(langF.getString("LIGHTNING_MAGICSEAL.name"), langF.getStringList("LIGHTNING_MAGICSEAL.lore"), "LIGHTNING_MAGICSEAL", this.craftCauldronGetMaterial());
            }

            public CraftsOfScrolls[] getScroll(){
                CraftsOfScrolls[] craftsOfScrolls = new CraftsOfScrolls[3];
                craftsOfScrolls[0] = CraftsOfScrolls.LIGHTNINGONE;
                craftsOfScrolls[1] = CraftsOfScrolls.LIGHTNINGTWO;
                craftsOfScrolls[2] = CraftsOfScrolls.LIGHTNINGTHREE;
                return craftsOfScrolls;
            }
        },
        NECROMANCY_MAGICSEAL{
            public Material craftCauldronGetMaterial(){
                return Material.BONE;
            }

            public ItemStack craftCauldronGetItem(){
                return setItemMeta(langF.getString("NECROMANCY_MAGICSEAL.name"), langF.getStringList("NECROMANCY_MAGICSEAL.lore"), "NECROMANCY_MAGICSEAL", this.craftCauldronGetMaterial());
            }

            public CraftsOfScrolls[] getScroll(){
                CraftsOfScrolls[] craftsOfScrolls = new CraftsOfScrolls[1];
                craftsOfScrolls[0] = CraftsOfScrolls.NECROMANCY;
                return craftsOfScrolls;
            }
        },
        TELEPORTATION_MAGICSEAL{
            public Material craftCauldronGetMaterial(){
                return Material.ENDER_PEARL;
            }

            public ItemStack craftCauldronGetItem(){
                return setItemMeta(langF.getString("TELEPORTATION_MAGICSEAL.name"), langF.getStringList("TELEPORTATION_MAGICSEAL.lore"), "TELEPORTATION_MAGICSEAL", this.craftCauldronGetMaterial());
            }

            public CraftsOfScrolls[] getScroll(){
                CraftsOfScrolls[] craftsOfScrolls = new CraftsOfScrolls[1];
                craftsOfScrolls[0] = CraftsOfScrolls.TELEPORTATION;
                return craftsOfScrolls;
            }
        },
        VAMPIRIC_MAGICSEAL{
            public Material craftCauldronGetMaterial(){
                return Material.GHAST_TEAR;
            }

            public ItemStack craftCauldronGetItem(){
                return setItemMeta(langF.getString("VAMPIRIC_MAGICSEAL.name"), langF.getStringList("VAMPIRIC_MAGICSEAL.lore"), "VAMPIRIC_MAGICSEAL", this.craftCauldronGetMaterial());
            }

            public CraftsOfScrolls[] getScroll(){
                CraftsOfScrolls[] craftsOfScrolls = new CraftsOfScrolls[1];
                craftsOfScrolls[0] = CraftsOfScrolls.VAMPIRIC;
                return craftsOfScrolls;
            }
        },
        VORTEX_MAGICSEAL{
            public Material craftCauldronGetMaterial(){
                return Material.DIAMOND;
            }

            public ItemStack craftCauldronGetItem(){
                return setItemMeta(langF.getString("VORTEX_MAGICSEAL.name"), langF.getStringList("VORTEX_MAGICSEAL.lore"), "VORTEX_MAGICSEAL", this.craftCauldronGetMaterial());
            }

            public CraftsOfScrolls[] getScroll(){
                CraftsOfScrolls[] craftsOfScrolls = new CraftsOfScrolls[1];
                craftsOfScrolls[0] = CraftsOfScrolls.VORTEX;
                return craftsOfScrolls;
            }
        },
        SPIDERWEB_MAGICSEAL{
            public Material craftCauldronGetMaterial(){
                return Material.SPIDER_EYE;
            }

            public ItemStack craftCauldronGetItem(){
                return setItemMeta(langF.getString("SPIDERWEB_MAGICSEAL.name"), langF.getStringList("SPIDERWEB_MAGICSEAL.lore"), "SPIDERWEB_MAGICSEAL", this.craftCauldronGetMaterial());
            }

            public CraftsOfScrolls[] getScroll(){
                CraftsOfScrolls[] craftsOfScrolls = new CraftsOfScrolls[1];
                craftsOfScrolls[0] = CraftsOfScrolls.SPIDERWEB;
                return craftsOfScrolls;
            }
        };

        public abstract Material craftCauldronGetMaterial();
        public abstract ItemStack craftCauldronGetItem();
        public abstract CraftsOfScrolls[] getScroll();

    }



    public enum CraftsOfScrolls{
        ARROWSTORM {

            public ItemStack craftAltarResult(){
                return setItemMeta(langF.getString("ARROWSTORM.name"), langF.getStringList("ARROWSTORM.lore"), "ARROWSTORM", Material.PAPER);
            }

        },
        LIGHTNINGONE {

            public ItemStack craftAltarResult(){
                return setItemMeta(langF.getString("LIGHTNINGONE.name"), langF.getStringList("LIGHTNINGONE.lore"), "LIGHTNINGONE", Material.PAPER);
            }

        },
        LIGHTNINGTWO {

            public ItemStack craftAltarResult(){
                return setItemMeta(langF.getString("LIGHTNINGTWO.name"), langF.getStringList("LIGHTNINGTWO.lore"), "LIGHTNINGTWO", Material.PAPER);
            }
        },
        LIGHTNINGTHREE {

            public ItemStack craftAltarResult(){
                return setItemMeta(langF.getString("LIGHTNINGTHREE.name"), langF.getStringList("LIGHTNINGTHREE.lore"), "LIGHTNINGTHREE", Material.PAPER);
            }
        },
        NECROMANCY{

            public ItemStack craftAltarResult(){
                return setItemMeta(langF.getString("NECROMANCY.name"), langF.getStringList("NECROMANCY.lore"), "NECROMANCY", Material.PAPER);
            }
        },
        TELEPORTATION{

            public ItemStack craftAltarResult(){
                return setItemMeta(langF.getString("TELEPORTATION.name"), langF.getStringList("TELEPORTATION.lore"), "TELEPORTATION", Material.PAPER);
            }
        },
        VAMPIRIC{

            public ItemStack craftAltarResult(){
                return setItemMeta(langF.getString("VAMPIRIC.name"), langF.getStringList("VAMPIRIC.lore"), "VAMPIRIC", Material.PAPER);
            }
        },
        VORTEX{

            public ItemStack craftAltarResult(){
                return setItemMeta(langF.getString("VORTEX.name"), langF.getStringList("VORTEX.lore"), "VORTEX", Material.PAPER);
            }
        },
        SPIDERWEB{

            public ItemStack craftAltarResult(){
                return setItemMeta(langF.getString("SPIDERWEB.name"), langF.getStringList("SPIDERWEB.lore"), "SPIDERWEB", Material.PAPER);
            }
        };

        public abstract ItemStack craftAltarResult();
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
