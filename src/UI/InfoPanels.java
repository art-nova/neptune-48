package UI;

import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import models.App;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InfoPanels{

    public static JLayeredPane obstacle(String title){
        JLayeredPane panel = new JLayeredPane();
        panel.setPreferredSize(new Dimension(600,131));
        try {
            JLabel image = new JLabel(new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/obstacles/"+ title + ".png"))));
            JLabel imageLight = new JLabel(new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/obstacles/" + title + "Light.png"))));
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
            });
            panel.add(image);
            panel.add(imageLight);
        } catch (Exception e) {System.out.println("InfoPanels Obstacles: " + title + " " + e.getMessage());}
        return panel;
    }

    public static JLayeredPane buttonNext(){
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
                    App.loadLevelFromLevels();                   
                }
            });
            panel.add(image);
            panel.add(imageLight);
        } catch (Exception e) {}
        
        return panel;
    }
}
