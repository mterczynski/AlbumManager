package local.terczynski.albummanager.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import local.terczynski.albummanager.R;

public class FontsActivity extends AppCompatActivity {

    private Intent resultIntent;

    // views:
    private EditText editText;
    private TextView outputText;
    private ImageView acceptButton;
    private AssetManager assetManager;
    private LinearLayout textViewList;
    private String[] listOfFonts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fonts_layout);

        // get views:
        acceptButton = findViewById(R.id.fonts_accept);
        outputText = findViewById(R.id.font_textOutput);
        editText = (EditText )findViewById(R.id.fonts_editText);
        textViewList = (LinearLayout) findViewById(R.id.font_scrollView_linear);

        // get assetManager:
        assetManager = getAssets();
        try{
            listOfFonts = assetManager.list("fonts");
            Toast.makeText(FontsActivity.this, "fonts loaded", Toast.LENGTH_SHORT).show();

            // show fonts:
            for(int i=0; i<listOfFonts.length; i++){
                TextView chooseFont = new TextView(FontsActivity.this);
                Typeface typeface = Typeface.createFromAsset(assetManager, "fonts/" + listOfFonts[i]);
                chooseFont.setTypeface(typeface);
                chooseFont.setText("Zażółć gęślą jaźń");
                chooseFont.setTextSize(32f);
                textViewList.addView(chooseFont);
            }
        } catch (Exception ex){
            Toast.makeText(FontsActivity.this, "no fonts found", Toast.LENGTH_SHORT).show();
        }

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
