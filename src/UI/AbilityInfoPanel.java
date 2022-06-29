package UI;

import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AbilityInfoPanel {
    public Ability[] passive = new Ability[5];
    public Ability[] active = new Ability[7];


    public class Ability extends JLayeredPane{
        ImageIcon light;
        ImageIcon normal;
        ImageIcon dark;
        JLabel image;

        public Ability(String title, boolean isPassive){
            setPreferredSize(new Dimension(600,131));
            if(isPassive){
                title = "passive/" + title;
            }else{
                title = "active/" + title;
            }

            try {
                light = new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/abilities/" + title + "Light.png")));
                normal = new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/abilities/" + title + ".png")));
                dark = new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/abilities/" + title + "Dark.png")));

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
                    Ability[] set = isPassive ? passive : active;
                    for (Ability ability : set) {
                        ability.makeDark();
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
