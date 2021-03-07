package ru.vixtor141.MagickScrolls.Misc;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.research.*;

import java.util.ArrayList;
import java.util.List;

import static ru.vixtor141.MagickScrolls.research.Research.*;

public enum Menu {
    MANA_SHIELD_LEVELS(Material.SHIELD, 47, new Research[]{MANA_SHIELD_II}){
        @Override
        public void use(PlayerResearch playerResearch){
            playerResearch.getShieldManaLevels().openInventory();
        }
    },
    MAGIC_INVENTORY(Material.KNOWLEDGE_BOOK, 0, new Research[]{BASIC_RESEARCH}){
        @Override
        public void use(PlayerResearch playerResearch){
            playerResearch.getAccessoriesInventory().open();
        }
    },
    ASPECTS(Material.GLASS, 8, new Research[]{BASIC_RESEARCH}){
        @Override
        public void use(PlayerResearch playerResearch){
            playerResearch.getPlayerMana().getPlayerAspectsStorage().openAspects();
        }
    };

    private final Material material;
    private final int position;
    private final Research[] neededResearches;

    public abstract void use(PlayerResearch playerResearch);

    Menu(Material material, int position){
        this(material, position, new Research[0]);
    }

    Menu(Material material, int position, Research[] neededResearches){
        this.material = material;
        this.position = position;
        this.neededResearches = neededResearches;
    }

    public int getPosition(){
        return position;
    }

    public Material getMaterialType(){
        return material;
    }

    private static String standardName(String string){
        return ChatColor.YELLOW + "" + ChatColor.BOLD + getNameString(string);
    }

    public ItemStack getItem(){
        return standard(this.getMaterialType(), standardName(this.name()), new ArrayList<>(getLore(this.name())));
    }

    public Research[] getNeededResearch(){
        return neededResearches;
    }

    private static ItemStack standard(Material type, String name, List<String> lore){
        ItemStack itemStack = new ItemStack(type);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        for(int i = 0; i< lore.size(); i++){
            String s = lore.get(i);
            lore.set(i,ChatColor.BLUE + "" + s);
        }
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private static String getNameString(String string){
        return Main.getPlugin().getLangCF().getString(string + ".name");
    }

    private static List<String> getLore(String string){
        return Main.getPlugin().getLangCF().getStringList(string + ".lore");
    }
}
