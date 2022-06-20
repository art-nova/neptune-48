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

public class BonusInfoPanel{
    public Bonus[] passive = new Bonus[5];
    public Bonus[] active = new Bonus[7];


    public class Bonus extends JLayeredPane{
        ImageIcon light;
        ImageIcon normal;
        ImageIcon dark;
        JLabel image;

        public Bonus(String title, boolean isPassive){
            setPreferredSize(new Dimension(600,131));
            if(isPassive){
                title = "passive/" + title;
            }else{
                title = "active/" + title;
            }

            try {
                light = new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/bonuses/" + title + "Light.png")));
                normal = new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/bonuses/" + title + ".png")));
                dark = new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/bonuses/" + title + "Dark.png")));

                image = new JLabel(normal);
                image.setVisible(true);
                image.setBounds(0,0,600,131);
                add(image);
            } catch (Exception e) {}

            image.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    image.setIcon(light);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    image.setIcon(normal);
                }
                //TODO
                @Override
                public void mouseClicked(MouseEvent e) {
                    Bonus[] set = isPassive ? passive : active;
                    for (Bonus bonus : set) {
                        bonus.makeDark();
                    }
                    makeLight();
                }
            });
        }

        public void makeLight(){
            image.setIcon(light);
        }

        public void makeDark(){
            image.setIcon(dark);
        }

        public void makeNormal(){
            image.setIcon(normal);
        }
    }
}
