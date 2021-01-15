package ru.vixtor141.MagickScrolls.research;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Research {
    BASIC_RESEARCH{
        private final String name = standardName(this.name());
        @Override
        public ItemStack getResearchItem(String string){
            ItemStack itemStack = ACCrafts.ItemsCauldronCrafts.RITUAL_BOOK.craftCauldronGetItem().clone();
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(name);
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.RED + "  "+ string);
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }

        @Override
        public int getPosition() {
            return 22;
        }

        public Material getMaterialType(){
            return ACCrafts.ItemsCauldronCrafts.RITUAL_BOOK.craftCauldronGetItem().getType();
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public void start(PlayerResearch playerResearch) {
            playerResearch.getAccessoriesInventory().open();
        }
    },
    ANCIENT_BOTTLE{
        private final Material type = Material.EXP_BOTTLE;
        private final String name = standardName(this.name());

        @Override
        public void start(PlayerResearch playerResearch){
            playerResearch.startResearch(new AncientBottleResearch(playerResearch));
        }

        @Override
        public void startFromLoad(PlayerResearch playerResearch, FileConfiguration fileConfiguration) {
            playerResearch.startResearch(new AncientBottleResearch(playerResearch, fileConfiguration));
        }

        @Override
        public Research[] getNeededResearch(){
            return new Research[]{BASIC_RESEARCH};
        }

        @Override
        public int getPosition() {
            return 21;
        }

        @Override
        public Material getMaterialType(){
            return type;
        }

        @Override
        public String getName() {
            return name;
        }
    },
    MAGIC_SCROLL{
        private final Material type = Material.PAPER;
        private final String name = standardName(this.name());

        @Override
        public int getPosition() {
            return 13;
        }

        @Override
        public void start(PlayerResearch playerResearch){
            playerResearch.startResearch(new MagicScrollResearch(playerResearch));
        }

        @Override
        public Research[] getNeededResearch(){
            return new Research[]{BASIC_RESEARCH};
        }

        @Override
        public void startFromLoad(PlayerResearch playerResearch, FileConfiguration fileConfiguration) {
            playerResearch.startResearch(new MagicScrollResearch(playerResearch, fileConfiguration));
        }

        @Override
        public Material getMaterialType() {
            return type;
        }

        @Override
        public String getName() {
            return name;
        }
    },
    MANA_SHIELD{
        private final Material type = Material.SHIELD;
        private final String name = standardName(this.name());

        @Override
        public void start(PlayerResearch playerResearch){
            playerResearch.startResearch(new ManaShieldResearch(playerResearch));
        }

        @Override
        public void startFromLoad(PlayerResearch playerResearch, FileConfiguration fileConfiguration) {
            playerResearch.startResearch(new ManaShieldResearch(playerResearch, fileConfiguration));
        }

        @Override
        public Research[] getNeededResearch(){
            return new Research[]{BASIC_RESEARCH};
        }

        @Override
        public int getPosition() {
            return 31;
        }

        @Override
        public Material getMaterialType() {
            return type;
        }

        @Override
        public String getName() {
            return name;
        }
    },
    MANA_SHIELD_LEVELS{
        private final Material type = Material.SHIELD;
        private final String name = standardName(this.name());

        @Override
        public void start(PlayerResearch playerResearch){
            playerResearch.getShieldManaLevels().openInventory();
        }

        @Override
        public Research[] getNeededResearch(){
            return new Research[]{MANA_SHIELD};
        }

        @Override
        public int getPosition() {
            return 45;
        }

        @Override
        public Material getMaterialType() {
            return type;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public ItemStack getResearchItem(String string){
            return standard(this.getMaterialType(), this.getName(), new ArrayList<>(getLore(this.name())), "");
        }
    },
    MAGIC_INVENTORY{
        private final Material type = Material.KNOWLEDGE_BOOK;
        private final String name = standardName(this.name());

        @Override
        public void start(PlayerResearch playerResearch){
            playerResearch.getAccessoriesInventory().open();
        }

        @Override
        public Research[] getNeededResearch(){
            return new Research[]{BASIC_RESEARCH};
        }

        @Override
        public int getPosition() {
            return 0;
        }

        @Override
        public Material getMaterialType() {
            return type;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public ItemStack getResearchItem(String string){
            return standard(this.getMaterialType(), this.getName(), new ArrayList<>(getLore(this.name())), "");
        }
    },
    MAGNET{
        private final Material type = Material.FEATHER;
        private final String name = standardName(this.name());

        @Override
        public void start(PlayerResearch playerResearch){
            playerResearch.startResearch(new MagnetResearch(playerResearch));
        }

        @Override
        public void startFromLoad(PlayerResearch playerResearch, FileConfiguration fileConfiguration) {
            playerResearch.startResearch(new MagnetResearch(playerResearch, fileConfiguration));
        }

        @Override
        public Research[] getNeededResearch(){
            return new Research[]{MANA_SHIELD};
        }

        @Override
        public int getPosition() {
            return 41;
        }

        @Override
        public Material getMaterialType() {
            return type;
        }

        @Override
        public String getName() {
            return name;
        }
    };

    private final Main plugin = Main.getPlugin();

    public abstract int getPosition();
    public abstract Material getMaterialType();
    public abstract String getName();

    private static String standardName(String string){
        return ChatColor.YELLOW + "" + ChatColor.BOLD + getNameString(string);
    }

    public void start(PlayerResearch playerResearch){}
    public void startFromLoad(PlayerResearch playerResearch, FileConfiguration fileConfiguration){}

    public ItemStack getResearchItem(String string){
        return standard(this.getMaterialType(), this.getName(), new ArrayList<>(getLore(this.name())), string);
    }

    public Research[] getNeededResearch(){
        return new Research[0];
    }

    private static ItemStack standard(Material type, String name, List<String> lore, String string){
        ItemStack itemStack = new ItemStack(type);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
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
}