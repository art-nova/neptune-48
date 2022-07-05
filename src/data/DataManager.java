package data;

import models.App;

import java.io.*;

/**
 * Class that implements capabilities for loading game data from files and saving it.
 *
 * @author Artem Novak
 */
public class DataManager {

    /**
     * Reads data of the level with given index.
     *
     * @param level container of the level's identifying properties.
     * @return {@link LevelData} object storing information about the level
     * @throws IOException when a loading error occurs or when data is saved incorrectly
     */
    public static LevelData loadLevelData(LevelIdentifier level) throws IOException {
        ObjectInputStream loader = new ObjectInputStream(App.class.getResourceAsStream("/data/levels/"+level.difficulty()+"/level"+level.index()+".dat"));
        LevelData data;
        try {
            data = (LevelData) loader.readObject();
            loader.close();
        }
        catch (ClassCastException | ClassNotFoundException exception) {
            throw new IOException("Level file for " + level + " contains incorrectly saved data");
        }
        return data;
    }

    /**
     * Writes given {@link LevelData} object into a file inside "resources\\data\\levels" as "level" + level's index.
     * <br>
     * Note: this method is not intended for normal runtime use because level data is static.
     *
     * @param data object to be saved.
     * @throws IOException when a saving error occurs
     */
    public static void saveLevelData(LevelData data, File filepath) throws IOException {
        ObjectOutputStream saver = new ObjectOutputStream(new FileOutputStream(filepath));
        saver.writeObject(data);
        saver.close();
    }

    /**
     * Loads current instance of {@link PlayerData}.
     *
     * @return loaded instance
     * @throws IOException when a loading error occurs
     */
    public static PlayerData loadPlayerData() throws IOException {
        ObjectInputStream loader = new ObjectInputStream(new FileInputStream("data/playerData.dat"));
        PlayerData data;
        try {
            data = (PlayerData) loader.readObject();
            loader.close();
        }
        catch (ClassCastException | ClassNotFoundException exception) {
            throw new IOException("data/playerData.dat contains incorrectly saved data");
        }
        return data;
    }

    /**
     * Saves a given instance of {@link PlayerData} as the current player data.
     *
     * @param data instance to be saved
     * @throws IOException when a saving error occurs
     */
    public static void savePlayerData(PlayerData data) throws IOException {
        File folder = new File("data");
        if (!folder.exists()) folder.mkdir();
        ObjectOutputStream saver = new ObjectOutputStream(new FileOutputStream("data/playerData.dat"));
        saver.writeObject(data);
        saver.close();
    }

    /**
     * Saves a new instance of {@link PlayerData} with no progress.
     *
     * @throws IOException when a saving error occurs
     */
    public static void newPlayerData() throws IOException {
        savePlayerData(new PlayerData());
    }

    public static boolean isPlayerDataAvailable() {
        File file = new File("data/playerData.dat");
        return file.exists();
    }
}
