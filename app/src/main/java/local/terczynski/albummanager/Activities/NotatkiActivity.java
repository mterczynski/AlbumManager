package local.terczynski.albummanager.Activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import local.terczynski.albummanager.Adapters.MyArrayAdapter;
import local.terczynski.albummanager.Helpers.DatabaseManager;
import local.terczynski.albummanager.Helpers.Note;
import local.terczynski.albummanager.R;

public class NotatkiActivity extends AppCompatActivity {

    DatabaseManager dbManager;
    ArrayList<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notatki);

        dbManager = new DatabaseManager(
            NotatkiActivity.this, // TODO check activity z galerią zdjęć
            getString(R.string.dbName), // database name
            null,
            3 // database version
        );

        notes = dbManager.getAllNotes();

        MyArrayAdapter adapter = new MyArrayAdapter(
            NotatkiActivity.this,
            R.layout.note_row_layout,
            notes
        );
        ListView notatki_listView = findViewById(R.id.notatki_listView);
        notatki_listView.setAdapter(adapter);

        notatki_listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int index, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NotatkiActivity.this);
                String [] items = {
                    "edytuj",
                    "usuń",
                    "sortuj wg tytułu",
                    "sortuj wg koloru",
                    "sortuj wg nazwy zdjęcia"
                };
                builder.setTitle("Uwaga!").setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){ // edit

                        } else if(which == 1){ // delete
                            dbManager.deleteNoteById(notes.get(index).id+"");
                            finish();
                            startActivity(getIntent()); // refresh activity
                        } else if(which == 2){ // sort by title

                        } else if(which == 3){ // sort by color

                        } else if(which == 4){ // sort by picture name

                        }
                    }
                });
                builder.show();

                return true;
            }
        });
    }
}
