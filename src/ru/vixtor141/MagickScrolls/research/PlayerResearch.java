package ru.vixtor141.MagickScrolls.research;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import ru.vixtor141.MagickScrolls.Mana;
import ru.vixtor141.MagickScrolls.Misc.OtherResearchBookUpdater;
import ru.vixtor141.MagickScrolls.interfaces.ResearchI;
import ru.vixtor141.MagickScrolls.inventories.AccessoriesInventory;
import ru.vixtor141.MagickScrolls.lang.LangVar;
import ru.vixtor141.MagickScrolls.levels.ShieldManaLevels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerResearch{

    private final Mana playerMana;
    private final List<Boolean> researches;
    private final List<ResearchI> activeResearch;
    private final Inventory researchBookInventory;
    private final Player player;
    private final ShieldManaLevels shieldManaLevels;
    private final AccessoriesInventory accessoriesInventory;

    public PlayerResearch(Mana playerMana){
        this.player = playerMana.getPlayer();
        this.playerMana = playerMana;
        researches = new ArrayList<>(Research.values().length);
        Arrays.stream(Research.values()).forEach(research -> researches.add(false));
        activeResearch = new ArrayList<>();
        for(int i = 0; i < Research.values().length; i++){
            activeResearch.add(null);
        }
        researchBookInventory = new ResearchBook(player).getInventory();
        shieldManaLevels = new ShieldManaLevels(player);
        accessoriesInventory = new AccessoriesInventory(player);

    }

    public void bookUpdate(){
        new ResearchBookUpdater(researchBookInventory, researches, activeResearch);
        playerMana.getPlayerRitualInventory().bookUpdate();
        new OtherResearchBookUpdater(researchBookInventory, researches);
    }

    public List<Boolean> getResearches() {
        return researches;
    }

    public void openBook(){
        player.openInventory(researchBookInventory);
    }

    public Inventory getResearchBookInventory(){
        return researchBookInventory;
    }

    public boolean checkResearch(Research research){
        return activeResearch.get(research.ordinal()) != null;
    }

    public ResearchI getResearch(Research research){
        return activeResearch.get(research.ordinal());
    }

    public void startResearch(ResearchI research){
        activeResearch.set(research.getResearch().ordinal(), research);
    }

    public void endResearch(Research research){
        researches.set(research.ordinal(), true);
        activeResearch.set(research.ordinal(), null);
        bookUpdate();
        player.sendMessage(research.getStandName() + " " + LangVar.msg_l.getVar());
    }

    public Player getPlayer() {
        return player;
    }

    public void setResearchStatus(Research research, boolean flag) {
        researches.set(research.ordinal(), flag);
        bookUpdate();
    }

    public List<ResearchI> getActiveResearch() {
        return activeResearch;
    }

    public int getCountOfPaper(){
        if(researches.get(Research.MAGIC_SCROLL_II.ordinal())){
            return 24;
        }
        return 16;
    }

    public ShieldManaLevels getShieldManaLevels(){
        return shieldManaLevels;
    }

    public AccessoriesInventory getAccessoriesInventory(){
        return accessoriesInventory;
    }

    public Mana getPlayerMana(){
        return playerMana;
    }
}
