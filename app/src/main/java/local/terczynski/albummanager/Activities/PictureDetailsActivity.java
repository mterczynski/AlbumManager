package local.terczynski.albummanager.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import local.terczynski.albummanager.Adapters.DrawerArrayAdapter;
import local.terczynski.albummanager.Adapters.MyArrayAdapter;
import local.terczynski.albummanager.R;

public class PictureDetailsActivity extends AppCompatActivity {

    private Bitmap betterImageDecode(String filePath) {

        Bitmap myBitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();    //opcje przekształcania bitmapy
        options.inSampleSize = 4; // zmniejszenie jakości bitmapy 4x
        myBitmap = BitmapFactory.decodeFile(filePath, options);
        return myBitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_details);

        final String imageFullPath = getIntent().getStringExtra("pictureFullPath");
        try{
            Bundle extras = getIntent().getExtras();
            File currentDir = (File)extras.get("currentDir");
            Log.d("bundleFix", currentDir.getName());
        } catch (Exception ex){
            Log.d("bundleFix", "no currentDir provided");
        }

        // drawer:
        List tempObjects = new ArrayList<String>();
        tempObjects.add("Fonts");
        tempObjects.add("Item 2");
        tempObjects.add("Item 3");
        tempObjects.add("Item 4");
        tempObjects.add("Item 5");
        DrawerArrayAdapter adapter = new DrawerArrayAdapter(
                PictureDetailsActivity.this,
                R.layout.drawer_item_row_layout,
                tempObjects
        );
        ListView drawerItems = findViewById(R.id.picture_drawer_items);
        drawerItems.setAdapter(adapter);
        // end of drawer

        ImageView deleteImageButton = (ImageView)findViewById(R.id.deleteImage);
        ImageView obrazekDetails = (ImageView)findViewById(R.id.obrazekDetails);

        Bitmap bmp1 = betterImageDecode(imageFullPath);
        obrazekDetails.setImageBitmap(bmp1);

        deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(PictureDetailsActivity.this);
                alert.setTitle("Usuwanie");
                alert.setMessage("Czy usunąć zdjęcie?");
                alert.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    File imageFile = new File(imageFullPath);
                    imageFile.delete();
//                        backToAlbumDetails.putExtra("currentDir", (File)getIntent().getExtras().get("currentDir"));
//
//                        startActivity(backToAlbumDetails);
                    Log.d("bundleFix", "after starting activity");
                    //PictureDetailsActivity.this.finish();
                    finish();
                    }
                });
                alert.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alert.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                String user_font = data.getStringExtra("user_font");
                String user_text =  data.getStringExtra("user_text");

                Log.d("result", "user_font: " + user_font);
                Log.d("result", "user_text: " + user_text);
            }
        }
    }
}
