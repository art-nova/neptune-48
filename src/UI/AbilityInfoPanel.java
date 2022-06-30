package UI;

import java.io.File;
import java.awt.*;
import UI.LevelsMenu.TopPanel;
import UI.miscellaneous.*;
import models.App;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.*;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AbilityInfoPanel {

    public AbilityBar[] passive = new AbilityBar[5];
    public AbilityBar[] active = new AbilityBar[7];
    public TopPanel topPanel = LevelsMenu.topPanel;
    public static JLayeredPane pane;

    public static JLayeredPane getAbilitiesPanel(){

        pane = new JLayeredPane();
        pane.setBounds(15,152,770,796);
        FilledBox backStroke = new FilledBox(new Color(56,151,74));
        backStroke.setBounds(15,152,770,796);
        pane.add(backStroke, new Integer(1));
        FilledBox back = new FilledBox(new Color(23,63,31));
        back.setBounds(20,157,760,786);
        pane.add(back, new Integer(2));



        AbilityInfoPanel abilityInfoPanel = new AbilityInfoPanel();

        abilityInfoPanel.passive[0] = abilityInfoPanel.new AbilityBar("bonusTime", true);
        abilityInfoPanel.passive[1] = abilityInfoPanel.new AbilityBar("resistance", true);
        abilityInfoPanel.passive[2] = abilityInfoPanel.new AbilityBar("betterBaseLevel", true);
        abilityInfoPanel.passive[3] = abilityInfoPanel.new AbilityBar("cooldownReduction", true);
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
        centerPanel.add(saveButton());
        try {
            for (AbilityBar ability : abilityInfoPanel.passive) {
                centerPanel.add(ability);
            }
            for (AbilityBar ability : abilityInfoPanel.active) {
                centerPanel.add(ability);
            }
            //centerPanel.add(UI.InfoPanels.buttonNext());
        } catch (Exception e) {}
        centerPanel.setBackground(new Color(23,63,31));
        
        

        JScrollPane scroll = new JScrollPane(centerPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBounds(30,157,750,786);
        
        scroll.getVerticalScrollBar().setUI(new LevelsMenu.CustomScrollBarUI());
        scroll.getVerticalScrollBar().setUnitIncrement(LevelsMenu.scrollSpeed);

        pane.add(scroll, new Integer(3));
        pane.setVisible(true);
        return pane;
    }

    public static JLayeredPane saveButton(){
        JLayeredPane panel = new JLayeredPane();
        panel.setBackground(new Color(23,63,31));
        panel.setPreferredSize(new Dimension(750,125));
        try {
            ImageIcon save = new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/saveAbilities.png")));
            ImageIcon saveLight = new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/saveAbilitiesLight.png")));
            JLabel saveButton = new JLabel(save);
            saveButton.setBounds(21,20,707,82);
            saveButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    saveButton.setIcon(saveLight);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    saveButton.setIcon(save);
                }
                @Override
                public void mouseClicked(MouseEvent e) {
                    //TODO save data
                    pane.getParent().remove(LevelsMenu.overlayPane);
                    LevelsMenu.overlayPane = null;
                    App.getLevelsMenu().revalidate();
                    App.getLevelsMenu().repaint();
                }
            });
            panel.add(saveButton);

        } catch (Exception e) {System.out.println(e);}
        return panel;
    } 


    public class AbilityBar extends JLayeredPane{
        ImageIcon light;
        ImageIcon normal;
        ImageIcon dark;
        ImageIcon lock;
        ImageIcon checked;
        JLabel image;
        /*
         * unavailable
         * selectable
         * chosen
         * locked
         */
        public String state;
        JLabel overlay;

       


        public AbilityBar(String title, boolean isPassive){
            String folder;
            setPreferredSize(new Dimension(600,131));
            if(isPassive){
                title = "passive/" + title;
                folder = "passive/";
            }else{
                title = "active/" + title;
                folder = "active/";
            }

            try {
                light = new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/abilities/" + title + "Light.png")));
                normal = new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/abilities/" + title + ".png")));
                dark = new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/abilities/" + title + "Dark.png")));

                lock = new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/abilities/" + folder + "lock.png")).getScaledInstance(104, 104, Image.SCALE_SMOOTH));
                checked = new ImageIcon(ImageIO.read(new File("resources/images/levelInfo/abilities/" + folder + "checkED.png")).getScaledInstance(104, 104, Image.SCALE_SMOOTH));

                overlay = new JLabel();
                overlay.setBounds(26,14,104,104);
                image = new JLabel(normal);
                image.setVisible(true);
                image.setBounds(0,0,600,131);
                add(image);
                add(overlay,0);
            } catch (Exception e) {}

            image.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if(state.equals("selectable")){
                        image.setIcon(light);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if(state.equals("selectable")){
                        image.setIcon(normal);
                    }
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

        public void makeLock(){
            state = "locked";
            overlay.setIcon(lock);
            makeDark();
        }

        public void makeChecked(){
            state = "chosen";
            overlay.setIcon(checked);
            makeLight();
        }
    }


}
