package ru.vixtor141.MagickScrolls;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Crafts {
    private static FileConfiguration langF = Main.readingLangFile.getLang();

    public enum ScrollsCrafts{
        ARROWSTORM {
            public ItemStack craftScroll(boolean check) {
                ItemStack scrollOfArrowStorm = setItemMeta(langF.getString("ARROWSTORM_name"), langF.getStringList("ARROWSTORM_lore"));
                NamespacedKey key = new NamespacedKey(Main.getPlugin(), "scrollOfArrowStorm");

                scrollOfArrowStorm.setAmount(4);
                ShapedRecipe s = new ShapedRecipe(key, scrollOfArrowStorm);
                s.shape("PAP", "ANA", "PAP");
                s.setIngredient('A', Material.ARROW);
                s.setIngredient('N', Material.NETHER_STAR);
                s.setIngredient('P', Material.PAPER);
                if(check) {
                    Bukkit.getServer().addRecipe(s);
                }
                return scrollOfArrowStorm;
            }
        },
        LIGHTNINGONE {
            public ItemStack craftScroll(boolean check) {
                ItemStack scrollOfLightning = setItemMeta(langF.getString("LIGHTNINGONE_name"), langF.getStringList("LIGHTNINGONE_lore"));
                NamespacedKey key = new NamespacedKey(Main.getPlugin(), "scrollOfLightning");

                scrollOfLightning.setAmount(8);
                ShapedRecipe s = new ShapedRecipe(key, scrollOfLightning);
                s.shape("PPP", "PNP", "PPP");
                s.setIngredient('N', Material.NETHER_STAR);
                s.setIngredient('P', Material.PAPER);
                if(check) {
                    Bukkit.getServer().addRecipe(s);
                }
                return scrollOfLightning;
            }
        },
        LIGHTNINGTWO {
            public ItemStack craftScroll(boolean check) {
                ItemStack scrollOfLightningPowerTwo = setItemMeta(langF.getString("LIGHTNINGTWO_name"), langF.getStringList("LIGHTNINGTWO_lore"));
                NamespacedKey key = new NamespacedKey(Main.getPlugin(), "scrollOfLightningPowerTwo");

                scrollOfLightningPowerTwo.setAmount(8);
                ShapedRecipe s = new ShapedRecipe(key, scrollOfLightningPowerTwo);
                s.shape("PNP", "PNP", "PNP");
                s.setIngredient('N', Material.NETHER_STAR);
                s.setIngredient('P', Material.PAPER);
                if(check) {
                    Bukkit.getServer().addRecipe(s);
                }
                return scrollOfLightningPowerTwo;
            }
        },
        LIGHTNINGTHREE {
            public ItemStack craftScroll(boolean check) {
                ItemStack scrollOfLightningPowerThree = setItemMeta(langF.getString("LIGHTNINGTHREE_name"), langF.getStringList("LIGHTNINGTHREE_lore"));
                NamespacedKey key = new NamespacedKey(Main.getPlugin(), "scrollOfLightningPowerThree");

                scrollOfLightningPowerThree.setAmount(8);
                ShapedRecipe s = new ShapedRecipe(key, scrollOfLightningPowerThree);
                s.shape("PNP", "NNN", "PNP");
                s.setIngredient('N', Material.NETHER_STAR);
                s.setIngredient('P', Material.PAPER);
                if(check) {
                    Bukkit.getServer().addRecipe(s);
                }
                return scrollOfLightningPowerThree;
            }
        },
        NECROMANCY{
            public ItemStack craftScroll(boolean check){
                ItemStack scrollOfNecromancy = setItemMeta(langF.getString("NECROMANCY_name"), langF.getStringList("NECROMANCY_lore"));
                NamespacedKey key = new NamespacedKey(Main.getPlugin(), "scrollOfNecromancy");

                scrollOfNecromancy.setAmount(4);
                ShapedRecipe s = new ShapedRecipe(key, scrollOfNecromancy);
                s.shape("PRP","BNS","PGP");
                s.setIngredient('R', Material.ROTTEN_FLESH);
                s.setIngredient('B', Material.BONE);
                s.setIngredient('S', Material.SPIDER_EYE);
                s.setIngredient('G', Material.SULPHUR);
                s.setIngredient('N', Material.NETHER_STAR);
                s.setIngredient('P', Material.PAPER);
                if(check) {
                    Bukkit.getServer().addRecipe(s);
                }
                return scrollOfNecromancy;
            }
        },
        TELEPORTATION{
            public ItemStack craftScroll(boolean check){
                ItemStack scrollOfTeleportation = setItemMeta(langF.getString("TELEPORTATION_name"), langF.getStringList("TELEPORTATION_lore"));
                NamespacedKey key = new NamespacedKey(Main.getPlugin(), "scrollOfTeleportation");

                scrollOfTeleportation.setAmount(4);
                ShapedRecipe s = new ShapedRecipe(key, scrollOfTeleportation);
                s.shape("EPE","P P","EPE");
                s.setIngredient('E', Material.ENDER_PEARL);
                s.setIngredient('P', Material.PAPER);
                if(check) {
                    Bukkit.getServer().addRecipe(s);
                }
                return scrollOfTeleportation;
            }
        },
        VAMPIRIC{
            public ItemStack craftScroll(boolean check){
                ItemStack itemStack = setItemMeta(langF.getString("VAMPIRIC_name"), langF.getStringList("VAMPIRIC_lore"));
                NamespacedKey key = new NamespacedKey(Main.getPlugin(), "scrollOfVampirism");

                itemStack.setAmount(4);
                ShapedRecipe s = new ShapedRecipe(key, itemStack);
                s.shape("T T"," P ","T T");
                s.setIngredient('T', Material.GHAST_TEAR);
                s.setIngredient('P', Material.PAPER);
                if(check) {
                    Bukkit.getServer().addRecipe(s);
                }
                return itemStack;
            }
        },
        VORTEX{
            public ItemStack craftScroll(boolean check){
                ItemStack itemStack = setItemMeta(langF.getString("VORTEX_name"), langF.getStringList("VORTEX_lore"));
                NamespacedKey key = new NamespacedKey(Main.getPlugin(), "scrollOfVortex");

                itemStack.setAmount(4);
                ShapedRecipe s = new ShapedRecipe(key, itemStack);
                s.shape("PBP","GSG","PFP");
                s.setIngredient('S', Material.SHULKER_SHELL);
                s.setIngredient('B', Material.BLAZE_POWDER);
                s.setIngredient('G', Material.GLOWSTONE_DUST);
                s.setIngredient('F', Material.FEATHER);
                s.setIngredient('P', Material.PAPER);
                if(check) {
                    Bukkit.getServer().addRecipe(s);
                }
                return itemStack;
            }
        };

        public abstract ItemStack craftScroll(boolean check);
    }

    private static ItemStack setItemMeta(String displayName, List<String> lore){
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE + displayName);
        meta.setLore(lore);
        meta.addEnchant(Enchantment.DURABILITY, 1,true);
        item.setItemMeta(meta);
        return item;
    }
}

