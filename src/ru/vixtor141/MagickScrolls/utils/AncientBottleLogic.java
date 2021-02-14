package ru.vixtor141.MagickScrolls.utils;

import org.bukkit.entity.Player;

public class AncientBottleLogic {

    private final Player player;
    private final int expFor16 = 352;
    private final int expFor31 = 1507;
    private int expInBottle;

    public AncientBottleLogic(Player player, int expInBottle){
        this.player = player;
        this.expInBottle = expInBottle;
    }

    private boolean takenExp(int needExp){
        if(expInBottle < 1)return false;

        if(expInBottle >= needExp){
            expInBottle -= needExp;
            player.giveExp(needExp);
        }else {
            player.giveExp(expInBottle);
            expInBottle = 0;
        }
        return true;
    }

    public boolean takeLevel(){
        return takenExp(player.getExpToLevel());
    }

    public boolean takeTenLevels(){
        int expA = totalExpCalc(player.getLevel() + 10), expN = totalExpCalc(player.getLevel());
        int needExp = expA - expN;
        return takenExp(needExp);
    }

    public boolean takeAll(){
        return takenExp(expInBottle + 1);
    }

    public boolean moveLevel(){
        if(player.getLevel() == 0 && player.getExp() == 0)return false;

        int movedExp = 0;
        movePreLevel();
        if(player.getLevel() != 0) {
            movedExp = expCalc(player.getLevel() - 1);
            player.giveExpLevels(-1);
        }
        expInBottle += movedExp;
        return true;
    }

    public boolean moveTenLevels(){
        if(player.getLevel() < 10)return false;

        movePreLevel();

        int expR = totalExpCalc(player.getLevel() - 10), expA = totalExpCalc(player.getLevel());
        expInBottle += expA - expR;
        player.giveExpLevels(-10);

        return true;
    }

    public boolean moveAll(){
        if(player.getLevel() == 0 && player.getExp() == 0)return false;
        movePreLevel();
        if(player.getLevel() != 0) {
            int exp = totalExpCalc(player.getLevel());
            expInBottle += exp;
            player.setLevel(0);
        }
        return true;
    }

    private void movePreLevel(){
        if(player.getExp() != 0) {
            expInBottle += (int) (player.getExpToLevel() * player.getExp());
            player.setExp(0);
        }
    }

    private int expCalc(int lvl){
        int exp;
        if(lvl < 16){
            exp = 2 * lvl + 7;
        } else if (lvl <= 31) {
            exp = 5 * lvl - 38;
        } else {
            exp = 9 * lvl - 158;
        }
        return exp;
    }

    public int getExpInBottle() {
        return expInBottle;
    }

    public int levelCalc(){
        if(expInBottle <= expFor16){
            return calcFormula(1, 6, - expInBottle);
        }else if(expInBottle <= expFor31){
            return calcFormula(2.5, -40.5, 360 - expInBottle);
        }else {
            return calcFormula(4.5, -162.5, 2220 - expInBottle);
        }
    }

    private int calcFormula(double a, double b, int c){
        return (int) ((Math.sqrt((b*b) - 4 * a * c) - b)/(2 * a));
    }

    private int totalExpCalc(int lvl){
        int exp;
        if (lvl < 16) {
            exp = lvl * lvl + 6 * lvl;
        } else if (lvl <= 31) {
            exp = (int) (2.5 * lvl * lvl - 40.5 * lvl + 360);
        } else {
            exp = (int) (4.5 * lvl * lvl - 162.5 * lvl + 2220);
        }
        return exp;
    }
}
