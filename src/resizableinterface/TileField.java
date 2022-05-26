/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package resizableinterface;

import java.awt.Point;

/**
 *
 * @author temak
 */
//board
public class TileField {
    int rows;
    int collumns;
    public MovingTile[] tiles;
    
    int tileSize;
    int offset = 5;
    Point upperLeft;
    
    public TileField(int rows, int collumns, int tileSize, int offset, int x, int y){
        this.rows = rows;
        this.collumns = collumns;
        this.tileSize = tileSize;
        this.offset = offset;
        upperLeft = new Point(x,y);
        tiles = new MovingTile[rows*collumns];
    }
    
    public void moveToRightRigid(){
        for(int i = 0; i < rows; i++){
            MovingTile[] row = new MovingTile[collumns];
            for(int j = 0; j < collumns; j++){
                row[j] = tiles[i*collumns + j];
            }
            //e.g.
            //i = 2
            //collumns = rows = 6
            //     [0] [1] [2]  [3] [4] [5]
            //row {NULL 13 NULL NULL 16 NULL}
            //after 16th move
            //row {NULL 13 NULL NULL NULL 17}
            for(int l = collumns - 1; l >= 0; l--){
                if(row[l] != null){
                    int moves = 0;
                    int newPos = l;
                    
                    for(int k = l + 1; k < collumns; k++){
                        if(row[k] == null) {
                            moves++;
                            newPos++;
                        }
                        else{
                            newPos = k - 1;
                            break;
                        }                       
                    }
                    
                    if(newPos != l){
                        row[newPos] = row[l];
                        row[l] = null;
                        row[newPos].positionOnField = i*collumns + newPos;
                        row[newPos].targetPoint = placeByPositionOnField(row[newPos].positionOnField);
                        //System.out.println(row[newPos].positionOnField);
                        //System.out.println(placeByPositionOnField(row[newPos].positionOnField));
                        row[newPos].setTimer();
                    }
                }
            }
        }
    }
    
    public void moveToLeftRigid(){
        for(int i = 0; i < rows; i++){
            MovingTile[] row = new MovingTile[collumns];
            for(int j = 0; j < collumns; j++){
                row[j] = tiles[i*collumns + j];
            }
            //e.g.
            //i = 2
            //collumns = rows = 6
            //     [0] [1] [2]  [3] [4] [5]
            //row {NULL 13 NULL NULL 16 NULL}
            //after 13th move
            //row {12 NULL NULL NULL 16 NULL}
            for(int l = 0; l < collumns; l++){
                if(row[l] != null){
                    int newPos = l;
                    
                    for(int k = l - 1; k >= 0; k--){
                        if(row[k] == null) {
                            newPos--;
                        }
                        else{
                            newPos = k + 1;
                            break;
                        }                       
                    }
                    //System.out.println("old " + row[l].positionOnField);
                    if(newPos != l){
                        var temp = new MovingTile(tiles[row[l].positionOnField]);
                        tiles[row[l].positionOnField] = null;
                        
                        temp = row[l];
                        row[l] = null;
                        temp.positionOnField = i*collumns + newPos;
                        temp.targetPoint = placeByPositionOnField(temp.positionOnField);
                        tiles[temp.positionOnField] = temp;
                        //System.out.println("new " +row[newPos].positionOnField);
                        //System.out.println(placeByPositionOnField(row[newPos].positionOnField));
                        temp.setTimer();
                    }
                }
            }
        }
    }
       
    Point placeByPositionOnField(int pos){
        return new Point(
        upperLeft.x + (pos%rows * (offset + tileSize)),
        upperLeft.y + (pos/collumns * (offset + tileSize))
        );          
    }

    @Override
    public String toString() {
        String res = "";
        for(int i = 0; i < rows; i ++){
            for(int j = 0; j < collumns; j++){
                try {
                    res = res + " " + tiles[i * rows + j].positionOnField;
                } catch (Exception e) {
                    res = res + " NULL";
                }
            }
            res = res + "\n";
        }
        return res;
    }
    
    
}
