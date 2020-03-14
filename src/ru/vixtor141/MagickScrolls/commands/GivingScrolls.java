package ru.vixtor141.MagickScrolls.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.CDSystem;
import ru.vixtor141.MagickScrolls.Crafts;
import ru.vixtor141.MagickScrolls.Mana;

public class GivingScrolls implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        switch (args[0]){
            case "give": return givingCommand(commandSender, command, s, args);
            case "heal": return manaHealCommand(commandSender, command, s, args);
            case "cdreset": return CDReset(commandSender, command, s, args);
        }
        return false;
    }

    private boolean givingCommand(CommandSender commandSender, Command command, String s, String[] args){
        if(!commandSender.hasPermission("magickscrolls.give")){
            commandSender.sendMessage(ChatColor.RED + "You don't have permission");
            return true;
        }

        if(args.length < 3)return false;
        if(!args[0].equals("give"))return false;
        if(!Bukkit.getPlayer(args[1]).isOnline()){
            commandSender.sendMessage("Player isn't online");
            return true;
        }

        Player player = Bukkit.getPlayer(args[1]);

        Crafts.ScrollsCrafts scrollsCrafts = Crafts.ScrollsCrafts.valueOf(args[2].toUpperCase());

        ItemStack item = scrollsCrafts.craftScroll(false);

        item.setAmount(Integer.parseInt(args[3]));

        player.getInventory().addItem(item);
        player.sendMessage("scroll has been added");

        return true;
    }

    private boolean manaHealCommand(CommandSender commandSender, Command command, String s, String[] args){
        if(args.length != 1)return false;

        if(!(commandSender instanceof Player))return false;

        Player player = (Player) commandSender;
        Mana playerMana = Mana.getPlayerMap().get(player);

        playerMana.setCurrentMana(50);

        return true;
    }

    private boolean CDReset(CommandSender commandSender, Command command, String s, String[] args){
        if(args.length < 1)return false;

        if(!(commandSender instanceof Player))return false;

        Player player = (Player) commandSender;
        Mana playerMana = Mana.getPlayerMap().get(player);

        if(args[1] != null){
            playerMana.getCdSystem().CDSet(CDSystem.Scrolls.valueOf(args[1].toUpperCase()), 0);
            return true;
        }

        for(int i = 0; i < CDSystem.Scrolls.values().length; i++) {
            playerMana.getCdSystem().CDSet(CDSystem.Scrolls.values()[i], 0);
        }
        return true;
    }

}
