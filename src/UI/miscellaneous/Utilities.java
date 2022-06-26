package UI.miscellaneous;

import java.awt.Color;
import java.util.Arrays;

/**
 * Miscellaneous utilities.
 * @author Artemii Kolomiichuk
 */
public class Utilities {
   
    static Color previousColor = Color.BLACK;
    
    /**
     * Returns a random color.
     * @return a random color.
     */
    public static Color randomColor(){
        Color newColor = new Color(
                (int)Math.round(Math.random() * 255),
                (int)Math.round(Math.random() * 255),
                (int)Math.round(Math.random() * 255)
                //,(int)Math.round(Math.random() * 255)
                );
        if (colorDistance(previousColor, newColor) < 250 || colorSaturation(newColor) < 150){
            previousColor = randomColor();
            return previousColor;
        }
        previousColor = newColor;
        return newColor;
    }

    /**
     * Returns color saturation.
     * @param color
     * @return saturation. [0;255]
     */
    public static double colorSaturation(Color color){
        int[] values = new int[] {color.getRed(), color.getGreen(), color.getBlue()};
        Arrays.sort(values);
        return Math.sqrt(Math.pow(values[0] - values[2], 2) + Math.pow(values[1] - values[2], 2));
    }

    /**
     * Returns color distance.
     * @param color1
     * @param color2
     * @return distance. [0;255]
     */
    public static double colorDistance(Color first, Color second){
        int deltaRed = first.getRed() - second.getRed();
        int deltaGreen = first.getGreen() - second.getGreen();
        int deltaBlue = first.getBlue() - second.getBlue();
        return Math.sqrt(Math.pow(deltaRed, 2) + Math.pow(deltaGreen, 2) + Math.pow(deltaBlue, 2));
    }

    /**
     * Returns same color with zero alpha.
     */
    public static Color withZeroAlpha(Color input){
        return new Color(input.getRed(), input.getBlue(), input.getGreen(), 0);
    }

    /**
     * Returns color between two colors by percentage.
     */
    public static Color colorBetween(Color first, Color second, double percentage){
	    percentage /= 100;
        int red = (int)Math.round(first.getRed() + (second.getRed() - first.getRed()) * percentage);
        int green = (int)Math.round(first.getGreen() + (second.getGreen() - first.getGreen()) * percentage);
        int blue = (int)Math.round(first.getBlue() + (second.getBlue() - first.getBlue()) * percentage);
        return new Color(red, green, blue);
    }
}
