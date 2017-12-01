package local.terczynski.albummanager.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import java.io.File;

import local.terczynski.albummanager.R;

public class AlbumsActivity extends AppCompatActivity {

    String mainFolderName;
    private File SYS_pictures;
    private File mainFolder;

    protected void refreshFileList() {

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.album_name,     // nazwa pliku xml do layoutu wiersza
                R.id.album_name_textView,  // id pola tekstowego w wierszu
                mainFolder.list()
        );
        final ListView listView = (ListView) findViewById(R.id.albumListView);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LinearLayout kliknietyLayout = (LinearLayout)view;
                TextView textViewZNazwa = (TextView)kliknietyLayout.getChildAt(1);
                Intent intent = new Intent(AlbumsActivity.this, AlbumDetailsActivity.class);
                intent.putExtra("currentDir", new File(mainFolder,textViewZNazwa.getText() + ""));
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);

        mainFolderName = getString(R.string.mainFolderName);
        SYS_pictures =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        mainFolder = new File(SYS_pictures, mainFolderName);

        refreshFileList();

        ImageView plus_icon = (ImageView)findViewById(R.id.plus_icon);

        plus_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(AlbumsActivity.this);
                alert.setTitle("Nowy album");
                alert.setMessage("Podaj nazwę albumu");

                final EditText input = new EditText(AlbumsActivity.this);
                alert.setView(input);
                alert.setNeutralButton("Dodaj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    String folderName = input.getText().toString();
                    File newFolder = new File(mainFolder, folderName);
                    if(!newFolder.mkdirs()){
                        AlertDialog.Builder fileExistAlert = new AlertDialog.Builder(AlbumsActivity.this);
                        fileExistAlert.setTitle("Uwaga");
                        fileExistAlert.setMessage("Nie można utworzyć folderu. Być może folder z taką nazwą już istnieje");
                        fileExistAlert.show();
                    } else {
                        refreshFileList();
                    }
                    }
                });
                alert.setNegativeButton("Anuluj", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });

                alert.show();
            }
        });


//        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                //test
//                Log.d("TAG","index = " + i);
//            }
//        });
    }
}
