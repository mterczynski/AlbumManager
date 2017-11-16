package local.terczynski.albummanager.Helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class Miniature extends android.support.v7.widget.AppCompatImageView {

    private final Point minatureSize = new Point(50,50);
    public Point position = new Point();

    public Miniature(Context context, Bitmap bitmap, Point imageSize) {
        super(context);
    }
    public Miniature(Context context, Bitmap bitmap, Point imageSize, Point miniaturePosition) {
        super(context);
        this.position = miniaturePosition;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3); // border thickness
        paint.setColor(Color.argb(150, 255, 255, 255));

        Rect rect = new Rect(position.x, position.y, position.x + minatureSize.x, position.y + minatureSize.y);

        canvas.drawRect(rect, paint);
    }
}
