package UI;

import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingConstants;

import data.DataManager;
import data.LevelData;
import data.LevelIdentifier;
import data.PlayerData;
import models.App;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InfoPanels{

    public static JLayeredPane obstacle(String title){
        JLayeredPane panel = new JLayeredPane();
        panel.setPreferredSize(new Dimension(600,131));
        try {
            JLabel image = new JLabel(new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/obstacles/"+ title + ".png"))));
            image.setVisible(true);
            image.setBounds(0,0,600,131);
            panel.add(image);
        } catch (Exception e) {System.out.println("InfoPanels Obstacles: " + title + " " + e.getMessage());}
        return panel;
    }

    public static JLayeredPane buttonNext(LevelIdentifier levelIdentifier){
        JLayeredPane panel = new JLayeredPane();
        panel.setPreferredSize(new Dimension(600,131));
        try {
            JLabel image = new JLabel(new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/next.png"))));
            JLabel imageLight = new JLabel(new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/nextLight.png"))));
            imageLight.setVisible(false);
            image.setVisible(true);
            image.setBounds(0,0,600,131);
            imageLight.setBounds(0,0,600,131);

            image.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseEntered(MouseEvent e) {
                    imageLight.setVisible(true);
                    image.setVisible(false);
                    
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    imageLight.setVisible(false);
                    image.setVisible(true);
                }
            });

            imageLight.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseEntered(MouseEvent e) {
                    imageLight.setVisible(true);
                    image.setVisible(false);
                    
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    imageLight.setVisible(false);
                    image.setVisible(true);
                }
                @Override
                public void mouseClicked(MouseEvent e) {
                    App.loadLevelFromLevels(levelIdentifier);                   
                }
            });
            panel.add(image);
            panel.add(imageLight);
        } catch (Exception e) {}
        
        return panel;
    }

    public static JLayeredPane starsPanel(LevelIdentifier levelIdentifier){
        JLayeredPane panel = new JLayeredPane();

        panel.setPreferredSize(new Dimension(600,300));
        try {
            LevelData levelData = DataManager.loadLevelData(levelIdentifier);
            PlayerData playerData = DataManager.loadPlayerData();

            ImageIcon starHollow = new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/starHollow.png")));
            ImageIcon starFull = new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/starFull.png")));
            JLabel star1 = new JLabel(starHollow);
            star1.setBounds(37,16,170,162);
            JLabel star2 = new JLabel(starHollow);
            star2.setBounds(215,16,170,162);
            JLabel star3 = new JLabel(starHollow);
            star3.setBounds(394,16,170,162);

            JLabel star2Text = new JLabel("null", SwingConstants.CENTER);   
            star2Text.setForeground(new Color(23,63,31));
            star2Text.setFont(App.lexenDeca.deriveFont(App.lexenDeca.getStyle(), 50));
            star2Text.setBounds(254,57+16,93,53);
            JLabel star3Text = new JLabel("null", SwingConstants.CENTER);
            star3Text.setForeground(new Color(23,63,31));
            star3Text.setFont(App.lexenDeca.deriveFont(App.lexenDeca.getStyle(), 50));
            star3Text.setBounds(433,57+16,93,53);

            JLabel overall = new JLabel("Всього ходів: " + levelData.getTurns());
            overall.setForeground(new Color(56,151,74));
            overall.setFont(App.exo2.deriveFont(App.exo2.getStyle(), 40));
            overall.setBounds(36,190,435,47);
            JLabel best;
            try {
                best = new JLabel("Найкращий результат: " + playerData.getLevelTurnsLeft(levelIdentifier));
            } catch (Exception e) {
                best = new JLabel("Найкращий результат: -");
            }
            best.setForeground(new Color(56,151,74));
            best.setFont(App.exo2.deriveFont(App.exo2.getStyle(), 40));
            best.setBounds(36,235,555,47);

            switch(playerData.getLevelStars(levelIdentifier)){
                case 0:
                    star1.setIcon(starHollow);
                    star2.setIcon(starHollow);
                    star3.setIcon(starHollow);
                    star2Text.setText("" + levelData.getTwoStarThreshold());
                    star3Text.setText("" + levelData.getThreeStarThreshold());
                    break;
                case 1:
                    star1.setIcon(starFull);
                    star2.setIcon(starHollow);
                    star3.setIcon(starHollow);
                    star2Text.setText("" + levelData.getTwoStarThreshold());
                    star3Text.setText("" + levelData.getThreeStarThreshold());
                    break;
                case 2:
                    star1.setIcon(starFull);
                    star2.setIcon(starFull);
                    star3.setIcon(starHollow);
                    star3Text.setText("" + levelData.getThreeStarThreshold());
                    break;
                case 3:
                    star1.setIcon(starFull);
                    star2.setIcon(starFull);
                    star3.setIcon(starFull);
                    break;
            }
            if(!star2Text.getText().equals("null")){
                panel.add(star2Text);
            }
            if(!star3Text.getText().equals("null")){
                panel.add(star3Text);
            }
            panel.add(overall);
            panel.add(best);
            panel.add(star1);
            panel.add(star2);
            panel.add(star3);
            
        } catch (Exception e) {System.out.println("InfoPanels Stars: " + e.getMessage());}
        
        return panel;
    }
}
