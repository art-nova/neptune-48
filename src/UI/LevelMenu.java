package UI;
import java.awt.Dimension;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.*;

import UI.miscellaneous.FilledBox;
import UI.miscellaneous.Utilities;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LevelMenu extends JFrame{

    Color darkGreen = new Color(23,62,31);
    Color lightGreen = new Color(42,113,56);
    int width, height;
    JLayeredPane pane;
    
    public LevelMenu() {
        super("Level");
        loadSettings();
        this.width = 800;
        this.height = 900;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width + 15, height + 15);

        pane = new JLayeredPane();
        pane.setPreferredSize(new Dimension(width, height));
        pane.setBounds(0,0,width,height);

        FilledBox background = new FilledBox(lightGreen);
        background.setBounds(0,79,width,height - 79);
        pane.add(background, 0);

        FilledBox topPanel = new FilledBox(darkGreen);
        topPanel.setBounds(0,0,width,79);
        pane.add(topPanel, 0);

        FilledBox pauseBack = new FilledBox(lightGreen);
        pauseBack.setBounds(15,12,125,55);
        pane.add(pauseBack, 0);

        FilledBox countdownBack = new FilledBox(lightGreen);
        countdownBack.setBounds(660,12,125,55);
        pane.add(countdownBack, 0);

        FilledBox healhBarBack = new FilledBox(lightGreen);
        healhBarBack.setBounds(160,12,480,55);
        pane.add(healhBarBack, 0);

        Healthbar healthBar = new Healthbar(1000);
        healthBar.setValue(400);
        pane.add(healthBar, 0);

        FilledBox enemy = new FilledBox(Utilities.randomColor());
        enemy.setBounds(160,125,480,200);
        pane.add(enemy, 0);

        FilledBox boardBack = new FilledBox(darkGreen);
        boardBack.setBounds(25,458,755,300);
        pane.add(boardBack, 0);

        FilledBox board = new FilledBox(Utilities.randomColor());
        board.setBounds(160,365,480,480);
        pane.add(board, 0);

        Bonus bonus1 = new Bonus("level/attack.png", "a");
        bonus1.setBounds(40,480,110,110);
        pane.add(bonus1, 0);

        Bonus bonus2 = new Bonus("level/attack.png", "b");
        bonus2.setBounds(40,625,110,110);
        bonus2.setCover(14);
        pane.add(bonus2, 0);

        Bonus attack = new Bonus("level/attack.png", "attack");
        attack.setBounds(655,480,110,110);
        pane.add(attack, 0);

        JLabel passiveBonus = new  JLabel(getIcon("level/attack.png", 110, 110));
        passiveBonus.setBounds(655,625,110,110);
        pane.add(passiveBonus, 0);



        add(pane);
        revalidate();
        repaint();
        setVisible(true);
    }

    private void loadSettings() {
        // TODO Auto-generated method stub
    }

    class Healthbar extends JPanel{

        Color color = new Color(240,54,42);
        int maxValue, value;
        public Healthbar(int maxValue){
            super();
            this.maxValue = maxValue;
            this.value = maxValue;
            setBounds(167,19,466,41);
            setBackground(color);
        }

        public void setValue(int value){
            if(value < 0 || value > maxValue){
                throw new IllegalArgumentException("LevelMenu -> HealthBar -> setValue value must be between 0 and maxValue (" + maxValue + ")");
            }
            this.value = value;
            resetWidth();
        }

        private void resetWidth(){
            width = (int)((double)value / maxValue * 466);
            setBounds(167,19,width,41);
        }
    }

    class Bonus extends JLayeredPane{
        String nameID;
        JLabel icon;
        JLabel highlight;
        JLabel cover;
        JLabel coverNumber;
        int coverNum;

        public Bonus(String path, String nameID){
            super();
            this.nameID = nameID;
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
            coverNumber.setFont(new java.awt.Font("Lexend Deca", 0, 52));

            add(icon, 0);
            add(cover, 0);
            add(highlight, 0);
            add(coverNumber, 0);
            icon.setVisible(true);
            cover.setVisible(false);
            highlight.setVisible(false);
            coverNumber.setVisible(false);

            icon.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseEntered(MouseEvent e) {
                    if(coverNum < 1)
                        highlight.setVisible(true);
                }
            });

            highlight.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println("clicked " + nameID);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    highlight.setVisible(false);
                }
            });
        }

        public void setCover(int num){
            cover.setVisible(true);
            coverNumber.setVisible(true);
            coverNum = num;
            coverNumber.setText("" + coverNum);
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
    
}
