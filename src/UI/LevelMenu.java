package UI;
import java.awt.Dimension;

import java.awt.Color;
import javax.swing.JFrame;
import javax.xml.crypto.Data;
import javax.swing.*;

import UI.miscellaneous.FilledBox;
import data.DataManager;
import data.LevelData;
import data.LevelIdentifier;
import data.PlayerData;
import game.GamePanel;
import game.gameobjects.Board;
import models.App;

import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 * @see models.App#getLevelMenu
 */
public class LevelMenu extends JFrame{

    Color darkGreen = new Color(23,62,31);
    Color lightGreen = new Color(42,113,56);
    int width, height;
    JLayeredPane pane;
    static JLabel turnsLeftCounter;
    static Healthbar healthBar;
    /**
     * Cover over JFrame
     * @see #isVisible
     */
    static JLayeredPane cover;

    static Ability activeAbility1;
    static Ability activeAbility2;
    static Ability attack;
    static JLabel passiveAbility;
    static LevelIdentifier levelIdentifier;

    static PlayerData playerData;
    static LevelData levelData;
    static GamePanel gamePanel;

    


    void init(LevelIdentifier levelIdentifier){
        LevelMenu.levelIdentifier = levelIdentifier;
        try {
            playerData = DataManager.loadPlayerData();
            levelData = DataManager.loadLevelData(levelIdentifier);
            setHealthbarMaxValue((int)levelData.getEntityHealth());
            setHealthbarValue((int)levelData.getEntityHealth());
            setTurnsLeft(levelData.getTurns());

            gamePanel = new GamePanel(levelData, playerData, this);
            gamePanel.setBounds(160, 125, gamePanel.getPreferredSize().width, gamePanel.getPreferredSize().height);
            pane.add(gamePanel, 0);

            attack.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1 && !attack.cover.isVisible()) gamePanel.getAbilityManager().attemptAttack();
                    else if (e.getButton() == MouseEvent.BUTTON3 && gamePanel.getBoard().getState() == Board.SELECTING) gamePanel.getActionHandler().scheduleAction("abortSelection");
                }
            });

            passiveAbility.setIcon(getIcon("level/coverPassive.png", 110,110));
            if(playerData.getActiveAbility1() != null){
                activeAbility1.setAbility("level/" + playerData.getActiveAbility1() + ".png", playerData.getActiveAbility1());
                activeAbility1.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1 && !activeAbility1.cover.isVisible()) gamePanel.getAbilityManager().attemptActive1();
                        else if (e.getButton() == MouseEvent.BUTTON3 && gamePanel.getBoard().getState() == Board.SELECTING) gamePanel.getActionHandler().scheduleAction("abortSelection");
                    }
                });
            }
            if(playerData.getActiveAbility2() != null){
                activeAbility2.setAbility("level/" + playerData.getActiveAbility2() + ".png", playerData.getActiveAbility2());
                activeAbility2.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1 && !activeAbility2.cover.isVisible()) gamePanel.getAbilityManager().attemptActive2();
                        else if (e.getButton() == MouseEvent.BUTTON3 && gamePanel.getBoard().getState() == Board.SELECTING) gamePanel.getActionHandler().scheduleAction("abortSelection");
                    }
                });
            }
            if(playerData.getPassiveAbility() != null){
                passiveAbility.setIcon(getIcon("level/" + playerData.getPassiveAbility() + ".png", 110,110));
            }
        } catch (Exception e) {
            System.err.println("Error loading level data: " + e.getMessage());
        }
    }   

    public void setWinOverlay(int stars){
        cover = winOverlay(stars);
        add(cover,0);
        cover.setVisible(true);
        revalidate();
        repaint();
    }

    public void setLoseOverlay(){
        cover = loseOverlay();
        add(cover,0);
        cover.setVisible(true);
        revalidate();
        repaint();
    }

    public void setPauseOverlay(){
        cover = pauseOverlay();
        add(cover, 0);
        cover.setVisible(true);
        revalidate();
        repaint();
    }

    public void clearOverlay() {
        remove(cover);
        cover = null;
        revalidate();
        repaint();
    }

    public Ability getActiveAbility1() {
        return activeAbility1;
    }

    public Ability getActiveAbility2() {
        return activeAbility2;
    }

    public Ability getAttack() {
        return attack;
    }

    public JLabel getPassiveAbility() {
        return passiveAbility;
    }

    /**
     * Sets the value of the turns left counter
     * @param value
     */
    public void setTurnsLeft(int value){
        turnsLeftCounter.setText(String.valueOf(value));
    }
    /**
     * Sets the value of the healthbar
     * @param value
     */
    public void setHealthbarValue(int value){
        healthBar.setValue(value);
    }

    /**
     * Sets max malue of the healthbar
     * @param maxValue
     */
    public static void setHealthbarMaxValue(int maxValue){
        healthBar.setMaxValue(maxValue);
        healthBar.setVisible(true);
    }
    
    public LevelMenu(LevelIdentifier levelIdentifier) {
        super("Neptune-48");
        setResizable(false);
        this.width = 815;
        this.height = 1000;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFocusable(true);
        setSize(width + 15, height + 15);

        pane = new JLayeredPane();
        pane.setPreferredSize(new Dimension(width, height));
        pane.setBounds(0,0,width,height);

        setBackground(lightGreen);
        FilledBox background = new FilledBox(lightGreen);
        background.setBounds(0,79,width,height - 79);
        pane.add(background, 0);

        FilledBox topPanel = new FilledBox(darkGreen);
        topPanel.setBounds(0,0,width,79);
        pane.add(topPanel, 0);

        FilledBox pauseCover = new FilledBox(new Color(52,148,71));
        pauseCover.setBounds(15,12,125,55);
        pauseCover.setVisible(false);
        pauseCover.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) gamePanel.getActionHandler().scheduleAction("pause");
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                pauseCover.setVisible(true);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                pauseCover.setVisible(false);
            }
        });

        FilledBox pauseBack = new FilledBox(lightGreen);
        pauseBack.setBounds(15,12,125,55);
        pauseBack.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) gamePanel.getActionHandler().scheduleAction("pause");
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                pauseCover.setVisible(true);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                pauseCover.setVisible(false);
            }
        });
        pane.add(pauseBack, 0);
        pane.add(pauseCover, 0);
        FilledBox pause1 = new FilledBox(darkGreen);
        pause1.setBounds(56,22,13,36);
        pane.add(pause1, 0);
        FilledBox pause2 = new FilledBox(darkGreen);
        pause2.setBounds(84,22,13,36);
        pane.add(pause2, 0);

        


        FilledBox countdownBack = new FilledBox(lightGreen);
        countdownBack.setBounds(660,12,125,55);
        pane.add(countdownBack, 0);

        turnsLeftCounter = new JLabel("999");
        turnsLeftCounter.setHorizontalAlignment(SwingConstants.CENTER);
        turnsLeftCounter.setForeground(new Color(23,62,31));

        turnsLeftCounter.setFont(App.lexenDeca.deriveFont(App.lexenDeca.getStyle(), 45));
        turnsLeftCounter.setBounds(668,14,106,47);
        pane.add(turnsLeftCounter, 0);

        FilledBox healhBarBack = new FilledBox(lightGreen);
        healhBarBack.setBounds(160,12,480,55);
        pane.add(healhBarBack, 0);

        healthBar = new Healthbar(350);
        pane.add(healthBar, Integer.valueOf(100));

        FilledBox boardBack = new FilledBox(darkGreen);
        boardBack.setBounds(25,458 + 50,755,300);
        pane.add(boardBack, 0);

        activeAbility1 = new Ability("level/cover.png", "empty");
        activeAbility1.setBounds(40,480 + 50,110,110);
        pane.add(activeAbility1, 0);

        activeAbility2 = new Ability("level/cover.png", "empty");
        activeAbility2.setBounds(40,625 + 50,110,110);
        pane.add(activeAbility2, 0);

        try {
            if(DataManager.loadLevelData(levelIdentifier).getGameMode() == GamePanel.GAME_MODE_REPAIR){
                attack = new Ability("level/repair.png", "attack");
            }
            else{
                attack = new Ability("level/attack.png", "attack");
            }
        } catch (Exception e) {}
        

        
        attack.setBounds(655,480 + 50,110,110);
        pane.add(attack, 0);

        passiveAbility = new  JLabel(getIcon("level/attack.png", 110, 110));
        passiveAbility.setBounds(655,625 + 50,110,110);
        pane.add(passiveAbility, 0);

        init(levelIdentifier);
        add(pane);
        revalidate();
        repaint();
        setVisible(true);
    }

    static class Healthbar extends JPanel{
        static int width;
        Color color = new Color(240,54,42);
        int maxValue, value;
        JLabel healthLeft;
        FilledBox healthBack;

        public Healthbar(int maxValue){
            super();
            this.maxValue = maxValue;
            this.value = maxValue;
            setBounds(167,19,466,41);
            setLayout(null);
            setBackground(new Color(23,62,31));
            healthBack = new FilledBox(color);
            healthBack.setBounds(0,0,466,41);
            healthLeft = new JLabel("" + maxValue);
            healthLeft.setBounds(15,-5,232,48);
            healthLeft.setForeground(new Color(240,248,217));
            healthLeft.setFont(App.lexenDeca.deriveFont(App.lexenDeca.getStyle(), 40));
            add(healthLeft);
            add(healthBack);
           
        }

        public void setMaxValue(int value){
            this.maxValue = value;
        }

        /**
         * Sets the value of the healthbar
         * @param value
         */
        public void setValue(int value){
            if(value < 0 || value > maxValue){
                throw new IllegalArgumentException("LevelMenu -> HealthBar -> setValue value must be between 0 and maxValue (" + maxValue + ")");
            }
            this.value = value;
            resetWidth();
        }

        private void resetWidth(){
            width = (int)((double)value / maxValue * 466);
            healthBack.setBounds(0,0,width,41);
            healthLeft.setText("" + value);
        }
    }

    /**
     * Active ability or attack button with cover and number over it
     */
    public class Ability extends JLayeredPane{
        String nameID;
        JLabel icon;
        JLabel highlight;
        JLabel cover;
        JLabel coverNumber;
        int coverNum;
        boolean isEmpty;
        boolean hovered;

        public Ability(String path, String nameIDentifier){
            super();
            nameID = nameIDentifier;
            isEmpty = nameID.equals("empty");
            coverNum = 0;
            icon = new JLabel(LevelMenu.getIcon(path, 110,110));
            icon.setBounds(0,0,110,110);
            cover = new JLabel(LevelMenu.getIcon("level/cover.png", 110,110));
            cover.setBounds(0,0,110,110);
            highlight = new JLabel(LevelMenu.getIcon("level/highlight.png", 110,110));
            highlight.setBounds(0,0,110,110);
            coverNumber = new JLabel("" + coverNum, SwingConstants.CENTER);
            coverNumber.setBounds(0,0,110,110);
            coverNumber.setForeground(new Color(221,233,188));
            coverNumber.setFont(App.lexenDeca.deriveFont(App.lexenDeca.getStyle(), 52));

            add(icon, 0);
            add(cover, 0);
            add(highlight, 0);
            add(coverNumber, 0);
            icon.setVisible(true);
            highlight.setVisible(false);
            cover.setVisible(false);
            coverNumber.setVisible(false);

            addMouseListener(new MouseAdapter(){
                @Override
                public void mouseEntered(MouseEvent e) {
                    if(!isEmpty){
                        if((LevelMenu.cover == null) || (!LevelMenu.cover.isVisible())) {
                            if(!cover.isVisible()){
                                highlight.setVisible(true);
                            }
                        }
                        hovered = true;
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    highlight.setVisible(false);
                    hovered = false;
                }
            });
        }

        /**
         * Sets ability to not empty
         * @param path in "folder/file.png" format
         * @param nameID
         */
        public void setAbility(String path, String nameID){
            isEmpty = nameID.equals("empty");
            coverNum = 0;
            this.nameID = nameID;
            icon.setIcon(getIcon(path, 110,110));
        }

        /**
         * Used to change highlight visibility programmatically (for non-empty slots only).
         */
        public void setHighlightVisible(boolean visible) {
            if (!isEmpty) highlight.setVisible(visible);
        }

        public boolean isHovered() {
            return hovered;
        }

        /**
         * Makes the cover of the ability visible and sets it to given number.
         * Numbers less than 1 do not display.
         */
        public void setCover(int num){
            cover.setVisible(true);
            coverNum = num;
            coverNumber.setText("" + coverNum);
            coverNumber.setVisible(num > 0);
            highlight.setVisible(false);
        }

        /**
         * Removes the cover on the ability and makes the cover number 0.
         */
        public void removeCover(){
            cover.setVisible(false);
            coverNumber.setVisible(false);
            coverNum = 0;
            highlight.setVisible(hovered);
        }

    }

    /**
     * Returns an ImageIcon
     * @param location in "folder/image.png" format
     */
    static ImageIcon getIcon(String location, int width, int height){
        try {
            return new ImageIcon(ImageIO.read(new File("resources/images/" + location)).getScaledInstance(width, height, 16));
        } catch (Exception e) {System.err.println("LevelMenu -> getIcon("+location+") -> " + e.getMessage());}
        return null;
    }


    public static JLayeredPane pauseOverlay(){
        JLayeredPane pane = new JLayeredPane();
        try {
            pane.setBounds(0,0,815,1000);
            JPanel centerPanel = new JPanel();
            centerPanel.setLayout(null);
            centerPanel.setBackground(new Color(23,63,31));
            centerPanel.setBounds(100,360,600,280);
            
            ImageIcon back = new ImageIcon(ImageIO.read(new File("resources/images/level/toMenu.png")));
            ImageIcon backLight = new ImageIcon(ImageIO.read(new File("resources/images/level/toMenuLight.png")));
            JLabel backButton = new JLabel(back);
            backButton.setBounds(24,161,551,81);
            backButton.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseEntered(MouseEvent e){
                    backButton.setIcon(backLight);
                }
                @Override
                public void mouseExited(MouseEvent e){
                    backButton.setIcon(back);
                }
                @Override
                public void mouseClicked(MouseEvent e){
                    gamePanel.close();
                    cover = null;
                    App.loadLevelsFromLevel();
                }
            });

            ImageIcon restart = new ImageIcon(ImageIO.read(new File("resources/images/level/restart.png")));
            ImageIcon restartLight = new ImageIcon(ImageIO.read(new File("resources/images/level/restartLight.png")));
            JLabel restartButton = new JLabel(restart);
            restartButton.setBounds(24,38,551,81);
            restartButton.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseEntered(MouseEvent e){
                    restartButton.setIcon(restartLight);
                }
                @Override
                public void mouseExited(MouseEvent e){
                    restartButton.setIcon(restart);
                }
                @Override
                public void mouseClicked(MouseEvent e){
                    gamePanel.close();
                    cover = null;
                    App.loadLevelFromLevel(levelIdentifier);
                }
            });

            centerPanel.add(restartButton);
            centerPanel.add(backButton);
            pane.add(centerPanel);
            FilledBox outline = new FilledBox(new Color(42,113,56));
            outline.setBounds(90,350,620,300);
            pane.add(outline);

            FilledBox backGroundDark = new FilledBox(new Color(0,0,0,103));
            backGroundDark.setBounds(0,0,815,1000);
            pane.add(backGroundDark);
            backGroundDark.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent e) {
                    gamePanel.getActionHandler().scheduleAction("unpause");
                }
            });
            pane.setVisible(true);
        } catch (Exception e) {System.err.println("LevelMenu -> pauseOverlay() -> " + e.getMessage());}
        return pane;
    }

    public static JLayeredPane winOverlay(int stars){
        JLayeredPane pane = new JLayeredPane();
        try {
            pane.setBounds(0,0,815,1000);
            JPanel centerPanel = new JPanel();
            centerPanel.setLayout(null);
            centerPanel.setBackground(new Color(23,63,31));
            centerPanel.setBounds(100,160,600,680);


            JLabel win = new JLabel("Перемога", SwingConstants.CENTER); 
            win.setForeground(new Color(56,151,74));
            win.setFont(App.exo2.deriveFont(App.exo2.getStyle(), 100));
            win.setBounds(35,10,530,115);
            centerPanel.add(win);

            

            
            var panel = InfoPanels.starsPanel(levelIdentifier, stars);
            panel.setBounds(0,20 + 103,600,300);
            centerPanel.add(panel);
            JLabel yours = new JLabel("Ваш результат: " + turnsLeftCounter.getText());
            yours.setForeground(new Color(56,151,74));
            yours.setFont(App.exo2.deriveFont(App.exo2.getStyle(), 40));
            yours.setBounds(36,301 + 103,435,47);
            centerPanel.add(yours);
            


            ImageIcon back = new ImageIcon(ImageIO.read(new File("resources/images/level/toMenu.png")));
            ImageIcon backLight = new ImageIcon(ImageIO.read(new File("resources/images/level/toMenuLight.png")));
            JLabel backButton = new JLabel(back);
            backButton.setBounds(24,611 - 35,551,81);
            backButton.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseEntered(MouseEvent e){
                    backButton.setIcon(backLight);
                }
                @Override
                public void mouseExited(MouseEvent e){
                    backButton.setIcon(back);
                }
                @Override
                public void mouseClicked(MouseEvent e){
                    App.loadLevelsFromLevel();
                }
            });

            ImageIcon restart = new ImageIcon(ImageIO.read(new File("resources/images/level/restart.png")));
            ImageIcon restartLight = new ImageIcon(ImageIO.read(new File("resources/images/level/restartLight.png")));
            JLabel restartButton = new JLabel(restart);
            restartButton.setBounds(24,538- 50 - 20,551,81);
            restartButton.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseEntered(MouseEvent e){
                    restartButton.setIcon(restartLight);
                }
                @Override
                public void mouseExited(MouseEvent e){
                    restartButton.setIcon(restart);
                }
                @Override
                public void mouseClicked(MouseEvent e){
                    App.loadLevelFromLevel(levelIdentifier);
                }
            });

            centerPanel.add(restartButton);
            centerPanel.add(backButton);
            pane.add(centerPanel);
            FilledBox outline = new FilledBox(new Color(42,113,56));
            outline.setBounds(90,150,620,700);
            pane.add(outline);

            FilledBox backGroundDark = new FilledBox(new Color(0,0,0,103));
            backGroundDark.setBounds(0,0,815,1000);
            pane.add(backGroundDark);
            pane.setVisible(true);
        } catch (Exception e) {System.err.println("LevelMenu -> pauseOverlay() -> " + e.getMessage());}
        return pane;
    }

    @Override
    public String toString() {
        return "LevelMenu:\n |" + activeAbility1.nameID + "|\n    |" + activeAbility2.nameID + "|\n   |" + attack.nameID + "|\n|"; 
    }


    public static JLayeredPane loseOverlay(){
        JLayeredPane pane = new JLayeredPane();
        try {
            pane.setBounds(0,0,815,1000);
            JPanel centerPanel = new JPanel();
            centerPanel.setLayout(null);
            centerPanel.setBackground(new Color(23,63,31));
            centerPanel.setBounds(100,160,600,680);

            JLabel lose = new JLabel("Програш", SwingConstants.CENTER);
            lose.setForeground(new Color(56,151,74));
            lose.setFont(App.exo2.deriveFont(App.exo2.getStyle(), 100));
            lose.setBounds(35,10,530,115);
            centerPanel.add(lose);

            

            
            var panel = InfoPanels.starsPanel(levelIdentifier, 0);
            panel.setBounds(0,20 + 112,600,300);
            centerPanel.add(panel);
            
            ImageIcon back = new ImageIcon(ImageIO.read(new File("resources/images/level/toMenu.png")));
            ImageIcon backLight = new ImageIcon(ImageIO.read(new File("resources/images/level/toMenuLight.png")));
            JLabel backButton = new JLabel(back);
            backButton.setBounds(24,611 - 35,551,81);
            backButton.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseEntered(MouseEvent e){
                    backButton.setIcon(backLight);
                }
                @Override
                public void mouseExited(MouseEvent e){
                    backButton.setIcon(back);
                }
                @Override
                public void mouseClicked(MouseEvent e){
                    App.loadLevelsFromLevel();
                }
            });

            ImageIcon restart = new ImageIcon(ImageIO.read(new File("resources/images/level/restart.png")));
            ImageIcon restartLight = new ImageIcon(ImageIO.read(new File("resources/images/level/restartLight.png")));
            JLabel restartButton = new JLabel(restart);
            restartButton.setBounds(24,538- 50 - 20,551,81);
            restartButton.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseEntered(MouseEvent e){
                    restartButton.setIcon(restartLight);
                }
                @Override
                public void mouseExited(MouseEvent e){
                    restartButton.setIcon(restart);
                }
                @Override
                public void mouseClicked(MouseEvent e){
                    App.loadLevelFromLevel(levelIdentifier);
                }
            });

            centerPanel.add(restartButton);
            centerPanel.add(backButton);
            pane.add(centerPanel);
            FilledBox outline = new FilledBox(new Color(42,113,56));
            outline.setBounds(90,150,620,700);
            pane.add(outline);

            FilledBox backGroundDark = new FilledBox(new Color(0,0,0,103));
            backGroundDark.setBounds(0,0,815,1000);
            pane.add(backGroundDark);
            pane.setVisible(true);
        } catch (Exception e) {System.err.println("LevelMenu -> pauseOverlay() -> " + e.getMessage());}
        return pane;
    }
    
}
