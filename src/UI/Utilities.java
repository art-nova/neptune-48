/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UI;

import java.awt.Color;
import java.util.Arrays;

/**
 *
 * @author temak
 */
public class Utilities {
   
    static Color previousColor = Color.BLACK;
    
    static Color randomColor(){
        Color newColor = new Color(
                (int)Math.round(Math.random() * 255),
                (int)Math.round(Math.random() * 255),
                (int)Math.round(Math.random() * 255),
                (int)Math.round(Math.random() * 255));
        if (colorDistance(previousColor, newColor) < 250 || colorSaturation(newColor) < 150){
            previousColor = randomColor();
            return previousColor;
        }
        previousColor = newColor;
        return newColor;
    }
    
    static double colorSaturation(Color color){
        int[] values = new int[] {color.getRed(), color.getGreen(), color.getBlue()};
        Arrays.sort(values);
        return Math.sqrt(Math.pow(values[0] - values[2], 2) + Math.pow(values[1] - values[2], 2));
    }
    
    static double colorDistance(Color first, Color second){
        int deltaRed = first.getRed() - second.getRed();
        int deltaGreen = first.getGreen() - second.getGreen();
        int deltaBlue = first.getBlue() - second.getBlue();
        return Math.sqrt(Math.pow(deltaRed, 2) + Math.pow(deltaGreen, 2) + Math.pow(deltaBlue, 2));
    }
    
    public static Color withZeroAlpha(Color input){
        return new Color(input.getRed(), input.getBlue(), input.getGreen(), 0);
    }
}
