package local.terczynski.albummanager.Helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

public class Miniature extends android.support.v7.widget.AppCompatImageView {

    private final Point minatureSize = new Point(50,80);
    public Miniature(Context context, Bitmap bitmap, Point miniaturePosition) {
        super(context);
        this.setX(miniaturePosition.x - 50/4);
        this.setY(miniaturePosition.y - 60/4);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3); // border thickness
        paint.setColor(Color.argb(150, 255, 255, 255));

        Rect rect = new Rect((int)this.getX(), (int)this.getY(), (int)this.getX() + minatureSize.x, (int)this.getY() + minatureSize.y);

        Log.d("miniature", "x: " + this.getX() + ", y: " + this.getY());

        canvas.drawRect(rect, paint);
    }
}
