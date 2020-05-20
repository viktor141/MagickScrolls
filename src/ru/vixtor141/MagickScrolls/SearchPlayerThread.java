package ru.vixtor141.MagickScrolls;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;


public class SearchPlayerThread extends Thread {

    private OfflinePlayer[] offlinePlayer;
    private CommandSender commandSender;
    private String name;
    private Main plugin = Main.getPlugin();

    public SearchPlayerThread(OfflinePlayer[] offlinePlayer, CommandSender commandSender, String name){
        this.offlinePlayer = offlinePlayer;
        this.commandSender = commandSender;
        this.name = name;
    }

    @Override
    public void run() {
        for(OfflinePlayer op: offlinePlayer){
            if(op.getName().equals(name)){
                sendMessToCS(op);
                return;
            }
        }
        if(commandSender instanceof Player && !((Player) commandSender).isOnline())return;
        commandSender.sendMessage(ChatColor.RED + plugin.getReadingLangFile().getMsg("msg_pnf"));
    }

    private void sendMessToCS(OfflinePlayer player){
        if(commandSender instanceof Player && !((Player) commandSender).isOnline())return;
        File playerSF = new File(Main.getPlugin().getDataFolder() + File.separator + "Players" + File.separator + player.getUniqueId());
        if(!playerSF.exists()){
            commandSender.sendMessage(ChatColor.RED + plugin.getReadingLangFile().getMsg("msg_pdne"));
            return;
        }
        FileConfiguration playerStats = YamlConfiguration.loadConfiguration(playerSF);
        commandSender.sendMessage(ChatColor.AQUA + plugin.getReadingLangFile().getMsg("msg_iap") + ChatColor.GREEN + player.getName());
        commandSender.sendMessage(ChatColor.AQUA + plugin.getReadingLangFile().getMsg("msg_m") + ChatColor.GOLD + playerStats.getDouble("CurrentMana") + ChatColor.YELLOW + "/" + ChatColor.GOLD + playerStats.getDouble("MaxMana"));
        commandSender.sendMessage(ChatColor.AQUA + plugin.getReadingLangFile().getMsg("msg_cdsl") + ChatColor.GOLD + playerStats.getIntegerList("CDSystem"));
    }

}
