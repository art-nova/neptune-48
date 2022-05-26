package UI;

import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import models.App;

/**
 * Set of images for level
 * @author Artemii Kolomiichuk
 */
public class ImageSet {
    public ImageIcon enemy;
    public ImageIcon pause;
    public ImageIcon info;
    
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
    
    public ImageIcon[] tile;
    
    public static ImageSet neptun(int tileSize){
        ImageSet set = new ImageSet();
        try {
            set.enemy = new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/neptun/enemy.png")).getScaledInstance(000,000, 1));
            set.pause = new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/neptun/pause.png")).getScaledInstance(000,000, 1));
            //...
            set.tile = new ImageIcon[]{
                new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/neptun/tile00.png")).getScaledInstance(tileSize,tileSize, 1)),
                new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/neptun/tile01.png")).getScaledInstance(tileSize,tileSize, 1)),
                new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/neptun/tile02.png")).getScaledInstance(tileSize,tileSize, 1)),
                new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/neptun/tile03.png")).getScaledInstance(tileSize,tileSize, 1)),
                new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/neptun/tile04.png")).getScaledInstance(tileSize,tileSize, 1)),
                new ImageIcon(ImageIO.read(new File(App.PATH  + "/images/neptun/tile05.png")).getScaledInstance(tileSize,tileSize, 1))
                //...
            };
        } catch(Exception e){System.err.println("ImageSet neptun couldn't be loaded");}
        return set;
    }
}
