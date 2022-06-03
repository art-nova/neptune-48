package UI.levelPieces;

import javax.swing.JLayeredPane;

import UI.fragments.FilledBox;
import UI.miscellaneous.SizeSet;
import java.awt.Dimension;
import javax.swing.JPanel;
import models.App;

/**
 * Board with cells.
 * @author Artemii Kolomiichuk
 */
public class Board extends JPanel{
    //private BoardCover cover;

    public Board(){
        super();
        //cover = new BoardCover();
        //add(cover, 100);
        //TODO
        
        setPreferredSize(new Dimension(App.getLevel().sizes.boardSize, App.getLevel().sizes.boardSize));
        FilledBox back = new FilledBox(App.getLevel().colors.outline);
        back.setBounds(0, 0, App.getLevel().sizes.boardSize, App.getLevel().sizes.boardSize);
        JLayeredPane pane = new JLayeredPane();
        pane.setPreferredSize(new Dimension(App.getLevel().sizes.boardSize, App.getLevel().sizes.boardSize));
        pane.add(back, 100);
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                FilledBox cell = new FilledBox(App.getLevel().colors.filling);
                SizeSet sizes = App.getLevel().sizes;
                cell.setBounds(sizes.boardOffsetBig + i * (sizes.tileSize + sizes.boardOffsetSmall),
                sizes.boardOffsetBig + j * (sizes.tileSize + sizes.boardOffsetSmall),
                sizes.tileSize,
                sizes.tileSize);
                pane.add(cell, 0);
            }
        }
        
        add(pane);
        setBackground(App.getLevel().colors.background);
    }
}
