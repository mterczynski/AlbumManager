package local.terczynski.albummanager.Activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import local.terczynski.albummanager.Adapters.MyArrayAdapter;
import local.terczynski.albummanager.Helpers.DatabaseManager;
import local.terczynski.albummanager.Helpers.Note;
import local.terczynski.albummanager.R;

public class NotatkiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notatki);

        DatabaseManager db = new DatabaseManager(
            NotatkiActivity.this, // activity z galerią zdjęć
            getString(R.string.dbName), // database name
            null,
            3//wersja bazy, po zmianie schematu bazy należy ją zwiększyć
        );



//        ArrayList<Note> mockNotes = new ArrayList<Note>();
//        mockNotes.add(new Note("title1", "text1", "#00ff00", 1));
//        mockNotes.add(new Note("title2", "text2 taki inny", "#ff0000", 2));

        ArrayList<Note> notes = db.getAllNotes();

        MyArrayAdapter adapter = new MyArrayAdapter(
            NotatkiActivity.this,
            R.layout.note_row_layout,
            notes
        );
        ListView notatki_listView = findViewById(R.id.notatki_listView);
        notatki_listView.setAdapter(adapter);
    }
}
