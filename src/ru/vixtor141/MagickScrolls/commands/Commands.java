package ru.vixtor141.MagickScrolls.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.*;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length == 0)return helpMS(commandSender, command, s, args);
        switch (args[0]){
            case "give": return givingCommand(commandSender, command, s, args);
            case "heal": return manaHealCommand(commandSender, command, s, args);
            case "cdreset": return CDReset(commandSender, command, s, args);
            case "getinfo": return getInfo(commandSender, command, s, args);
            case "help": return helpMS(commandSender, command, s, args);
        }
        return false;
    }

    private boolean givingCommand(CommandSender commandSender, Command command, String s, String[] args){//magickscrolls give [nick] [scroll] -[amount]
        if(args.length < 3 || args.length > 4)return false;
        if(!commandSender.hasPermission("magickscrolls.give")){
            commandSender.sendMessage(ChatColor.RED + "You don't have permission");
            return true;
        }
        Player player = Bukkit.getPlayer(args[1]);

        if(player == null){
            commandSender.sendMessage(ChatColor.RED + "Player isn't online");
            return true;
        }

        int amount = 1;
        if(args.length == 4)amount = Integer.parseInt(args[3]);

        if(commandSender instanceof Player && commandSender != player){
            if(!commandSender.hasPermission("magickscrolls.give.other")){
                commandSender.sendMessage(ChatColor.RED + "You don't have permission");
                return true;
            }
        }

        Crafts.ScrollsCrafts scrollsCrafts;

        try {
            scrollsCrafts = Crafts.ScrollsCrafts.valueOf(args[2].toUpperCase());
        }catch (IllegalArgumentException e){
            commandSender.sendMessage(ChatColor.RED + "Such a scroll doesn't exist");
            return true;
        }

        if(!commandSender.hasPermission("magickscrolls.give.scroll." + scrollsCrafts.name())){
            commandSender.sendMessage(ChatColor.RED + "You don't have permission");
            return true;
        }

        ItemStack item = scrollsCrafts.craftScroll(false);
        item.setAmount(amount);

        player.getInventory().addItem(item);
        commandSender.sendMessage(ChatColor.AQUA + "The scroll has been added");

        return true;
    }

    private boolean manaHealCommand(CommandSender commandSender, Command command, String s, String[] args){//magickscrolls heal -[nick]
        if(args.length > 2)return false;
        if(!commandSender.hasPermission("magickscrolls.heal")){
            commandSender.sendMessage(ChatColor.RED + "You don't have permission");
            return true;
        }

        Player player;

        if(args.length == 2){
            player = Bukkit.getPlayer(args[1]);
            if(commandSender instanceof Player && commandSender != player){
                if(!commandSender.hasPermission("magickscrolls.heal.other")){
                    commandSender.sendMessage(ChatColor.RED + "You don't have permission");
                    return true;
                }
            }
            if(player == null){
                commandSender.sendMessage(ChatColor.RED + "Player is offline");
                return true;
            }
        }else{
            if(!(commandSender instanceof Player)){
                commandSender.sendMessage(ChatColor.RED + "Only for players");
                return true;
            }
            player = (Player) commandSender;
        }

        Mana playerMana = Mana.getPlayerMap().get(player);

        playerMana.setCurrentMana(50);
        commandSender.sendMessage(ChatColor.YELLOW + "Mana is now full");
        return true;
    }

    private boolean CDReset(CommandSender commandSender, Command command, String s, String[] args){//magickscrolls cdreset -[nick] -[scroll] -[seconds]
        if(args.length > 4)return false;
        if(!commandSender.hasPermission("magickscrolls.cdreset")){
            commandSender.sendMessage(ChatColor.RED + "You don't have permission");
            return true;
        }
        Player player;
        int setSecond = 0;

        if(args.length > 1){
            if(!commandSender.hasPermission("magickscrolls.cdreset.other")){
                commandSender.sendMessage(ChatColor.RED + "You don't have permission");
                return true;
            }
            if(Bukkit.getPlayer(args[1]) == null){
                commandSender.sendMessage(ChatColor.RED + "Player is offline");
                return true;
            }
            player = Bukkit.getPlayer(args[1]);
        }else{
            player = (Player) commandSender;
        }
        if(args.length == 4){
            setSecond = Integer.parseInt(args[3]);
        }

        Mana playerMana = Mana.getPlayerMap().get(player);

        if(args.length > 2){
            CDSystem.Scrolls scroll;
            try {
                 scroll = CDSystem.Scrolls.valueOf(args[2].toUpperCase());
            }catch (IllegalArgumentException e){
                commandSender.sendMessage(ChatColor.RED + "Such a scroll doesn't exist");
                return true;
            }
            playerMana.getCdSystem().CDSet(scroll, setSecond);
            commandSender.sendMessage(ChatColor.GREEN + args[2].toUpperCase() + "CD was set to " + setSecond);
            return true;
        }

        for(int i = 0; i < CDSystem.Scrolls.values().length; i++) {
            playerMana.getCdSystem().CDSet(CDSystem.Scrolls.values()[i], setSecond);
        }

        commandSender.sendMessage(ChatColor.GREEN + "CDs was set to " + setSecond);
        return true;
    }

    private boolean getInfo(CommandSender commandSender, Command command, String s, String[] args){//magickscrolls getinfo [nick]
        if(args.length != 2){
            return false;
        }
        if(!commandSender.hasPermission("magickscrolls.getinfo")){
            commandSender.sendMessage(ChatColor.RED + "You don't have permission");
            return true;
        }
        Player player = Bukkit.getPlayer(args[1]);

        if(player != null){
            Mana playerMana = Mana.getPlayerMap().get(player);
            commandSender.sendMessage(ChatColor.AQUA + "Info about player: " + ChatColor.GREEN + player.getName());
            commandSender.sendMessage(ChatColor.AQUA + "Mana: " + ChatColor.GOLD + playerMana.getCurrentMana() + ChatColor.YELLOW + "/" + ChatColor.GOLD + playerMana.getMaxMana());
            commandSender.sendMessage(ChatColor.AQUA + "CDs list: " + ChatColor.GOLD + playerMana.getCdSystem().getCDs());
        }else {
            commandSender.sendMessage("Player is offline, wait until a file is found");
            SearchPlayerThread searchPlayerThread = new SearchPlayerThread(Bukkit.getOfflinePlayers(), commandSender, args[1]);
            searchPlayerThread.setPriority(Thread.MIN_PRIORITY);
            searchPlayerThread.start();
        }
        return true;
    }

    private boolean helpMS(CommandSender commandSender, Command command, String s, String[] args){//magickscrolls help
        String[] string = {
                ChatColor.GREEN + "/magickscrolls give <nick> <scroll> [amount]" + ChatColor.YELLOW + " - Giving a scroll which you type",
                ChatColor.GREEN + "/magickscrolls heal [nick]" + ChatColor.YELLOW + " - regen all your mana",
                ChatColor.GREEN + "/magickscrolls cdreset [nick] [scroll] [seconds]" + ChatColor.YELLOW + " - sets your CD to the desired seconds or to zero",
                ChatColor.GREEN + "/magickscrolls getinfo <nick>" + ChatColor.YELLOW + " - getting info about player",
                ChatColor.GREEN + "/magickscrolls help" + ChatColor.YELLOW + " - this page"
        };
        commandSender.sendMessage(string);
        return true;
    }
}