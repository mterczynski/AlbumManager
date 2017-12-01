package local.terczynski.albummanager.Activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import local.terczynski.albummanager.Adapters.MyArrayAdapter;
import local.terczynski.albummanager.Helpers.DatabaseManager;
import local.terczynski.albummanager.Helpers.Note;
import local.terczynski.albummanager.R;

public class NotatkiActivity extends AppCompatActivity {

    DatabaseManager dbManager;
    ArrayList<Note> notes;

    String edit_selectedColor;

    private void updateAdapter(){

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
                            final String imagePath;

                            final TextView colorToPick1;
                            final TextView colorToPick2;
                            final TextView colorToPick3;
                            final TextView colorToPick4;
                            final EditText noteTitleET;
                            final EditText noteTextET;

                            final android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(NotatkiActivity.this);
                            final View customView = View.inflate(NotatkiActivity.this, R.layout.note_layout, null);

                            noteTitleET = (EditText) customView.findViewById(R.id.noteTitle);
                            noteTextET = (EditText) customView.findViewById(R.id.noteText);

                            alert.setView(customView);
                            alert.setTitle("Edit note");

                            alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String title = noteTitleET.getText().toString();
                                    String text = noteTextET.getText().toString();

                                    //Log.d("imagePath","imagePath is: " + imagePath);
                                    Log.d("note_edit","note id: " + notes.get(index).id);
                                    Log.d("note_edit","note title: " + title);
                                    Log.d("note_edit","note text: " + text);
                                    Log.d("note_edit","note edit_selectedColor: " + edit_selectedColor);

                                    dbManager.updateNote(notes.get(index).id+"", title, edit_selectedColor, text);
                                    notes = dbManager.getAllNotes();
                                    updateAdapter();
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

                            colorToPick1.setOnClickListener(new View.OnClickListener() {
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

                                    edit_selectedColor = String.format("#%06X", (0xFFFFFF & color));
                                }
                            });
                            colorToPick2.setOnClickListener(new View.OnClickListener() {
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

                                    edit_selectedColor = String.format("#%06X", (0xFFFFFF & color));
                                }
                            });
                            colorToPick3.setOnClickListener(new View.OnClickListener() {
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

                                    edit_selectedColor = String.format("#%06X", (0xFFFFFF & color));
                                }
                            });
                            colorToPick4.setOnClickListener(new View.OnClickListener() {
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

                                    edit_selectedColor = String.format("#%06X", (0xFFFFFF & color));
                                }
                            });
                        } else if(which == 1){ // delete
                            dbManager.deleteNoteById(notes.get(index).id+"");
                            finish();
                            startActivity(getIntent()); // refresh activity
                        } else if(which == 2){ // sort by title
                            notes = dbManager.getNotesSortedByTitle();
                            updateAdapter();
                        } else if(which == 3){ // sort by color
                            notes = dbManager.getNotesSortedByColor();
                            updateAdapter();
                        } else if(which == 4){ // sort by picture name
                            notes = dbManager.getNotesSortedByImagePath();
                            updateAdapter();
                        }
                    }
                });
                builder.show();

                return true;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        dbManager = new DatabaseManager(
            NotatkiActivity.this, // TODO check activity z galerią zdjęć
            getString(R.string.dbName), // database name
            null,
            7 // database version
        );

        notes = dbManager.getAllNotes();
        updateAdapter();
    }
}
