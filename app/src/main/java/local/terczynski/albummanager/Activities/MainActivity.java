package local.terczynski.albummanager.Activities;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.io.File;

import local.terczynski.albummanager.R;

public class MainActivity extends AppCompatActivity {

    private void createAppFolderIfDoesntExist(){
        String mainFolderName = getString(R.string.mainFolderName);

        File SYS_pictures = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES );
        File mainDir = new File(SYS_pictures, mainFolderName);

        Log.d("fileInit", "Foldery w folderze PICTURES: ");

        if(SYS_pictures.exists()){
            File[] filesInPictureFolder = SYS_pictures.listFiles();
            if(filesInPictureFolder != null) {
                for (File file : filesInPictureFolder) {
                    Log.d("fileInit", file.getName());
                }
            }
        } else {
            throw new Error("no picture folder in Android!");
        }

        File[] subdirs = {
            new File(mainDir, "miejsca"),
            new File(mainDir, "ludzie"),
            new File(mainDir, "rzeczy")
        };

        if(!mainDir.mkdir()){
            Log.d("fileInit", "Nie udało się utworzyć głównego folderu, możliwe że folder już istnieje.");
        }
        for(File subdir : subdirs){
            if(!subdir.mkdirs()){
                Log.d("fileInit", "Nie udało się utworzyć folderu \"" + subdir.getName() + "\", możliwe że folder już istnieje.");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout layout_zdjecie = (LinearLayout)findViewById(R.id.layout_zdjecie);
        LinearLayout layout_albumy = (LinearLayout)findViewById(R.id.layout_albumy);
        LinearLayout layout_kolaz = (LinearLayout)findViewById(R.id.layout_kolaz);
        LinearLayout layout_zobacz_w_sieci = (LinearLayout)findViewById(R.id.layout_zobacz_w_sieci);
        LinearLayout layout_notatki = findViewById(R.id.layout_notatki);

        // utworzenie folderów przy starcie aplikacji:
        createAppFolderIfDoesntExist();

        // click -> zmiana aktywności:
        layout_zdjecie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PictureActivity.class));
            }
        });
        layout_albumy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AlbumsActivity.class));
            }
        });
        layout_kolaz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CollageActivity.class));
            }
        });
        layout_zobacz_w_sieci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SeeOnlineActivity.class));
            }
        });
        layout_notatki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NotatkiActivity.class));
            }
        });
    }
}
