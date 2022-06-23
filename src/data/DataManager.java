package data;

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
        String path = "resources\\data\\levels\\"+level.difficulty()+"\\level"+level.index()+".dat";
        ObjectInputStream loader = new ObjectInputStream(new FileInputStream(path));
        LevelData data;
        try {
            data = (LevelData) loader.readObject();
            loader.close();
        }
        catch (ClassCastException | ClassNotFoundException exception) {
            throw new IOException("Level file at \"" + path + "\" contains incorrectly saved data");
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
    public static void saveLevelData(LevelData data) throws IOException {
        ObjectOutputStream saver = new ObjectOutputStream(new FileOutputStream("resources\\data\\levels\\"+data.getLevelIdentifier().difficulty()+"\\level"+data.getLevelIdentifier().index()+".dat"));
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
        ObjectInputStream loader = new ObjectInputStream(new FileInputStream("resources\\data\\playerData.dat"));
        PlayerData data;
        try {
            data = (PlayerData) loader.readObject();
            loader.close();
        }
        catch (ClassCastException | ClassNotFoundException exception) {
            throw new IOException("playerData.dat contains incorrectly saved data");
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
        ObjectOutputStream saver = new ObjectOutputStream(new FileOutputStream("resources\\data\\playerData.dat"));
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
}
