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
import android.util.Log;

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
        Bitmap result = Bitmap.createBitmap(275, 275, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint p = new Paint();
        ColorFilter filter = new LightingColorFilter(featureList.get(4), 0);
        p.setColorFilter(filter);
        for (int i = 0; i < 4; i++) {

            String featureName = "feature_" + (i*5 + featureList.get(i) + 1);
            int featureID = context.getResources().getIdentifier(featureName, "drawable", "com.example.qrgo");
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), featureID);
            if (i < 2) {
                canvas.drawBitmap(bitmap, 0, 0, p);
            } else {
                canvas.drawBitmap(bitmap, 0, 0, null);
            }

        }

// Return the resulting QR code bitmap.
        return result;
    }
}