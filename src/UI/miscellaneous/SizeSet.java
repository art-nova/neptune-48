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

        set.enemyWidth = boardSize;
        set.enemyHeight = 130;

        set.healthOffset = 15;
        set.timerWidth = 350;
        set.timerHeight = 70;
        set.timerHealthOffset = 15;
        set.healthWidth = set.boardSize - set.timerWidth - set.timerHealthOffset;
        set.healthHeight = set.timerHeight;

        centerPanelOffset = 15;
        set.attackWidth = 50;
        set.attackHeight = 50;

        set.obstacleSize = 135;

        set.barOffset = 15;
        set.buttonSize = 95;

        set.windowWidth = set.boardSize + 2 * (set.barOffset * 2 + set.buttonSize);
        set.windowHeight = 5 * set.centerPanelOffset + boardSize + set.healthHeight + set.attackHeight + set.enemyHeight;
        return set;
    }
}
