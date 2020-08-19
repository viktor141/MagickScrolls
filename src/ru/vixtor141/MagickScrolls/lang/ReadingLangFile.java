package ru.vixtor141.MagickScrolls.lang;

import org.bukkit.configuration.file.FileConfiguration;

public class ReadingLangFile {

    private FileConfiguration lang;

    public FileConfiguration getLang() {
        return lang;
    }

    public void setLang(FileConfiguration lang){
        this.lang = lang;
    }

    public ReadingLangFile(FileConfiguration fileConfiguration){
        this.lang = fileConfiguration;
    }

    public String getMsg(String msg){
        switch(msg){
            case "msg_ydhp" : return lang.getString("msg_ydhp");
            case "msg_pino" : return lang.getString("msg_pino");
            case "msg_sasdne" : return lang.getString("msg_sasdne");
            case "msg_tshba" : return lang.getString("msg_tshba");
            case "msg_minf" : return lang.getString("msg_minf");
            case "msg_cdwst" : return lang.getString("msg_cdwst");
            case "msg_iap" : return lang.getString("msg_iap");
            case "msg_m" : return lang.getString("msg_m");
            case "msg_cdsl" : return lang.getString("msg_cdsl");
            case "msg_piowuafif" : return lang.getString("msg_piowuafif");
            case "msg_gaswyt" : return lang.getString("msg_gaswyt");
            case "msg_raym" : return lang.getString("msg_raym");
            case "msg_sycdttdsotz" : return lang.getString("msg_sycdttdsotz");
            case "msg_giap" : return lang.getString("msg_giap");
            case "msg_tp" : return lang.getString("msg_tp");
            case "msg_pnf" : return lang.getString("msg_pnf");
            case "msg_pdne" : return lang.getString("msg_pdne");
            case "msg_ydnhm" : return lang.getString("msg_ydnhm");
            case "msg_ycutsa" : return lang.getString("msg_ycutsa");
            case "msg_seconds" : return lang.getString("msg_seconds");
            case "msg_nmay" : return lang.getString("msg_nmay");
            case "name_nec_scroll_mobs" : return lang.getString("name_nec_scroll_mobs");
            case "msg_yctt" : return lang.getString("msg_yctt");
            case "msg_tind" : return lang.getString("msg_tind");
            case "msg_ymn" : return lang.getString("msg_ymn");
            case "msg_los" : return lang.getString("msg_los");
        }
        return null;
    }
}
