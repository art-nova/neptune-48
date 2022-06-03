package UI;

import java.awt.*;
import java.awt.geom.*;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
public class RotateJLabelTest extends JFrame {
    
   public RotateJLabelTest() {
      setTitle("Rotate JLabel");
      try {
         RotateLabel label = new RotateLabel(new ImageIcon(ImageIO.read(new File("C:/Users/temak/Desktop/2048/images/level.png")).getScaledInstance(500, 500, 1)));
         label.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
               label.scaleAndRotate();
            }
         });
         setLayout(new BorderLayout());
         add(label);
         setSize(900, 800);
         setDefaultCloseOperation(EXIT_ON_CLOSE);
         //setLocationRelativeTo(null);
         setVisible(true);
      } catch (Exception e){}     
   }

   private class RotateLabel extends JLabel implements ActionListener {
      double x, y;
      int width, height;
      private Timer timer;
      private int angle = 0;
      private double scaleFactor = 0.1;
      public RotateLabel(ImageIcon image) {
         super(image);
         setBounds(0, 0, 50,50);
      }
      @Override
      public void paintComponent(Graphics g) {
         Graphics2D gx = (Graphics2D) g;
         //gx.scale(scaleFactor, scaleFactor);
         gx.rotate(Math.toRadians(angle), getX() + getWidth()/2, getY() + getHeight()/2);
         
         super.paintComponent(g);
      }
      public void scaleAndRotate(){
         timer = new Timer(13, this);
         timer.start();
      }
      public void actionPerformed(ActionEvent e) {
         
         scaleFactor += 0.03;
         //angle +=2;
         x -= 7;
         y -= 1.8;

         setLocation((int)x,(int) y);
         //repaint();
         System.out.println("x " + getX() + "\n" + "y " + getY() + "\n" + "width " + getWidth() + "\n" + "height " + getHeight());
     }
   }
   public static void main(String[] args) {
      new RotateJLabelTest();
   }
}
