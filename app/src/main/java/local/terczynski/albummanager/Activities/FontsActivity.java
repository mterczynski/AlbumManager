package local.terczynski.albummanager.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import local.terczynski.albummanager.R;

public class FontsActivity extends AppCompatActivity {

    private Intent resultIntent;
    private CharSequence lastTypedText;
    private String[] listOfFonts;
    private Typeface activeTypeface;

    // views:
    private EditText editText;
    private RelativeLayout outputLayout;
    private TextView outputText;
    private ImageView acceptButton;
    private AssetManager assetManager;
    private LinearLayout textViewList;


    private void renderOutputText(CharSequence text){
        TextView outputTextView = new TextView(FontsActivity.this);
        outputTextView.setTextSize(40f);
        outputTextView.setTextColor(Color.RED);
        outputTextView.setText(text);
        outputTextView.setTypeface(activeTypeface);
        outputLayout.removeAllViews();
        outputLayout.addView(outputTextView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fonts_layout);

        // get views:
        outputLayout = (RelativeLayout) findViewById(R.id.font_previewLayout);
        acceptButton = findViewById(R.id.fonts_accept);
        outputText = findViewById(R.id.font_textOutput);
        editText = (EditText )findViewById(R.id.fonts_editText);
        textViewList = (LinearLayout) findViewById(R.id.font_scrollView_linear);

        TextWatcher textWatcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                lastTypedText = charSequence;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                renderOutputText(lastTypedText);
            }
        };

        editText.addTextChangedListener(textWatcher);

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
                chooseFont.setTextSize(40f);
                chooseFont.setTag(typeface);

                chooseFont.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        activeTypeface = (Typeface) view.getTag();
                        renderOutputText(lastTypedText);
                    }
                });

                textViewList.addView(chooseFont);
            }
        } catch (Exception ex){
            Toast.makeText(FontsActivity.this, "no fonts found", Toast.LENGTH_SHORT).show();
        }

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
