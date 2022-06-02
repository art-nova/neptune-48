package UI.miscellaneous;

/**
 * Set of sizes of elements in the level.
 * @author Artemii Kolomiichuk
 */
public class SizeSet {
    public int windowWidth;
    public int windowHeight;

    public int boardSize;
    public int boardOffsetBig;
    public int boardOffsetSmall;
    public int tileSize;

    public int barOffset;
    public int buttonSize;

    public int enemyWidth;
    public int enemyHeight;

    public int timerWidth;
    public int timerHeight;

    public int timerHealthOffset;

    public int healthWidth;
    public int healthHeight;

    public int healthOutlineWidth;
    public int healthOutlineOffset;

    public int healthOffset;
    public int centerPanelOffset;

    public int attackWidth;
    public int attackHeight;

    public int obstacleSize;

    public SizeSet defaultSizeSet(){
        SizeSet set = new SizeSet();
        set.boardSize = 500;
        set.boardOffsetBig = 15;
        set.boardOffsetSmall = 10;
        set.tileSize = 110;

        set.enemyWidth = set.boardSize;
        set.enemyHeight = 130;

        set.healthOffset = 15;
        set.timerWidth = 130;
        set.timerHeight = 70;
        set.timerHealthOffset = 15;
        set.healthWidth = set.boardSize - set.timerWidth - set.timerHealthOffset;
        set.healthHeight = set.timerHeight;

        set.healthOutlineWidth = 10;
        set.healthOutlineOffset = 5;
        
        set.centerPanelOffset = 15;
        set.attackWidth = 500;
        set.attackHeight = 75;

        set.obstacleSize = 135;

        set.barOffset = 15;
        set.buttonSize = 95;

        set.windowWidth = set.boardSize + 2 * (set.barOffset * 2 + set.buttonSize);
        set.windowHeight = 5 * set.centerPanelOffset + set.boardSize + set.healthHeight + set.attackHeight + set.enemyHeight;
        return set;
    }

    @Override
    public String toString() {
        return "Sizeset{" + "\n" + "windowWidth=" + windowWidth + "\n" + "windowHeight=" + windowHeight + "\n" + "boardSize=" + boardSize + "\n" + "boardOffsetBig=" + boardOffsetBig + "\n" + "boardOffsetSmall=" + boardOffsetSmall + "\n" + "tileSize=" + tileSize + "\n" + "barOffset=" + barOffset + "\n" + "buttonSize=" + buttonSize + "\n" + "enemyWidth=" + enemyWidth + "\n" + "enemyHeight=" + enemyHeight + "\n" + "timerWidth=" + timerWidth + "\n" + "timerHeight=" + timerHeight + "\n" + "timerHealthOffset=" + timerHealthOffset + "\n" + "healthWidth=" + healthWidth + "\n" + "healthHeight=" + healthHeight + "\n" + "healthOffset=" + healthOffset + "\n" + "centerPanelOffset=" + centerPanelOffset + "\n" + "attackWidth=" + attackWidth + "\n" + "attackHeight=" + attackHeight + "\n" + "obstacleSize=" + obstacleSize + "\n" + '}';
    }
}
