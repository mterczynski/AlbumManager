package local.terczynski.albummanager.Activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import local.terczynski.albummanager.Adapters.MyArrayAdapter;
import local.terczynski.albummanager.Helpers.Note;
import local.terczynski.albummanager.R;

public class NotatkiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notatki);

        ArrayList<Note> mockNotes = new ArrayList<Note>();
        mockNotes.add(new Note("imagePath1", "title1", "text1", "#00ff00", 1));
        mockNotes.add(new Note("imagePath2", "title2", "text2 taki inny", "#ff0000", 2));

        MyArrayAdapter adapter = new MyArrayAdapter(
            NotatkiActivity.this,
            R.layout.note_row_layout,
            mockNotes
        );
        ListView notatki_listView = findViewById(R.id.notatki_listView);
        notatki_listView.setAdapter(adapter);
    }
}
