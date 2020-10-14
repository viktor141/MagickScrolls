package ru.vixtor141.MagickScrolls.Misc;

import net.md_5.bungee.api.chat.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import ru.vixtor141.MagickScrolls.Main;
import ru.vixtor141.MagickScrolls.lang.LangVar;

public class BookCreator {

    private final FileConfiguration lang = Main.getPlugin().getLangCF();
    private final ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
    private final BookMeta bm = (BookMeta)book.getItemMeta();

    public BookCreator(){
        bm.setAuthor(lang.getString("rb_sa"));
        bm.setTitle(lang.getString("rb_st"));

        for (RitualEnum.Rituals ritual : RitualEnum.Rituals.values()) {
            TextComponent select = new TextComponent(ChatColor.GREEN + lang.getString("rb_s").replace("\\n", "\n"));
            select.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ms rs " + ritual.name()));
            select.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GREEN + LangVar.rb_sh.getVar()).create()));
            bm.spigot().addPage(new BaseComponent[]{ritual.getTitle(), ritual.getDescription(), ritual.getNeed(), select});
        }
        book.setItemMeta(bm);
    }

    public ItemStack getBook(){
        return book;
    }

}
