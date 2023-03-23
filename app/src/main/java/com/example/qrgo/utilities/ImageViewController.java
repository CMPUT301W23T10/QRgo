package com.example.qrgo.utilities;
import android.widget.ImageView;
import java.util.Random;
/**
 * The ImageViewController class provides a utility method to set an ImageView's image based on the first
 * character of a given name. If the first character is a letter between A and Z, the corresponding image in the
 * drawables folder will be used. If the first character is a non-alphanumeric character or a number,
 * a random image will be chosen from the drawables folder.
 */
public class ImageViewController {
    /**
     * Sets the image of an ImageView based on the first character of the given name.
     *
     * @param name        The name to base the image on.
     * @param imageView   The ImageView to set the image on.
     * @param roundSquare Boolean indicating whether to set the image as a round or square image. (true = round, false = square)
     */
    public static void setImage(String name, ImageView imageView, Boolean roundSquare) {

        String firstChar = name.substring(0, 1);

        int imageResource;

        if (Character.isLetter(firstChar.charAt(0))) {

            int charVal = Character.toUpperCase(firstChar.charAt(0)) - 'A' + 1;

            imageResource = imageView.getResources()
                    .getIdentifier(String.format("%02d", charVal),
                            "drawable", imageView.getContext().getPackageName());

        } else {

            Random rand = new Random();
            int randomNum = rand.nextInt(26) + 1;

            imageResource = imageView.getResources()
                    .getIdentifier(String.format("%02d", randomNum),
                            "drawable", imageView.getContext().getPackageName());
        }

        imageView.setImageResource(imageResource);
    }
}
