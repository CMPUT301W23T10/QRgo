package com.example.qrgo.utilities;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.example.qrgo.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
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
     */
    public static void setImage(String name, ImageView imageView) {

        String firstChar = name.substring(0, 1);


        if (Character.isLetter(firstChar.charAt(0))) {

            int charVal = Character.toUpperCase(firstChar.charAt(0)) - 'A' + 1;

            try {
                // Load image from assets folder
                String imageName = String.format("%02d.png", charVal);
                InputStream ims = imageView.getContext().getAssets().open(imageName);
                Bitmap bitmap = BitmapFactory.decodeStream(ims);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

        }
    }
}
