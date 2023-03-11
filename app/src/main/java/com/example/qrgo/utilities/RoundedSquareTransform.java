package com.example.qrgo.utilities;

        import android.graphics.Bitmap;
        import android.graphics.BitmapShader;
        import android.graphics.Canvas;
        import android.graphics.Paint;
        import android.graphics.RectF;

        import com.squareup.picasso.Transformation;

public class RoundedSquareTransform implements Transformation {

    private final int radius;

    public RoundedSquareTransform(int radius) {
        this.radius = radius;
    }

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

    @Override
    public String key() {
        return "rounded-square";
    }
}

