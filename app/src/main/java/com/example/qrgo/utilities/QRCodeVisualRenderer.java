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

public class QRCodeVisualRenderer {

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
        return result;
    }
}
