package ru.vixtor141.MagickScrolls.ritual;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import ru.vixtor141.MagickScrolls.Mana;

public class PlayerRitualInventory {

    private final Mana playerMana;
    private final Player player;
    private final Inventory inventory;


    public PlayerRitualInventory(Mana playerMana){
        this.player = playerMana.getPlayer();
        this.playerMana = playerMana;
        this.inventory = new RitualBook(player).getInventory();
    }

    public Inventory getRitualInventory() {
        return inventory;
    }

    public void bookUpdate(){
        new RitualBookUpdater(playerMana, inventory);
    }

    public void openBook(){
        player.openInventory(inventory);
    }
}
