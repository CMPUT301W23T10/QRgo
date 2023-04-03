package com.example.qrgo.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;

import com.example.qrgo.R;

import java.util.ArrayList;

/**

 The QRCodeVisualRenderer class provides a static method to render a QR code by combining a set of visual features.
 */
public class QRCodeVisualRenderer {

    /**

     Renders a QR code by combining a set of visual features.

     @param context The context used to access resources.

     @param featureList The list of feature IDs used to build the QR code.

     @return The generated QR code bitmap.
     */
    public static Bitmap renderQRCodeVisual(Context context, ArrayList<Integer> featureList) {

// Create a new bitmap to hold the resulting QR code.
        Bitmap result = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);

// Create a new canvas to draw the visual features onto the bitmap.
        Canvas canvas = new Canvas(result);

// Loop through the list of feature IDs, and draw each feature onto the canvas.
        for (int i = 0; i < featureList.size(); i++) {
            String featureName = "feature_" + (i*5 + featureList.get(i) + 1);
            int featureID = context.getResources().getIdentifier(featureName, "drawable", "com.example.qrgo");
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), featureID);
            canvas.drawBitmap(bitmap, 0, 0, null);
        }

// Return the resulting QR code bitmap.
        return result;
    }
}