package ru.vixtor141.MagickScrolls.chatPlay;

import net.md_5.bungee.api.ChatColor;

import java.util.Random;

public class ImpSay {

    private final ChatColor[] colors = {ChatColor.DARK_RED, ChatColor.DARK_GREEN, ChatColor.GOLD, ChatColor.RED, ChatColor.YELLOW, ChatColor.DARK_AQUA, ChatColor.DARK_PURPLE, ChatColor.LIGHT_PURPLE};
    private final String message;

    public ImpSay(String message){
        this.message = message;
    }

    public String changeMessage(){
        String[] strD = message.split(" ");
        StringBuilder newMessage = new StringBuilder();
        ChatColor color = colors[new Random().nextInt(colors.length)];
        for(String string : strD){
            int count = new Random().nextInt(3);
            StringBuilder tr = new StringBuilder();
            for(int i = 0; i<count; i++){
                tr.append("d");
            }
            ChatColor trcol = colors[new Random().nextInt(colors.length)];
            newMessage.append(trcol).append(ChatColor.MAGIC).append(tr.toString()).append(color).append(string).append(" ");
        }

        return newMessage.toString();
    }
}
