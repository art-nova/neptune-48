package UI;

import java.io.File;
import java.awt.*;
import UI.*;
import UI.miscellaneous.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.*;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AbilityInfoPanel {
    public AbilityBar[] passive = new AbilityBar[5];
    public AbilityBar[] active = new AbilityBar[7];

    public static JLayeredPane getAbilitiesPanel(){
        JLayeredPane pane = new JLayeredPane();
        pane.setBounds(0,0,800,1000);

        AbilityInfoPanel abilityInfoPanel = new AbilityInfoPanel();

        abilityInfoPanel.passive[0] = abilityInfoPanel.new AbilityBar("bonusTime", true);
        abilityInfoPanel.passive[1] = abilityInfoPanel.new AbilityBar("resistance", true);
        abilityInfoPanel.passive[2] = abilityInfoPanel.new AbilityBar("betterBaseLevel", true);
        abilityInfoPanel.passive[3] = abilityInfoPanel.new AbilityBar("cooldown", true);
        abilityInfoPanel.passive[4] = abilityInfoPanel.new AbilityBar("bonusDamage", true);
        
        abilityInfoPanel.active[0] = abilityInfoPanel.new AbilityBar("swap", false);
        abilityInfoPanel.active[1] = abilityInfoPanel.new AbilityBar("crit", false);
        abilityInfoPanel.active[2] = abilityInfoPanel.new AbilityBar("merge", false);
        abilityInfoPanel.active[3] = abilityInfoPanel.new AbilityBar("dispose", false);
        abilityInfoPanel.active[4] = abilityInfoPanel.new AbilityBar("safeAttack", false);
        abilityInfoPanel.active[5] = abilityInfoPanel.new AbilityBar("upgrade", false);
        abilityInfoPanel.active[6] = abilityInfoPanel.new AbilityBar("scramble", false);
        

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        try {
            for (AbilityBar ability : abilityInfoPanel.passive) {
                centerPanel.add(ability);
            }
            for (AbilityBar ability : abilityInfoPanel.active) {
                centerPanel.add(ability);
            }
            //centerPanel.add(UI.InfoPanels.buttonNext());
        } catch (Exception e) {}
        
        
        

        JScrollPane scroll = new JScrollPane(centerPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBounds(100,100,600,800);
        scroll.getVerticalScrollBar().setUI(new LevelsMenu.CustomScrollBarUI());
        scroll.getVerticalScrollBar().setUnitIncrement(LevelsMenu.scrollSpeed);

        pane.add(scroll);
        try {
            JLabel image = new JLabel(new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/panel.png"))));
            image.setBounds(90,90,622,822);
            pane.add(image);
        } catch (Exception e) {System.out.println(e);}
        FilledBox back = new FilledBox(new Color(0,0,0,103));
        back.setBounds(0,0,800,1000);
        pane.add(back);
        pane.setVisible(true);
        return pane;
    }


    public class AbilityBar extends JLayeredPane{
        ImageIcon light;
        ImageIcon normal;
        ImageIcon dark;
        ImageIcon locked;
        ImageIcon checked;
        JLabel image;
        /*
         * unavailable
         * selectable
         * chosen
         * locked
         */
        String state;
        JLabel overlay;

       


        public AbilityBar(String title, boolean isPassive){
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
                    AbilityBar[] set = isPassive ? passive : active;
                    for (AbilityBar abilityBar : set) {
                        abilityBar.makeDark();
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
