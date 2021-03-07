package ru.vixtor141.MagickScrolls.commands;

import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.vixtor141.MagickScrolls.*;
import ru.vixtor141.MagickScrolls.aspects.Aspect;
import ru.vixtor141.MagickScrolls.aspects.PlayerAspectStorage;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;
import ru.vixtor141.MagickScrolls.lang.LangVar;
import ru.vixtor141.MagickScrolls.research.Research;

public class Commands implements CommandExecutor {

    private final Main plugin = Main.getPlugin();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length == 0)return helpMS(commandSender, command, s, args);
        switch (args[0].toLowerCase()){
            case "give": return givingCommand(commandSender, command, s, args);
            case "heal": return manaHealCommand(commandSender, command, s, args);
            case "cdreset": return CDReset(commandSender, command, s, args);
            case "getinfo": return getInfo(commandSender, command, s, args);
            case "list": return listOfScrolls(commandSender, command, s, args);
            case "help": return helpMS(commandSender, command, s, args);
            case "reload": return reloadConfiguration(commandSender, command, s, args);
            case "setcur": return setCurrentMana(commandSender, command, s, args);
            case "setmax": return setMaxMana(commandSender, command, s, args);
            case "aspect": return aspect(commandSender, command, s, args);
            case "research": return research(commandSender, command, s, args);
        }
        return false;
    }

    private boolean givingCommand(CommandSender commandSender, Command command, String s, String[] args){//magickscrolls give <nick> <scroll> [amount]
        if(args.length < 3 || args.length > 4)return false;
        if(!commandSender.hasPermission("magickscrolls.give")){
            commandSender.sendMessage(ChatColor.RED + LangVar.msg_ydhp.getVar());
            return true;
        }
        Player player = Bukkit.getPlayer(args[1]);

        if(player == null){
            commandSender.sendMessage(ChatColor.RED + LangVar.msg_pino.getVar());
            return true;
        }

        int amount = 1;
        if(args.length == 4){
            try{
                amount = Integer.parseInt(args[3]);
            }catch (NumberFormatException exception){
                commandSender.sendMessage(ChatColor.RED + LangVar.msg_nan.getVar());
                return true;
            }
        }

        if(commandSender instanceof Player && commandSender != player){
            if(!commandSender.hasPermission("magickscrolls.give.other")){
                commandSender.sendMessage(ChatColor.RED + LangVar.msg_ydhp.getVar());
                return true;
            }
        }

        ACCrafts.CraftsOfScrolls scrollsCrafts;

        try {
            scrollsCrafts = ACCrafts.CraftsOfScrolls.valueOf(args[2].toUpperCase());
        }catch (IllegalArgumentException e){
            return giveCauldronCrafted(commandSender, args, amount, player);
        }

        if(!commandSender.hasPermission("magickscrolls.give.scroll." + scrollsCrafts.name())){
            commandSender.sendMessage(ChatColor.RED + LangVar.msg_ydhp.getVar());
            return true;
        }

        ItemStack item = scrollsCrafts.craftAltarResult();
        item.setAmount(amount);

        player.getInventory().addItem(item);
        commandSender.sendMessage(ChatColor.AQUA + LangVar.msg_tshba.getVar());

        return true;
    }

    private boolean giveCauldronCrafted(CommandSender commandSender, String[] args, int amount, Player player){
        if(!commandSender.hasPermission("magickscrolls.give.artifact")){
            commandSender.sendMessage(ChatColor.RED + LangVar.msg_ydhp.getVar());
            return true;
        }
        ACCrafts.ItemsCauldronCrafts cauldronCraft;
        try{
            cauldronCraft = ACCrafts.ItemsCauldronCrafts.valueOf(args[2].toUpperCase());
        }catch (IllegalArgumentException e) {
            commandSender.sendMessage(ChatColor.RED + LangVar.msg_sasdne.getVar());
            return true;
        }

        ItemStack item = cauldronCraft.craftCauldronGetItem();
        item.setAmount(amount);

        player.getInventory().addItem(item);
        commandSender.sendMessage(ChatColor.AQUA + LangVar.msg_tshba.getVar());
        return true;
    }

    private boolean manaHealCommand(CommandSender commandSender, Command command, String s, String[] args){//magickscrolls heal [nick]
        if(args.length > 2)return false;
        if(!commandSender.hasPermission("magickscrolls.heal")){
            commandSender.sendMessage(ChatColor.RED + LangVar.msg_ydhp.getVar());
            return true;
        }

        Player player;

        if(args.length == 2){
            player = Bukkit.getPlayer(args[1]);
            if(commandSender instanceof Player && commandSender != player){
                if(!commandSender.hasPermission("magickscrolls.heal.other")){
                    commandSender.sendMessage(ChatColor.RED + LangVar.msg_ydhp.getVar());
                    return true;
                }
            }
            if(player == null){
                commandSender.sendMessage(ChatColor.RED + LangVar.msg_pino.getVar());
                return true;
            }
        }else{
            if(!(commandSender instanceof Player)){
                commandSender.sendMessage(ChatColor.RED + "Only for players");
                return true;
            }
            player = (Player) commandSender;
        }

        Mana playerMana = getPlayerMana(player, commandSender);

        playerMana.setCurrentMana(playerMana.getMaxMana());
        commandSender.sendMessage(ChatColor.YELLOW + LangVar.msg_minf.getVar());
        return true;
    }

    private boolean CDReset(CommandSender commandSender, Command command, String s, String[] args){//magickscrolls cdreset [nick] [scroll] [seconds]
        if(args.length > 4)return false;
        if(!commandSender.hasPermission("magickscrolls.cdreset")){
            commandSender.sendMessage(ChatColor.RED + LangVar.msg_ydhp.getVar());
            return true;
        }
        Player player;
        int setSecond = 0;

        if(args.length > 1){
            if(!commandSender.hasPermission("magickscrolls.cdreset.other")){
                commandSender.sendMessage(ChatColor.RED + LangVar.msg_ydhp.getVar());
                return true;
            }
            if(Bukkit.getPlayer(args[1]) == null){
                commandSender.sendMessage(ChatColor.RED + LangVar.msg_pino.getVar());
                return true;
            }
            player = Bukkit.getPlayer(args[1]);
        }else{
            player = (Player) commandSender;
        }
        if(args.length == 4){
            try{
                setSecond = Integer.parseInt(args[3]);
            }catch (NumberFormatException exception){
                commandSender.sendMessage(ChatColor.RED + LangVar.msg_nan.getVar());
                return true;
            }
        }


        Mana playerMana = getPlayerMana(player, commandSender);

        if(args.length > 2){
            CDSystem.Scrolls scroll;
            CDSystem.RitualsCD ritual;
            try {
                scroll = CDSystem.Scrolls.valueOf(args[2].toUpperCase());
                playerMana.getCdSystem().CDSet(scroll, setSecond);
            }catch (IllegalArgumentException | NullPointerException e){
                try {
                    ritual = CDSystem.RitualsCD.valueOf(args[2].toUpperCase());
                    playerMana.getCdSystem().CDSet(ritual, setSecond);
                }catch (IllegalArgumentException | NullPointerException e1) {
                    commandSender.sendMessage(ChatColor.RED + LangVar.msg_sasdne.getVar());
                    return true;
                }
            }

            commandSender.sendMessage(ChatColor.GREEN + args[2].toUpperCase() + " " + LangVar.msg_cdwst.getVar() + setSecond);
            return true;
        }

        for(int i = 0; i < CDSystem.CDsValuesLength(); i++) {
            playerMana.getCdSystem().CDSet(i, setSecond);
        }

        commandSender.sendMessage(ChatColor.GREEN + LangVar.msg_cdwst.getVar() + setSecond);
        return true;
    }

    private boolean getInfo(CommandSender commandSender, Command command, String s, String[] args){//magickscrolls getinfo <nick>
        if(args.length != 2){
            return false;
        }
        if(!commandSender.hasPermission("magickscrolls.getinfo")){
            commandSender.sendMessage(ChatColor.RED + LangVar.msg_ydhp.getVar());
            return true;
        }
        Player player = Bukkit.getPlayer(args[1]);

        if(player != null){
            Mana playerMana = getPlayerMana(player, commandSender);
            commandSender.sendMessage(ChatColor.AQUA + LangVar.msg_iap.getVar() + ChatColor.GREEN + player.getName());
            commandSender.sendMessage(ChatColor.AQUA + LangVar.msg_m.getVar() + ChatColor.GOLD + playerMana.getCurrentMana() + ChatColor.YELLOW + "/" + ChatColor.GOLD + playerMana.getMaxMana());
            commandSender.sendMessage(ChatColor.AQUA + LangVar.msg_cdsl.getVar() + ChatColor.GOLD + playerMana.getCdSystem().getCDs());
            commandSender.sendMessage(ChatColor.AQUA + LangVar.msg_as.getVar());
            commandSender.sendMessage(ChatColor.AQUA + ACCrafts.CraftsOfScrolls.SPECTRAL_SHIELD.craftAltarResult().getItemMeta().getDisplayName() + " seconds: " + ChatColor.GOLD + playerMana.getSpectralShieldSeconds());
            StringBuilder message = new StringBuilder();
            for(ACCrafts.AccessoryArtefact accessoryArtefact : ACCrafts.AccessoryArtefact.values()){
                message.append(ChatColor.WHITE).append("(").append(ChatColor.BLUE).append(accessoryArtefact.name()).append(ChatColor.GRAY).append(": ").append(ChatColor.GOLD).append(playerMana.getWearingArtefact().get(accessoryArtefact.ordinal())).append(ChatColor.WHITE).append(") ");
            }
            commandSender.sendMessage(ChatColor.AQUA + LangVar.msg_wa.getVar() + ": " + message);
        }else {
            commandSender.sendMessage(LangVar.msg_piowuafif.getVar());
            SearchPlayerThread searchPlayerThread = new SearchPlayerThread(Bukkit.getOfflinePlayers(), commandSender, args[1]);
            searchPlayerThread.setPriority(Thread.MIN_PRIORITY);
            searchPlayerThread.start();
        }
        return true;
    }

    private boolean listOfScrolls(CommandSender commandSender, Command command, String s, String[] args){
        if(!(commandSender instanceof Player))return false;
        if(!commandSender.hasPermission("magickscrolls.list")){
            commandSender.sendMessage(ChatColor.RED + LangVar.msg_ydhp.getVar());
            return true;
        }
        for(int i = 0; i < ACCrafts.CraftsOfScrolls.values().length; i++){
            TextComponent message = new TextComponent(ChatColor.YELLOW + Main.getPlugin().getLangCF().getString(ACCrafts.CraftsOfScrolls.values()[i].name() + ".name"));
            message.setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ms give " + commandSender.getName() + " " + ACCrafts.CraftsOfScrolls.values()[i].name()));
            message.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to give").create()));
            commandSender.spigot().sendMessage(message);
        }
        return true;
    }

    private boolean helpMS(CommandSender commandSender, Command command, String s, String[] args){//magickscrolls help
        String[] string = {
                ChatColor.GREEN + "/magickscrolls give <nick> <item> [amount]" + ChatColor.YELLOW + LangVar.msg_gaswyt.getVar(),
                ChatColor.GREEN + "/magickscrolls heal [nick]" + ChatColor.YELLOW + LangVar.msg_raym.getVar(),
                ChatColor.GREEN + "/magickscrolls cdreset [nick] [scroll] [seconds]" + ChatColor.YELLOW + LangVar.msg_sycdttdsotz.getVar(),
                ChatColor.GREEN + "/magickscrolls getinfo <nick>" + ChatColor.YELLOW + LangVar.msg_giap.getVar(),
                ChatColor.GREEN + "/magickscrolls list" + ChatColor.YELLOW + LangVar.msg_los.getVar(),
                ChatColor.GREEN + "/magickscrolls setmax <nick> <number>" + ChatColor.YELLOW + LangVar.msg_smmfp.getVar(),
                ChatColor.GREEN + "/magickscrolls setcur <nick> <number>" + ChatColor.YELLOW + LangVar.msg_scmfp.getVar(),
                ChatColor.GREEN + "/magickscrolls reload" + ChatColor.YELLOW + LangVar.msg_rac.getVar(),
                ChatColor.GREEN + "/magickscrolls research <player> <research/all> <true/false>" + ChatColor.YELLOW + LangVar.msg_srv.getVar(),
                ChatColor.GREEN + "/magickscrolls aspect <add/set> <player> <aspect/all> <number>" + ChatColor.YELLOW + LangVar.msg_gostav.getVar(),
                ChatColor.GREEN + "/magickscrolls help" + ChatColor.YELLOW + LangVar.msg_tp.getVar()
        };
        commandSender.sendMessage(string);
        return true;
    }

    private boolean reloadConfiguration(CommandSender commandSender, Command command, String s, String[] args){//magickscrolls reload
        if(!commandSender.hasPermission("magickscrolls.reload")){
            commandSender.sendMessage(ChatColor.RED + LangVar.msg_ydhp.getVar());
            return true;
        }

        plugin.reloadConfig();

        plugin.initIOWork();

        commandSender.sendMessage(ChatColor.GREEN + "Reloaded!");

        return true;
    }

    private boolean setMaxMana(CommandSender commandSender, Command command, String s, String[] args){//magickscrolls setmax <player> <number>
        if(args.length != 3){
            return false;
        }

        if(!commandSender.hasPermission("magickscrolls.setmax")){
            commandSender.sendMessage(ChatColor.RED + LangVar.msg_ydhp.getVar());
            return true;
        }
        Player player = Bukkit.getPlayer(args[1]);
        if(player == null){
            commandSender.sendMessage(ChatColor.RED + LangVar.msg_pino.getVar());
            return true;
        }

        Mana playerMana = getPlayerMana(player, commandSender);
        try{
            playerMana.setMaxMana(Integer.parseInt(args[2]));
        }catch (NumberFormatException exception){
            commandSender.sendMessage(ChatColor.RED + LangVar.msg_nan.getVar());
            return true;
        }
        commandSender.sendMessage(ChatColor.GREEN + LangVar.msg_s.getVar());
        return true;
    }

    private boolean setCurrentMana(CommandSender commandSender, Command command, String s, String[] args){//magickscrolls setcur '<player> <number>'
        if(args.length != 3){
            return false;
        }

        if(!commandSender.hasPermission("magickscrolls.setcurrent")){
            commandSender.sendMessage(ChatColor.RED + LangVar.msg_ydhp.getVar());
            return true;
        }
        Player player = Bukkit.getPlayer(args[1]);
        if(player == null){
            commandSender.sendMessage(ChatColor.RED + LangVar.msg_pino.getVar());
            return true;
        }
        Mana playerMana = getPlayerMana(player, commandSender);
        try {
            playerMana.setCurrentMana(Integer.parseInt(args[2]));
        }catch (NumberFormatException exception){
            commandSender.sendMessage(ChatColor.RED + LangVar.msg_nan.getVar());
            return true;
        }
        commandSender.sendMessage(ChatColor.GREEN + LangVar.msg_s.getVar());
        return true;
    }

    private Mana getPlayerMana(Player player, CommandSender commandSender){
        if(!player.hasMetadata("MagickScrollsMana")){
            commandSender.sendMessage(ChatColor.RED + "WARNING!!! Player: " + player.getDisplayName() + " lost a plugin meta.");
            throw new NullPointerException();
        }
        return (Mana) player.getMetadata("MagickScrollsMana").get(0).value();
    }

    private boolean aspect(CommandSender commandSender, Command command, String s, String[] args){//magickscrolls aspect <add/remove/set> <player> <aspect/all> <number>
        if(args.length != 5){
            return false;
        }
        Player player = Bukkit.getPlayer(args[2]);
        if(player == null){
            commandSender.sendMessage(ChatColor.RED + LangVar.msg_pino.getVar());
            return true;
        }
        String other = "";
        if(commandSender instanceof Player && commandSender != player){
            other = "other.";
        }
        Mana playerMana = getPlayerMana(player, commandSender);
        PlayerAspectStorage playerAspectsStorage = playerMana.getPlayerAspectsStorage();

        try {
            int num = Integer.parseInt(args[4]);
            if (!args[3].equals("all")) {
                String aspectS = args[3].toLowerCase();
                Aspect aspect = Aspect.valueOf(aspectS.substring(0,1).toUpperCase() + aspectS.substring(1));
                switch (args[1]) {
                    case "add":
                        if (permissionCheck(other + "aspect.add", commandSender)) return true;
                        if (num < 0 && playerAspectsStorage.getAspects()[aspect.ordinal()] < Math.abs(num)) {
                            commandSender.sendMessage(ChatColor.RED + LangVar.msg_siltz.getVar());
                            return true;
                        }
                        playerAspectsStorage.addAspect(aspect, num);
                        break;
                    case "set":
                        if (permissionCheck(other + "aspect.set", commandSender)) return true;
                        if(num < 0){
                            commandSender.sendMessage(ChatColor.RED + LangVar.msg_niltz.getVar());
                            return true;
                        }
                        playerAspectsStorage.setAspect(aspect, num);
                        break;
                }
            }else {
                switch (args[1]) {
                    case "add":
                        if (permissionCheck(other + "aspect.add", commandSender)) return true;
                        for(Aspect aspect: Aspect.values()){
                            if (num < 0 && playerAspectsStorage.getAspects()[aspect.ordinal()] < Math.abs(num)){
                                playerAspectsStorage.setAspect(aspect, 0);
                                continue;
                            }
                            playerAspectsStorage.addAspect(aspect, num);
                        }
                        break;
                    case "set":
                        if (permissionCheck(other + "aspect.set", commandSender)) return true;
                        if(num < 0){
                            commandSender.sendMessage(ChatColor.RED + LangVar.msg_niltz.getVar());
                            return true;
                        }
                        for(Aspect aspect: Aspect.values()){
                            playerAspectsStorage.setAspect(aspect, num);
                        }
                        break;
                }
            }
        }catch (NumberFormatException exception){
            commandSender.sendMessage(ChatColor.RED + LangVar.msg_nan.getVar());
            return true;
        }catch (IllegalArgumentException exception){
            commandSender.sendMessage(ChatColor.RED + LangVar.msg_tadne.getVar());
            return true;
        }

        commandSender.sendMessage(ChatColor.GREEN + LangVar.msg_suc.getVar());

        return true;
    }

    private boolean permissionCheck(String s, CommandSender commandSender){
        if(!commandSender.hasPermission("magickscrolls." + s)){
            commandSender.sendMessage(ChatColor.RED + LangVar.msg_ydhp.getVar());
            return true;
        }
        return false;
    }

    private boolean research(CommandSender commandSender, Command command, String s, String[] args){//magickscrolls research <player> <research> <true/false>
        if(args.length != 4){
            return false;
        }

        Player player = Bukkit.getPlayer(args[1]);
        if(player == null){
            commandSender.sendMessage(ChatColor.RED + LangVar.msg_pino.getVar());
            return true;
        }
        String other = "";
        if(commandSender instanceof Player && commandSender != player){
            other = ".other";
        }

        if(permissionCheck("research" + other, commandSender)){
            commandSender.sendMessage(ChatColor.RED + LangVar.msg_ydhp.getVar());
            return true;
        }

        try {
            boolean flag = Boolean.parseBoolean(args[3]);
            if(!args[2].equals("all")) {
                Research research = Research.valueOf(args[2].toUpperCase());
                getPlayerMana(player, commandSender).getPlayerResearch().setResearchStatus(research, flag);
            }else {
                for(Research research: Research.values()){
                    getPlayerMana(player, commandSender).getPlayerResearch().getResearches().set(research.ordinal(), flag);
                }
                getPlayerMana(player, commandSender).getPlayerResearch().bookUpdate();
            }
        }catch (NumberFormatException exception){
            commandSender.sendMessage(ChatColor.RED + LangVar.msg_nabf.getVar());
            return true;
        }catch (IllegalArgumentException exception){
            commandSender.sendMessage(ChatColor.RED + LangVar.msg_trdne.getVar());
            return true;
        }

        commandSender.sendMessage(ChatColor.GREEN + args[2] + " " + LangVar.msg_ist.getVar() + " " + args[3]);
        return true;
    }

}
