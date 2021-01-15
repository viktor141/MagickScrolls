package ru.vixtor141.MagickScrolls;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;
import ru.vixtor141.MagickScrolls.lang.LangVar;

import java.io.File;
import java.util.List;


public class SearchPlayerThread extends Thread {

    private final OfflinePlayer[] offlinePlayer;
    private final CommandSender commandSender;
    private final String name;

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
        commandSender.sendMessage(ChatColor.AQUA + LangVar.msg_as.getVar());
        commandSender.sendMessage(ChatColor.AQUA + ACCrafts.CraftsOfScrolls.SPECTRAL_SHIELD.craftAltarResult().getItemMeta().getDisplayName() + " seconds: " + ChatColor.GOLD + playerStats.getInt("SpectralShieldSeconds"));
        StringBuilder message = new StringBuilder();
        List<ItemStack> itemStacks = (List<ItemStack>) playerStats.getList("AccessoriesInventory");
        if(itemStacks != null && !itemStacks.isEmpty()) {
            for (ItemStack itemAdd : itemStacks) {
                if(itemAdd != null) {
                    if (!itemAdd.getItemMeta().hasLore()) continue;
                    List<String> lore = itemAdd.getItemMeta().getLore();
                    String name = lore.get(lore.size() - 2).substring(Main.getPlugin().getSubStr());
                    if (ACCrafts.AccessoryArtefact.isArtefact(name)) {
                        message.append(ChatColor.WHITE).append("(").append(ChatColor.BLUE).append(name).append(ChatColor.GRAY).append(": ").append(ChatColor.GOLD).append("true").append(ChatColor.WHITE).append(") ");
                    }
                }
            }
        }
        commandSender.sendMessage(ChatColor.AQUA + LangVar.msg_wa.getVar() + ": " + message);
    }

}
