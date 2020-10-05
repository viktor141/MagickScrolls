package ru.vixtor141.MagickScrolls;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import ru.vixtor141.MagickScrolls.lang.LangVar;

import java.io.File;


public class SearchPlayerThread extends Thread {

    private final OfflinePlayer[] offlinePlayer;
    private final CommandSender commandSender;
    private final String name;
    private final Main plugin = Main.getPlugin();

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
        commandSender.sendMessage(ChatColor.RED + LangVar.msg_pnf.getVar());
    }

    private void sendMessToCS(OfflinePlayer player){
        if(commandSender instanceof Player && !((Player) commandSender).isOnline())return;
        File playerSF = new File(Main.getPlugin().getDataFolder() + File.separator + "Players" + File.separator + player.getUniqueId());
        if(!playerSF.exists()){
            commandSender.sendMessage(ChatColor.RED + LangVar.msg_pdne.getVar());
            return;
        }
        FileConfiguration playerStats = YamlConfiguration.loadConfiguration(playerSF);
        commandSender.sendMessage(ChatColor.AQUA + LangVar.msg_iap.getVar() + ChatColor.GREEN + player.getName());
        commandSender.sendMessage(ChatColor.AQUA + LangVar.msg_m.getVar() + ChatColor.GOLD + playerStats.getDouble("CurrentMana") + ChatColor.YELLOW + "/" + ChatColor.GOLD + playerStats.getDouble("MaxMana"));
        commandSender.sendMessage(ChatColor.AQUA + LangVar.msg_cdsl.getVar() + ChatColor.GOLD + playerStats.getIntegerList("CDSystem"));
    }

}
