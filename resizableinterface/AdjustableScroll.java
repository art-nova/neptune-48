/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package resizableinterface;

import java.awt.GridLayout;
import javax.swing.JScrollPane;
import java.util.*;
import javax.swing.JPanel;
/**
 *
 * @author temak
 */
public class AdjustableScroll extends JScrollPane{
    public List<Row> rows = new ArrayList<>();
    
    public AdjustableScroll(int x, int y, int width, int height){
        super();
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(rows.size(), 1));
        for (Row row : rows) {
            panel.add(row);
        }
        setViewportView(panel);
    }
}
