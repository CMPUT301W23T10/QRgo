package com.example.qrgo.utilities;

        import android.graphics.Bitmap;
        import android.graphics.BitmapShader;
        import android.graphics.Canvas;
        import android.graphics.Paint;
        import android.graphics.RectF;

        import com.squareup.picasso.Transformation;

/**
 A transformation that rounds a square image to have rounded corners.
 */
public class RoundedSquareTransform implements Transformation {

    private final int radius;

    /**
     Constructs a new RoundedSquareTransform with the given radius for the corners.
     @param radius the radius of the corners in pixels
     */
    public RoundedSquareTransform(int radius) {
        this.radius = radius;
    }

    /**

     Transforms the given Bitmap to have rounded corners with the radius specified in the constructor.

     @param source the Bitmap to transform

     @return the transformed Bitmap with rounded corners
     */
    @Override
    public Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        if (squaredBitmap != source) {
            source.recycle();
        }

        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap,
                BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        RectF rectF = new RectF(0, 0, size, size);
        canvas.drawRoundRect(rectF, radius, radius, paint);

        squaredBitmap.recycle();
        return bitmap;
    }

    /**
     Returns a unique key for the transformation.
     @return a String key for the transformation
     */
    @Override
    public String key() {
        return "rounded-square";
    }
}
