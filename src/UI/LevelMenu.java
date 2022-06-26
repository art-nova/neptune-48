package UI;
import java.awt.Dimension;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.*;

import UI.miscellaneous.FilledBox;
import UI.miscellaneous.Utilities;

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
        //healthBar.setValue(400);
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

        FilledBox bonus1 = new FilledBox(Utilities.randomColor());
        bonus1.setBounds(40,480,110,110);
        pane.add(bonus1, 0);

        FilledBox bonus1NumBack = new FilledBox(Utilities.randomColor());
        bonus1NumBack.setBounds(110,550,55,55);
        pane.add(bonus1NumBack, 0);

        FilledBox bonus2 = new FilledBox(Utilities.randomColor());
        bonus2.setBounds(40,625,110,110);
        pane.add(bonus2, 0);

        FilledBox bonus2NumBack = new FilledBox(Utilities.randomColor());
        bonus2NumBack.setBounds(110,695,55,55);
        pane.add(bonus2NumBack, 0);

        FilledBox attack = new FilledBox(Utilities.randomColor());
        attack.setBounds(655,480,110,110);
        pane.add(attack, 0);

        FilledBox passiveBonus = new FilledBox(Utilities.randomColor());
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
}
