package ru.vixtor141.MagickScrolls.research;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.aspects.Aspect;
import ru.vixtor141.MagickScrolls.aspects.PlayerAspectStorage;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;
import ru.vixtor141.MagickScrolls.lang.LangVar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Research {
        BASIC_RESEARCH(ACCrafts.ItemsCauldronCrafts.RITUAL_BOOK.craftCauldronGetItem().getType(), 22){
        @Override
        public ItemStack getResearchItem(boolean flag){
            ItemStack itemStack = ACCrafts.ItemsCauldronCrafts.RITUAL_BOOK.craftCauldronGetItem().clone();
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(standardName(this.name()));
            String s;
            if(flag){
                s = ChatColor.GREEN + "✔";
            }else {
                s = ChatColor.RED + "✖";
            }
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.RED + "  "+ s);
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
    },
    ANCIENT_BOTTLE(Material.EXP_BOTTLE, 21, new Research[]{BASIC_RESEARCH}){
        @Override
        public void start(PlayerResearch playerResearch){
            this.aspectMover(playerResearch);
        }
    },
    MAGIC_SCROLL(Material.PAPER, 13, new Research[]{BASIC_RESEARCH}){
        @Override
        public void start(PlayerResearch playerResearch){
            playerResearch.startResearch(new MagicScrollResearch(playerResearch));
        }

        @Override
        public void startFromLoad(PlayerResearch playerResearch, HashMap<String, Integer>  map) {
            playerResearch.startResearch(new MagicScrollResearch(playerResearch, map));
        }
    },
    MANA_SHIELD(Material.SHIELD, 31, new Research[]{BASIC_RESEARCH}){
        @Override
        public void start(PlayerResearch playerResearch){
            playerResearch.startResearch(new ManaShieldResearch(playerResearch));
        }

        @Override
        public void startFromLoad(PlayerResearch playerResearch, HashMap<String, Integer>  map) {
            playerResearch.startResearch(new ManaShieldResearch(playerResearch, map));
        }
    },
    MAGNET(Material.FEATHER, 41, new Research[]{MANA_SHIELD}){
        @Override
        public void start(PlayerResearch playerResearch){
            playerResearch.startResearch(new MagnetResearch(playerResearch));
        }

        @Override
        public void startFromLoad(PlayerResearch playerResearch, HashMap<String, Integer>  map) {
            playerResearch.startResearch(new MagnetResearch(playerResearch, map));
        }
    },
    MAGIC_SCROLL_II(Material.PAPER, 5, new Research[]{MAGIC_SCROLL}){
        @Override
        public void start(PlayerResearch playerResearch){
            this.aspectMover(playerResearch);
        }
    },
    MANA_SHIELD_II(Material.SHIELD, 39, new Research[]{MANA_SHIELD}){
        @Override
        public void start(PlayerResearch playerResearch){
            this.aspectMover(playerResearch);
        }
    },
    MAGNET_II(Material.FEATHER, 51, new Research[]{MAGNET}){
        @Override
        public void start(PlayerResearch playerResearch){
            this.aspectMover(playerResearch);
        }
    };

    private final Main plugin = Main.getPlugin();
    private final Material material;
    private final int position;
    private final Research[] neededResearches;

    Research(Material material, int position){
        this(material, position, new Research[0]);
    }

    Research(Material material, int position, Research[] neededResearches){
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

    public String getStandName(){
        return standardName(this.name());
    }

    public void start(PlayerResearch playerResearch){}
    public void startFromLoad(PlayerResearch playerResearch, HashMap<String, Integer>  map){}

    public ItemStack getResearchItem(boolean flag){
        if(flag){
            return standard(this.getMaterialType(), this.name(), new ArrayList<>(getLore(this.name())), ChatColor.GREEN + "✔");
        }else {
            List<String> lore = new ArrayList<>(getLore(this.name()));
            this.getNeededAspects().forEach((aspect, integer) -> lore.add(ChatColor.LIGHT_PURPLE + "❉ " + ChatColor.GRAY + aspect + ": " + ChatColor.GOLD + integer + ChatColor.LIGHT_PURPLE + " ❉"));
            lore.add(ChatColor.DARK_PURPLE + LangVar.msg_ctsr.getVar());
            return standard(this.getMaterialType(), this.name(), lore, ChatColor.RED + "✖");
        }
    }

    public ItemStack getResearchItem(){
        return standard(this.getMaterialType(), this.name(), new ArrayList<>(getLore(this.name())), "");
    }

    public Research[] getNeededResearch(){
        return this.neededResearches;
    }

    private ItemStack standard(Material type, String name, List<String> lore, String string){
        ItemStack itemStack = new ItemStack(type);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(standardName(name));
        for(int i = 0; i< lore.size(); i++){
            String s = lore.get(i);
            lore.set(i,ChatColor.BLUE + "" + s);
        }
        lore.add("  " + string);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public Map<EntityType, Integer> getCountOfMaxMobs(){
        return new HashMap<>();
    }

    public Map<Aspect, Integer> getNeededAspects(){
        return new HashMap<>(plugin.getIoWork().getResearchDataSaver().getMapForResearch(this));
    }

    public int getCountOfMaxLvl(){
        return plugin.getIoWork().getIntDataForResearch(this, "level");
    }

    public int getMaxUses(){
        return plugin.getIoWork().getIntDataForResearch(this, "uses");
    }

    public int getMaxSpecific(String string){
        return plugin.getIoWork().getIntDataForResearch(this, string);
    }

    private static String getNameString(String string){
        return Main.getPlugin().getLangCF().getString(string + ".name");
    }

    private static List<String> getLore(String string){
        return Main.getPlugin().getLangCF().getStringList(string + ".lore");
    }

    public void aspectMover(PlayerResearch playerResearch){
        Map<Aspect, Integer>  map = this.getNeededAspects();
        if(aspectResearch(playerResearch.getPlayerMana().getPlayerAspectsStorage())){
            map.forEach((aspect, integer) -> playerResearch.getPlayerMana().getPlayerAspectsStorage().addAspect(aspect, 0));
            playerResearch.endResearch(this);
        }else {
            playerResearch.getPlayer().sendMessage(ChatColor.RED + LangVar.msg_yama.getVar());
            map.forEach((aspect, integer) -> playerResearch.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "❉ " + ChatColor.GRAY + aspect + ": " + ChatColor.GOLD + integer + "-> " + playerResearch.getPlayerMana().getPlayerAspectsStorage().getAspects()[aspect.ordinal()] + ChatColor.LIGHT_PURPLE + " ❉"));
        }
    }

    private boolean aspectResearch(PlayerAspectStorage playerAspectsStorage){
        Map<Aspect, Integer>  map = this.getNeededAspects();
        for(boolean flag: aspectChecker(playerAspectsStorage, map)){
            if(!flag){
                return false;
            }
        }

        map.forEach((aspect, integer) -> playerAspectsStorage.addAspect(aspect, -integer));

        return true;
    }

    private List<Boolean> aspectChecker(PlayerAspectStorage playerAspectsStorage, Map<Aspect, Integer>  map){
        List<Boolean> flags = new ArrayList<>();
        map.forEach((aspect, integer) -> flags.add(playerAspectsStorage.getAspects()[aspect.ordinal()] >= integer));
        return flags;
    }
}