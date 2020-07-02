package ru.vixtor141.MagickScrolls.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.*;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;


public class Commands implements CommandExecutor {

    private final Main plugin = Main.getPlugin();

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
            commandSender.sendMessage(ChatColor.RED + plugin.getReadingLangFile().getMsg("msg_ydhp"));
            return true;
        }
        Player player = Bukkit.getPlayer(args[1]);

        if(player == null){
            commandSender.sendMessage(ChatColor.RED + plugin.getReadingLangFile().getMsg("msg_pino"));
            return true;
        }

        int amount = 1;
        if(args.length == 4)amount = Integer.parseInt(args[3]);

        if(commandSender instanceof Player && commandSender != player){
            if(!commandSender.hasPermission("magickscrolls.give.other")){
                commandSender.sendMessage(ChatColor.RED + plugin.getReadingLangFile().getMsg("msg_ydhp"));
                return true;
            }
        }

        ACCrafts.CraftsOfScrolls scrollsCrafts;

        try {
            scrollsCrafts = ACCrafts.CraftsOfScrolls.valueOf(args[2].toUpperCase());
        }catch (IllegalArgumentException e){
            commandSender.sendMessage(ChatColor.RED + plugin.getReadingLangFile().getMsg("msg_sasdne"));
            return true;
        }

        if(!commandSender.hasPermission("magickscrolls.give.scroll." + scrollsCrafts.name())){
            commandSender.sendMessage(ChatColor.RED + plugin.getReadingLangFile().getMsg("msg_ydhp"));
            return true;
        }

        ItemStack item = scrollsCrafts.craftAltarResult();
        item.setAmount(amount);

        player.getInventory().addItem(item);
        commandSender.sendMessage(ChatColor.AQUA + plugin.getReadingLangFile().getMsg("msg_tshba"));

        return true;
    }

    private boolean manaHealCommand(CommandSender commandSender, Command command, String s, String[] args){//magickscrolls heal -[nick]
        if(args.length > 2)return false;
        if(!commandSender.hasPermission("magickscrolls.heal")){
            commandSender.sendMessage(ChatColor.RED + plugin.getReadingLangFile().getMsg("msg_ydhp"));
            return true;
        }

        Player player;

        if(args.length == 2){
            player = Bukkit.getPlayer(args[1]);
            if(commandSender instanceof Player && commandSender != player){
                if(!commandSender.hasPermission("magickscrolls.heal.other")){
                    commandSender.sendMessage(ChatColor.RED + plugin.getReadingLangFile().getMsg("msg_ydhp"));
                    return true;
                }
            }
            if(player == null){
                commandSender.sendMessage(ChatColor.RED + plugin.getReadingLangFile().getMsg("msg_pino"));
                return true;
            }
        }else{
            if(!(commandSender instanceof Player)){
                commandSender.sendMessage(ChatColor.RED + "Only for players");
                return true;
            }
            player = (Player) commandSender;
        }

        Mana playerMana = plugin.getPlayerMap().get(player);

        playerMana.setCurrentMana(50);
        commandSender.sendMessage(ChatColor.YELLOW + plugin.getReadingLangFile().getMsg("msg_minf"));
        return true;
    }

    private boolean CDReset(CommandSender commandSender, Command command, String s, String[] args){//magickscrolls cdreset -[nick] -[scroll] -[seconds]
        if(args.length > 4)return false;
        if(!commandSender.hasPermission("magickscrolls.cdreset")){
            commandSender.sendMessage(ChatColor.RED + plugin.getReadingLangFile().getMsg("msg_ydhp"));
            return true;
        }
        Player player;
        int setSecond = 0;

        if(args.length > 1){
            if(!commandSender.hasPermission("magickscrolls.cdreset.other")){
                commandSender.sendMessage(ChatColor.RED + plugin.getReadingLangFile().getMsg("msg_ydhp"));
                return true;
            }
            if(Bukkit.getPlayer(args[1]) == null){
                commandSender.sendMessage(ChatColor.RED + plugin.getReadingLangFile().getMsg("msg_pino"));
                return true;
            }
            player = Bukkit.getPlayer(args[1]);
        }else{
            player = (Player) commandSender;
        }
        if(args.length == 4){
            setSecond = Integer.parseInt(args[3]);
        }

        Mana playerMana = plugin.getPlayerMap().get(player);

        if(args.length > 2){
            CDSystem.Scrolls scroll;
            try {
                 scroll = CDSystem.Scrolls.valueOf(args[2].toUpperCase());
            }catch (IllegalArgumentException e){
                commandSender.sendMessage(ChatColor.RED + plugin.getReadingLangFile().getMsg("msg_sasdne"));
                return true;
            }
            playerMana.getCdSystem().CDSet(scroll, setSecond);
            commandSender.sendMessage(ChatColor.GREEN + args[2].toUpperCase() + plugin.getReadingLangFile().getMsg("msg_cdwst") + setSecond);
            return true;
        }

        for(int i = 0; i < CDSystem.Scrolls.values().length; i++) {
            playerMana.getCdSystem().CDSet(CDSystem.Scrolls.values()[i], setSecond);
        }

        commandSender.sendMessage(ChatColor.GREEN + plugin.getReadingLangFile().getMsg("msg_cdwst") + setSecond);
        return true;
    }

    private boolean getInfo(CommandSender commandSender, Command command, String s, String[] args){//magickscrolls getinfo [nick]
        if(args.length != 2){
            return false;
        }
        if(!commandSender.hasPermission("magickscrolls.getinfo")){
            commandSender.sendMessage(ChatColor.RED + plugin.getReadingLangFile().getMsg("msg_ydhp"));
            return true;
        }
        Player player = Bukkit.getPlayer(args[1]);

        if(player != null){
            Mana playerMana = plugin.getPlayerMap().get(player);
            commandSender.sendMessage(ChatColor.AQUA + plugin.getReadingLangFile().getMsg("msg_iap") + ChatColor.GREEN + player.getName());
            commandSender.sendMessage(ChatColor.AQUA + plugin.getReadingLangFile().getMsg("msg_m") + ChatColor.GOLD + playerMana.getCurrentMana() + ChatColor.YELLOW + "/" + ChatColor.GOLD + playerMana.getMaxMana());
            commandSender.sendMessage(ChatColor.AQUA + plugin.getReadingLangFile().getMsg("msg_cdsl") + ChatColor.GOLD + playerMana.getCdSystem().getCDs());
        }else {
            commandSender.sendMessage(plugin.getReadingLangFile().getMsg("msg_piowuafif"));
            SearchPlayerThread searchPlayerThread = new SearchPlayerThread(Bukkit.getOfflinePlayers(), commandSender, args[1]);
            searchPlayerThread.setPriority(Thread.MIN_PRIORITY);
            searchPlayerThread.start();
        }
        return true;
    }

    private boolean helpMS(CommandSender commandSender, Command command, String s, String[] args){//magickscrolls help
        String[] string = {
                ChatColor.GREEN + "/magickscrolls give <nick> <scroll> [amount]" + ChatColor.YELLOW + plugin.getReadingLangFile().getMsg("msg_gaswyt"),
                ChatColor.GREEN + "/magickscrolls heal [nick]" + ChatColor.YELLOW + plugin.getReadingLangFile().getMsg("msg_raym"),
                ChatColor.GREEN + "/magickscrolls cdreset [nick] [scroll] [seconds]" + ChatColor.YELLOW + plugin.getReadingLangFile().getMsg("msg_sycdttdsotz"),
                ChatColor.GREEN + "/magickscrolls getinfo <nick>" + ChatColor.YELLOW + plugin.getReadingLangFile().getMsg("msg_giap"),
                ChatColor.GREEN + "/magickscrolls help" + ChatColor.YELLOW + plugin.getReadingLangFile().getMsg("msg_tp")
        };
        commandSender.sendMessage(string);
        return true;
    }
}
