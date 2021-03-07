package ru.vixtor141.MagickScrolls;

import org.bukkit.configuration.file.FileConfiguration;
import ru.vixtor141.MagickScrolls.crafts.ACCrafts;
import ru.vixtor141.MagickScrolls.interfaces.ResearchI;
import ru.vixtor141.MagickScrolls.research.Research;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DataBase {

    private final String url;
    private String user, password;
    private final boolean flag;
    private enum Column{
        id{
            @Override
            public void fillUp(Mana playerMana, ResultSet resultSet) {
                playerMana.setIndividualID(getIntFrom("id", resultSet, -404));
            }
        },
        CurrentMana{
            @Override
            public void fillUp(Mana playerMana, ResultSet resultSet) {
                playerMana.setCurrentMana(getFloatFrom("CurrentMana", resultSet, 50));
            }
        },
        MaxMana{
            @Override
            public void fillUp(Mana playerMana, ResultSet resultSet) {
                playerMana.setMaxMana(getFloatFrom("MaxMana", resultSet, 50));
            }
        },
        SpectralShieldSeconds{
            @Override
            public void fillUp(Mana playerMana, ResultSet resultSet) {
                playerMana.setSpectralShieldSeconds(getIntFrom("SpectralShieldSeconds", resultSet, 0));
            }
        },
        CDSystem{
            @Override
            public void fillUp(Mana playerMana, ResultSet resultSet){
                String[] stringArrays = getStringArrayFromString(getStringArrayFrom("CDSystem", resultSet));
                if(stringArrays.length == 1 && stringArrays[0].equals(""))return;
                for(int i = 0; i<stringArrays.length; i++){
                    playerMana.getCdSystem().getCDs().set(i, Integer.parseInt(stringArrays[i].replaceAll(" ", "")));
                }
            }
        },
        Research {
            @Override
            public void fillUp(Mana playerMana, ResultSet resultSet) {
                String[] stringArrays = getStringArrayFromString(getStringArrayFrom("Research", resultSet));
                if(stringArrays.length == 1 && stringArrays[0].equals(""))return;
                for(int i = 0; i<stringArrays.length; i++){
                    playerMana.getPlayerResearch().getResearches().set(i, Boolean.parseBoolean(stringArrays[i].replaceAll(" ", "")));
                }
                playerMana.getPlayerResearch().bookUpdate();
            }
        },
        ActiveResearch {
            @Override
            public void fillUp(Mana playerMana, ResultSet resultSet) {
                String[] ARArray = getStringArrayFromString(getStringArrayFrom("ActiveResearch", resultSet));
                if(ARArray.length == 1 && ARArray[0].equals(""))return;
                String[] ARDArray = getStringArrayFromString(getStringArrayFrom("ActiveResearchData", resultSet));
                HashMap<String, Integer> map = new HashMap<>();
                for (String AResearchD : ARDArray){
                    String[] ARDASplit = AResearchD.replaceAll(" ", "").split("=");
                    map.put(ARDASplit[0], Integer.parseInt(ARDASplit[1]));
                }
                for(String AResearch : ARArray){
                    ru.vixtor141.MagickScrolls.research.Research.valueOf(AResearch.replaceAll(" ", "")).startFromLoad(playerMana.getPlayerResearch(), map);
                }
            }
        },
        ActiveResearchData {
            @Override
            public void fillUp(Mana playerMana, ResultSet resultSet) {
            }
        },
        ShieldLevelCount {
            @Override
            public void fillUp(Mana playerMana, ResultSet resultSet) {
                playerMana.getPlayerResearch().getShieldManaLevels().setCount(getIntFrom("ShieldLevelCount", resultSet, 0));
            }
        },
        ManaShieldLevel {
            @Override
            public void fillUp(Mana playerMana, ResultSet resultSet) {
                playerMana.getPlayerResearch().getShieldManaLevels().setManaShieldLevel(getStringFrom("ManaShieldLevel", resultSet, "ZERO"));
            }
        },
        AccessoriesInventory {
            @Override
            public void fillUp(Mana playerMana, ResultSet resultSet) {
                String[] WAArray = getStringArrayFromString(getStringArrayFrom("AccessoriesInventory", resultSet));
                if(WAArray.length == 1 && WAArray[0].equals(""))return;
                for(int i = 0; i<WAArray.length; i++){
                    if(Boolean.parseBoolean(WAArray[i].replaceAll(" ", ""))) {
                        playerMana.getPlayerResearch().getAccessoriesInventory().getInventory().addItem(ACCrafts.AccessoryArtefact.values()[i].getItem());
                        playerMana.getWearingArtefact().set(i, true);
                    }
                }
            }
        },
        Aspects {
            @Override
            public void fillUp(Mana playerMana, ResultSet resultSet) {
                String[] AArray = getStringArrayFromString(getStringArrayFrom("Aspects", resultSet));
                if(AArray.length == 1 && AArray[0].equals(""))return;
                for(int i = 0; i<AArray.length; i++){
                    playerMana.getPlayerAspectsStorage().getAspects()[i] = Integer.parseInt(AArray[i].replaceAll(" ", ""));
                }
                playerMana.getPlayerAspectsStorage().getAspectGui().inventoryFill();
            }
        };

        public abstract void fillUp(Mana playerMana, ResultSet resultSet);

        public int getIntFrom(String string, ResultSet resultSet, int defaultValue){
            try {
                int value = resultSet.getInt(string);
                if(!resultSet.wasNull()){
                    return value;
                }
            }catch(SQLException exception){
                exception.printStackTrace();
            }
            return defaultValue;
        }

        public float getFloatFrom(String string, ResultSet resultSet, float defaultValue){
            try {
                float value = resultSet.getFloat(string);
                if(!resultSet.wasNull()){
                    return value;
                }
            }catch(SQLException exception){
                exception.printStackTrace();
            }
            return defaultValue;
        }

        public String getStringArrayFrom(String string, ResultSet resultSet){
            try {
                String sValue = resultSet.getString(string);
                if(!resultSet.wasNull()) {
                    return sValue;
                }
            }catch(SQLException exception){
                exception.printStackTrace();
            }
            return "[]";
        }

        public String getStringFrom(String string, ResultSet resultSet, String defaultValue){
            try {
                String sValue = resultSet.getString(string);
                if(!resultSet.wasNull()) {
                    return sValue;
                }
            }catch(SQLException exception){
                exception.printStackTrace();
            }
            return defaultValue;
        }

        public String[] getStringArrayFromString(String string){
            return string.substring(1, string.length() - 1).split(",");
        }

    }


    public DataBase() throws Exception{
        FileConfiguration config = Main.getPlugin().getConfig();

        if(!config.getBoolean("localFileDB")){
            String host = config.getString("host");
            int port = config.getInt("port");
            String dbName = config.getString("dbName");
            user = config.getString("user");
            password = config.getString("password");
            url = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
            flag = true;
        }else {
            url = "jdbc:sqlite:" + Main.getPlugin().getDataFolder() + File.separator + "database.db";
            flag = false;

        }

        update("CREATE TABLE IF NOT EXISTS magick_scrolls_player_data (`Player` TEXT, `id` INT, `CurrentMana` FLOAT, `MaxMana` FLOAT, `SpectralShieldSeconds` INT, `CDSystem` TEXT, `Research` TEXT, `ActiveResearch` TEXT, `ActiveResearchData` TEXT, `ShieldLevelCount` INT, `ManaShieldLevel` TEXT, `AccessoriesInventory` TEXT, `Aspects` TEXT);");
    }

    private Connection getConnection() throws SQLException {
        Connection connection;
        if(flag) {
            connection = DriverManager.getConnection(url, user, password);
        }else {
            connection = DriverManager.getConnection(url);
        }
        return connection;
    }


    public synchronized void saveDataToDB(Mana playerMana){
        String inquiry = "UPDATE magick_scrolls_player_data SET `CurrentMana` = '%f', `MaxMana` = '%f', `SpectralShieldSeconds` = %d, `CDSystem` = '%s', `Research` = '%s', `ActiveResearch` = '%s', `ActiveResearchData` = '%s', `ShieldLevelCount` = %d, `ManaShieldLevel` = '%s', `AccessoriesInventory` = '%s', `Aspects` = '%s' WHERE id = " + playerMana.getIndividualID();

        List<ResearchI> researchI = playerMana.getPlayerResearch().getActiveResearch();
        List<String> activeResearch = new ArrayList<>();
        HashMap<String, Integer> ActiveResearchData = new HashMap<>();
        for(int i = 0; i < researchI.size(); i++){
            if(researchI.get(i) != null) {
                activeResearch.add(Research.values()[i].name());
                ActiveResearchData.putAll(researchI.get(i).saveResearchData());
            }
        }

        update(String.format(inquiry,
                playerMana.getCurrentMana(), playerMana.getMaxMana(), playerMana.getSpectralShieldSeconds(),
                playerMana.getCdSystem().getCDs().toString(),
                playerMana.getPlayerResearch().getResearches().toString(), activeResearch.toString(), ActiveResearchData.toString(),
                playerMana.getPlayerResearch().getShieldManaLevels().getCount(), playerMana.getPlayerResearch().getShieldManaLevels().getManaShieldLevel().name(),
                playerMana.getWearingArtefact(),
                Arrays.toString(playerMana.getPlayerAspectsStorage().getAspects())));
    }

    public synchronized void loadDataFromDB(Mana playerMana){
        boolean flag = false;
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();

            String string ="SELECT `id`, `CurrentMana`, `MaxMana`, `SpectralShieldSeconds`, `CDSystem`, `Research`, `ActiveResearch`, `ActiveResearchData`, `ShieldLevelCount`, `ManaShieldLevel`, `AccessoriesInventory`, `Aspects` FROM magick_scrolls_player_data WHERE Player = '%s'";

            ResultSet resultSet = statement.executeQuery(String.format(string, playerMana.getPlayer().getName()));

            if(resultSet.next()){
                for(Column column : Column.values()){
                    column.fillUp(playerMana, resultSet);
                }
            }else{
                flag = true;
            }

            resultSet.close();
            statement.close();
            connection.close();

        }catch (SQLException e){
            e.printStackTrace();
        }
        if(flag){
            firstTime(playerMana);
        }
    }

    private void update(String string){
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();

            statement.executeUpdate(string);

            statement.close();
            connection.close();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void setIndividualID(Mana playerMana){
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM magick_scrolls_player_data");

            resultSet.next();
            playerMana.setIndividualID(resultSet.getInt(1));

            resultSet.close();
            statement.close();
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void firstTime(Mana playerMana){
        setIndividualID(playerMana);
        String query = "INSERT INTO magick_scrolls_player_data (`Player`, `id`, `CurrentMana`, `MaxMana`, `SpectralShieldSeconds`, `CDSystem`, `Research`, `ActiveResearch`, `ActiveResearchData`, `ShieldLevelCount`, `ManaShieldLevel`, `AccessoriesInventory`, `Aspects`) VALUES ('%s', %d, '%f', '%f', %d, '%s', '%s', '%s', '%s', %d, '%s', '%s', '%s')";

        playerMana.getPlayerResearch().bookUpdate();
        playerMana.getPlayerAspectsStorage().getAspectGui().inventoryFill();

        update(String.format(query,
                playerMana.getPlayer().getName(), playerMana.getIndividualID(),
                playerMana.getCurrentMana(), playerMana.getMaxMana(), playerMana.getSpectralShieldSeconds(),
                playerMana.getCdSystem().getCDs().toString(), playerMana.getPlayerResearch().getResearches(), "[]", "{}",
                playerMana.getPlayerResearch().getShieldManaLevels().getCount(), playerMana.getPlayerResearch().getShieldManaLevels().getManaShieldLevel().name(),
                playerMana.getWearingArtefact(),
                Arrays.toString(playerMana.getPlayerAspectsStorage().getAspects())));
    }
}
