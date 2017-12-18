package local.terczynski.albummanager.Activities;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import local.terczynski.albummanager.R;

public class FontsActivity extends AppCompatActivity {

    private Intent resultIntent;

    // views:
    private EditText editText;
    private TextView outputText;
    private ImageView acceptButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fonts_layout);

        // get views:
        acceptButton = findViewById(R.id.fonts_accept);
        outputText = findViewById(R.id.font_textOutput);
        editText = (EditText )findViewById(R.id.fonts_editText);

        Log.d("fonts", "init activity");

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultIntent = new Intent();
                resultIntent.putExtra("user_font", "comic sans");
                resultIntent.putExtra("user_text", "testowy tekst");
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
