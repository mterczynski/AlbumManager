package local.terczynski.albummanager.Helpers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.Display;
import android.view.View;

public class Circle extends View {

    private final Point screenSize;
    private final int radius = 50;
    private final int smallerScreenSideLength;

    public Circle(Context context, Point screenSize) {
        super(context);
        this.screenSize = screenSize;
        this.smallerScreenSideLength = screenSize.x < screenSize.y ? screenSize.x : screenSize.y;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setColor(Color.argb(155, 255, 255, 255)); // lub paint.setColor(Color.RED);
        canvas.drawCircle(screenSize.x/2, screenSize.y/2, smallerScreenSideLength/4, paint);
    }
}
