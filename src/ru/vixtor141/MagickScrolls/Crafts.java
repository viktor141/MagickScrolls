package ru.vixtor141.MagickScrolls;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Crafts {

    public enum ScrollsCrafts{
        ARROWSTORM {
            public ItemStack craftScroll(boolean check) {
                ItemStack scrollOfArrowStorm = new ItemStack(Material.PAPER);
                NamespacedKey key = new NamespacedKey(Main.getPlugin(), "scrollOfArrowStorm");
                ItemMeta meta = scrollOfArrowStorm.getItemMeta();
                meta.setDisplayName(ChatColor.DARK_PURPLE + "scroll Of Arrow Storm");
                List<String> lore = new ArrayList<>();
                lore.add("Arrow Storm scroll");
                meta.setLore(lore);
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                scrollOfArrowStorm.setItemMeta(meta);

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
                ItemStack scrollOfLightning = new ItemStack(Material.PAPER);
                NamespacedKey key = new NamespacedKey(Main.getPlugin(), "scrollOfLightning");
                ItemMeta meta = scrollOfLightning.getItemMeta();
                meta.setDisplayName(ChatColor.DARK_PURPLE + "Scroll of lightning I");
                List<String> lore = new ArrayList<>();
                lore.add("Scroll for lightning strike");
                meta.setLore(lore);
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                scrollOfLightning.setItemMeta(meta);

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
                ItemStack scrollOfLightningPowerTwo = new ItemStack(Material.PAPER);
                NamespacedKey key = new NamespacedKey(Main.getPlugin(), "scrollOfLightningPowerTwo");
                ItemMeta meta = scrollOfLightningPowerTwo.getItemMeta();
                meta.setDisplayName(ChatColor.DARK_PURPLE + "Scroll of lightning II");
                List<String> lore = new ArrayList<>();
                lore.add("Scroll for lightning strike power two");
                meta.setLore(lore);
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                scrollOfLightningPowerTwo.setItemMeta(meta);

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
                ItemStack scrollOfLightningPowerThree = new ItemStack(Material.PAPER);
                NamespacedKey key = new NamespacedKey(Main.getPlugin(), "scrollOfLightningPowerThree");
                ItemMeta meta = scrollOfLightningPowerThree.getItemMeta();
                meta.setDisplayName(ChatColor.DARK_PURPLE + "Scroll of lightning III");
                List<String> lore = new ArrayList<>();
                lore.add("Scroll for lightning strike power three");
                meta.setLore(lore);
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                scrollOfLightningPowerThree.setItemMeta(meta);

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
                ItemStack scrollOfNecromancy = new ItemStack(Material.PAPER);
                NamespacedKey key = new NamespacedKey(Main.getPlugin(), "scrollOfNecromancy");
                ItemMeta meta = scrollOfNecromancy.getItemMeta();
                meta.setDisplayName(ChatColor.DARK_PURPLE+ "Scroll Of Necromancy");
                List<String> lore = new ArrayList<>();
                lore.add("Necromancy scroll");
                meta.setLore(lore);
                meta.addEnchant(Enchantment.DURABILITY, 1,true);
                scrollOfNecromancy.setItemMeta(meta);

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
                ItemStack scrollOfTeleportation = new ItemStack(Material.PAPER);
                NamespacedKey key = new NamespacedKey(Main.getPlugin(), "scrollOfTeleportation");
                ItemMeta meta = scrollOfTeleportation.getItemMeta();
                meta.setDisplayName(ChatColor.DARK_PURPLE+ "Scroll of teleportation");
                List<String> lore = new ArrayList<>();
                lore.add("Scroll for teleportation in dimension");
                meta.setLore(lore);
                meta.addEnchant(Enchantment.DURABILITY, 1,true);
                scrollOfTeleportation.setItemMeta(meta);

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
                ItemStack scrollOfVampirism = new ItemStack(Material.PAPER);
                NamespacedKey key = new NamespacedKey(Main.getPlugin(), "scrollOfVampirism");
                ItemMeta meta = scrollOfVampirism.getItemMeta();
                meta.setDisplayName(ChatColor.DARK_PURPLE+ "Scroll of Vampirism");
                List<String> lore = new ArrayList<>();
                lore.add("Vampirism power");
                meta.setLore(lore);
                meta.addEnchant(Enchantment.DURABILITY, 1,true);
                scrollOfVampirism.setItemMeta(meta);

                scrollOfVampirism.setAmount(4);
                ShapedRecipe s = new ShapedRecipe(key, scrollOfVampirism);
                s.shape("T T"," P ","T T");
                s.setIngredient('T', Material.GHAST_TEAR);
                s.setIngredient('P', Material.PAPER);
                if(check) {
                    Bukkit.getServer().addRecipe(s);
                }
                return scrollOfVampirism;
            }
        },
        VORTEX{
            public ItemStack craftScroll(boolean check){
                ItemStack scrollOfVortex = new ItemStack(Material.PAPER);
                NamespacedKey key = new NamespacedKey(Main.getPlugin(), "scrollOfVortex");
                ItemMeta meta = scrollOfVortex.getItemMeta();
                meta.setDisplayName(ChatColor.DARK_PURPLE+ "Scroll of vortex");
                List<String> lore = new ArrayList<>();
                lore.add("Vortex scroll");
                meta.setLore(lore);
                meta.addEnchant(Enchantment.DURABILITY, 1,true);
                scrollOfVortex.setItemMeta(meta);

                scrollOfVortex.setAmount(4);
                ShapedRecipe s = new ShapedRecipe(key, scrollOfVortex);
                s.shape("PBP","GSG","PFP");
                s.setIngredient('S', Material.SHULKER_SHELL);
                s.setIngredient('B', Material.BLAZE_POWDER);
                s.setIngredient('G', Material.GLOWSTONE_DUST);
                s.setIngredient('F', Material.FEATHER);
                s.setIngredient('P', Material.PAPER);
                if(check) {
                    Bukkit.getServer().addRecipe(s);
                }
                return scrollOfVortex;
            }
        };

        public abstract ItemStack craftScroll(boolean check);
    }
}
