package UI.miscellaneous;

import UI.LevelGraphics;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import models.App;

/**
 * Set of images for level
 * @author Artemii Kolomiichuk
 */
public class ImageSet {
    public ImageIcon[] enemy;
    public ImageIcon timer;
    //public ImageIcon health;
    public ImageIcon attack;
    
    
    public ImageIcon pause;
    public ImageIcon info;
    public ImageIcon buttonCover;
    //bonuses
        //active
    public ImageIcon safeAttack;
    public ImageIcon crit;
    public ImageIcon swap;
    public ImageIcon upgrade;
    public ImageIcon dispose;
    public ImageIcon merge;
    public ImageIcon scramble;
    public ImageIcon massiveAttack;
        //passive
    public ImageIcon bonusTime;
    public ImageIcon bonusDamage;
    public ImageIcon betterBaseLevel;
    public ImageIcon resistance;
    
    //obstacles
    public ImageIcon downgrade;
    public ImageIcon randomSwap;
    public ImageIcon randomDispose;
    public ImageIcon freeze;
    public ImageIcon garbageTile;
    public ImageIcon subtractTime;
    public ImageIcon randomScramble;
    //tiles
    public ImageIcon[] tile;
    
    public ImageSet(String folder, LevelGraphics level){
        int tileSize = level.sizes.tileSize;
        int buttonSize = level.sizes.buttonSize;

        //int healthWidth = level.sizes.healthWidth;
        //int healthHeight = level.sizes.healthHeight;

        int timerWidth = level.sizes.timerWidth;
        int timerHeight = level.sizes.timerHeight;

        int enemyWidth = level.sizes.enemyWidth;
        int enemyHeight = level.sizes.enemyHeight;

        int attackWidth = level.sizes.attackWidth;
        int attackHeight = level.sizes.attackHeight;
        
        try {
            enemy = new ImageIcon[2];
            enemy[0] = new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/" + folder + "/enemy00.png")).getScaledInstance(enemyWidth, enemyHeight, 1));
            /*
            enemy = new ImageIcon[]{
                new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/" + folder + "/enemy00.png")).getScaledInstance(enemyWidth, enemyHeight, 1)),
                new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/" + folder + "/enemy01.png")).getScaledInstance(enemyWidth, enemyHeight, 1))
                //...
            };
*/
            //health = new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/" + folder + "/health.png")).getScaledInstance(healthWidth, healthHeight, 1));
            buttonCover = new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/" + folder + "/buttonCover.png")).getScaledInstance(buttonSize, buttonSize, 1));
            timer = new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/" + folder + "/timer.png")).getScaledInstance(timerWidth, timerHeight, 1));
            swap = new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/" + folder + "/swap.png")).getScaledInstance(buttonSize, buttonSize, 1));
            attack = new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/" + folder + "/attack.png")).getScaledInstance(attackWidth, attackHeight, 1));
            pause = new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/" + folder + "/pause.png")).getScaledInstance(buttonSize, buttonSize, 1));
            info = new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/" + folder + "/info.png")).getScaledInstance(buttonSize, buttonSize, 1));
            safeAttack = new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/" + folder + "/safeAttack.png")).getScaledInstance(buttonSize, buttonSize, 1));
            crit = new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/" + folder + "/crit.png")).getScaledInstance(buttonSize, buttonSize, 1));
            
            upgrade = new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/" + folder + "/upgrade.png")).getScaledInstance(buttonSize, buttonSize, 1));
            dispose = new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/" + folder + "/dispose.png")).getScaledInstance(buttonSize, buttonSize, 1));
            merge = new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/" + folder + "/merge.png")).getScaledInstance(buttonSize, buttonSize, 1));
            scramble = new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/" + folder + "/scramble.png")).getScaledInstance(buttonSize, buttonSize, 1));
            massiveAttack = new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/" + folder + "/massiveAttack.png")).getScaledInstance(buttonSize, buttonSize, 1));
            bonusTime = new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/" + folder + "/bonusTime.png")).getScaledInstance(buttonSize, buttonSize, 1));
            bonusDamage = new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/" + folder + "/bonusDamage.png")).getScaledInstance(buttonSize, buttonSize, 1));
            betterBaseLevel = new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/" + folder + "/betterBaseLevel.png")).getScaledInstance(buttonSize, buttonSize, 1));
            resistance = new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/" + folder + "/resistance.png")).getScaledInstance(buttonSize, buttonSize, 1));
            downgrade = new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/" + folder + "/downgrade.png")).getScaledInstance(buttonSize, buttonSize, 1));
            randomSwap = new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/" + folder + "/randomSwap.png")).getScaledInstance(buttonSize, buttonSize, 1));
            randomDispose = new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/" + folder + "/randomDispose.png")).getScaledInstance(buttonSize, buttonSize, 1));
            freeze = new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/" + folder + "/freeze.png")).getScaledInstance(buttonSize, buttonSize, 1));
            garbageTile = new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/" + folder + "/garbageTile.png")).getScaledInstance(buttonSize, buttonSize, 1));
            subtractTime = new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/" + folder + "/subtractTime.png")).getScaledInstance(buttonSize, buttonSize, 1));
            randomScramble = new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/" + folder + "/randomScramble.png")).getScaledInstance(buttonSize, buttonSize, 1));
            
            tile = new ImageIcon[]{
                new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/" + folder + "/tile00.png")).getScaledInstance(tileSize, tileSize, 1)),
                new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/" + folder + "/tile01.png")).getScaledInstance(tileSize, tileSize, 1)),
                new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/" + folder + "/tile02.png")).getScaledInstance(tileSize, tileSize, 1)),
                new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/" + folder + "/tile03.png")).getScaledInstance(tileSize, tileSize, 1)),
                new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/" + folder + "/tile04.png")).getScaledInstance(tileSize, tileSize, 1)),
                new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/" + folder + "/tile05.png")).getScaledInstance(tileSize, tileSize, 1)),
                new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/" + folder + "/tile06.png")).getScaledInstance(tileSize, tileSize, 1)),
                new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/" + folder + "/tile07.png")).getScaledInstance(tileSize, tileSize, 1))
                //...
            };
        } catch(Exception e){System.err.println("ImageSet '" + folder + "' couldn't be loaded");}
    }
}
