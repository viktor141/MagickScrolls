package ru.vixtor141.MagickScrolls.aspects;

import ru.vixtor141.MagickScrolls.Mana;

public class PlayerAspectStorage {

    private final Mana playerMana;
    private final int[] aspects;
    private final AspectGui aspectGui;

    public PlayerAspectStorage(Mana playerMana){
        this.playerMana = playerMana;
        aspects = new int[Aspect.values().length];
        aspectGui = new AspectGui(playerMana.getPlayer(), this);
    }

    public int[] getAspects(){
        return aspects;
    }

    public AspectGui getAspectGui() {
        return aspectGui;
    }

    public void addAspect(Aspect aspect, int count){
        aspects[aspect.ordinal()] += count;
        aspectGui.aspectGuiUpdate(aspect);
    }

    public void setAspect(Aspect aspect, int num){
        aspects[aspect.ordinal()] = num;
        aspectGui.aspectGuiUpdate(aspect);
    }

    public void openAspects(){
        playerMana.getPlayer().openInventory(getAspectGui().getInventory());
    }
}
