package data;

import java.io.Serializable;

/**
 * Record that holds basic information needed to identify a level.
 *
 * @author Artem Novak
 */
public record LevelIdentifier(String difficulty, int index) implements Serializable {
    public LevelIdentifier {
        if (!difficulty.equals("normal") && !difficulty.equals("hard"))
            throw new IllegalArgumentException("Unknown difficulty \"" + difficulty + "\"");
        if (index < 0) throw new IllegalArgumentException("Unsupported level index " + index);
    }
}
