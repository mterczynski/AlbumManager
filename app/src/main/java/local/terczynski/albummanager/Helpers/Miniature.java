package local.terczynski.albummanager.Helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

public class Miniature extends android.support.v7.widget.AppCompatImageView implements View.OnLongClickListener {

    public static Point size = new Point(120,120);

    private Bitmap bitmap;

    public Miniature(Context context, Bitmap bitmap, Point miniaturePosition) {
        super(context);
        this.setX(miniaturePosition.x);
        this.setY(miniaturePosition.y);
        this.bitmap = Bitmap.createScaledBitmap(bitmap , size.x, size.y, false);

        setOnLongClickListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3); // border thickness
        paint.setColor(Color.argb(150, 255, 255, 255));

        Rect rect = new Rect((int)this.getX(), (int)this.getY(), (int)this.getX() + size.x, (int)this.getY() + size.y);

        Log.d("miniature", "x: " + this.getX() + ", y: " + this.getY());

        canvas.drawRect(rect, paint);
        canvas.drawBitmap(bitmap, this.getX(), this.getY(), new Paint());
    }

    @Override
    public boolean onLongClick(View view) {

        Log.d("debug miniature", "longclick");

        AlertDialog.Builder alert = new AlertDialog.Builder(super.getContext());
        String[] options = {"podgląd zdjęcia", "usuń bieżące", "zapisz bieżące"};
        alert.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alert.show();
        return false;
    }
}
