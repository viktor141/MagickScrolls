package ru.vixtor141.MagickScrolls.lang;

import org.bukkit.configuration.file.FileConfiguration;

public class ReadingLangFile {

    private FileConfiguration lang;
    public String msg_ydhp;//maybe better add enum
    public String msg_pino;
    public String msg_sasdne;
    public String msg_tshba;
    public String msg_minf;
    public String msg_cdwst;
    public String msg_iap;
    public String msg_m;
    public String msg_cdsl;
    public String msg_piowuafif;
    public String msg_gaswyt;
    public String msg_raym;
    public String msg_sycdttdsotz;
    public String msg_giap;
    public String msg_tp;
    public String msg_pnf;
    public String msg_pdne;
    public String msg_ydnhm;
    public String msg_ycutsa;
    public String msg_seconds;
    public String msg_nmay;
    public String name_nec_scroll_mobs;
    public String msg_yctt;
    public String msg_tind;
    public String msg_ymn;

    public FileConfiguration getLang() {
        return lang;
    }

    public ReadingLangFile(FileConfiguration fileConfiguration){
        this.lang = fileConfiguration;
        set();
    }

    private void set() {
        msg_ydhp = lang.getString("msg_ydhp");
        msg_pino = lang.getString("msg_pino");
        msg_sasdne = lang.getString("msg_sasdne");
        msg_tshba = lang.getString("msg_tshba");
        msg_minf = lang.getString("msg_minf");
        msg_cdwst = lang.getString("msg_cdwst");
        msg_iap = lang.getString("msg_iap");
        msg_m = lang.getString("msg_m");
        msg_cdsl = lang.getString("msg_cdsl");
        msg_piowuafif = lang.getString("msg_piowuafif");
        msg_gaswyt = lang.getString("msg_gaswyt");
        msg_raym = lang.getString("msg_raym");
        msg_sycdttdsotz = lang.getString("msg_sycdttdsotz");
        msg_giap = lang.getString("msg_giap");
        msg_tp = lang.getString("msg_tp");
        msg_pnf = lang.getString("msg_pnf");
        msg_pdne = lang.getString("msg_pdne");
        msg_ydnhm = lang.getString("msg_ydnhm");
        msg_ycutsa = lang.getString("msg_ycutsa");
        msg_seconds = lang.getString("msg_seconds");
        msg_nmay = lang.getString("msg_nmay");
        name_nec_scroll_mobs = lang.getString("name_nec_scroll_mobs");
        msg_yctt = lang.getString("msg_yctt");
        msg_tind = lang.getString("msg_tind");
        msg_ymn = lang.getString("msg_ymn");
    }
}
