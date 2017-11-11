package local.terczynski.albummanager.Helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import local.terczynski.albummanager.R;


public class CustomImageView extends android.support.v7.widget.AppCompatImageView implements View.OnClickListener, View.OnLongClickListener {

    private String selectedColor;
    private String imagePath;

    private TextView colorToPick1;
    private TextView colorToPick2;
    private TextView colorToPick3;
    private TextView colorToPick4;

    private EditText noteTitleET;
    private EditText noteTextET;

    private DatabaseManager db;

    public CustomImageView(Context context, int imageId, DatabaseManager db, String imagePath) {
        super(context);

        this.db = db;
        this.imagePath = imagePath;

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(200,200);
        lp.setMargins(10,0,10,10);
        this.setLayoutParams( lp);
        this.setBackgroundColor(0xff0000ff);
        this.setScaleType(ScaleType.CENTER_CROP);
        this.setImageResource(imageId);

        this.setLongClickable(true);
        this.setClickable(true);

        setOnClickListener(this);
        setOnLongClickListener(this);
    }
    @Override
    public void onClick(View v) {}

    @Override
    public boolean onLongClick(View view) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
        final View customView = View.inflate(view.getContext(), R.layout.note_layout, null);

        noteTitleET = (EditText) customView.findViewById(R.id.noteTitle);
        noteTextET = (EditText) customView.findViewById(R.id.noteText);

        alert.setView(customView);
        alert.setTitle("Add new note");

        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            String title = noteTitleET.getText().toString();
            String text = noteTextET.getText().toString();

            Log.d("imagePath","imagePath is: " + imagePath);

            db.insert(title, text, selectedColor, imagePath);
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alert.show();

        colorToPick1 = (TextView)customView.findViewById(R.id.colorToPick1);
        colorToPick2 = (TextView)customView.findViewById(R.id.colorToPick2);
        colorToPick3 = (TextView)customView.findViewById(R.id.colorToPick3);
        colorToPick4 = (TextView)customView.findViewById(R.id.colorToPick4);

        if(colorToPick1 == null){
            Log.d("colorPickerDebug", "colorToPick1 = null");
        }

        colorToPick1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                colorToPick1.setText("V");
                colorToPick2.setText("");
                colorToPick3.setText("");
                colorToPick4.setText("");

                int color = Color.TRANSPARENT;
                Drawable background = colorToPick1.getBackground();
                if (background instanceof ColorDrawable){
                    color = ((ColorDrawable) background).getColor();
                }

                selectedColor = String.format("#%06X", (0xFFFFFF & color));
            }
        });
        colorToPick2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                colorToPick1.setText("");
                colorToPick2.setText("V");
                colorToPick3.setText("");
                colorToPick4.setText("");

                int color = Color.TRANSPARENT;
                Drawable background = colorToPick2.getBackground();
                if (background instanceof ColorDrawable){
                    color = ((ColorDrawable) background).getColor();
                }

                selectedColor = String.format("#%06X", (0xFFFFFF & color));
            }
        });
        colorToPick3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                colorToPick1.setText("");
                colorToPick2.setText("");
                colorToPick3.setText("V");
                colorToPick4.setText("");

                int color = Color.TRANSPARENT;
                Drawable background = colorToPick3.getBackground();
                if (background instanceof ColorDrawable){
                    color = ((ColorDrawable) background).getColor();
                }

                selectedColor = String.format("#%06X", (0xFFFFFF & color));
            }
        });
        colorToPick4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                colorToPick1.setText("");
                colorToPick2.setText("");
                colorToPick3.setText("");
                colorToPick4.setText("V");

                int color = Color.TRANSPARENT;
                Drawable background = colorToPick4.getBackground();
                if (background instanceof ColorDrawable){
                    color = ((ColorDrawable) background).getColor();
                }

                selectedColor = String.format("#%06X", (0xFFFFFF & color));
            }
        });

        System.out.println("HALOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
        Log.d("longdebug", "long click detected");

        return false;
    }
}