package local.terczynski.albummanager.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import local.terczynski.albummanager.R;

public class DatabaseManager extends SQLiteOpenHelper {

    private String dbName;
    private String noteTableName = "notes";
    private String mainFolderName;

    // constructors:

    public DatabaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        dbName = context.getResources().getString(R.string.dbName);
        mainFolderName = context.getResources().getString(R.string.mainFolderName);
    }

    public DatabaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
        dbName = context.getResources().getString(R.string.dbName);
        mainFolderName = context.getResources().getString(R.string.mainFolderName);
    }

    // overrided methods:


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + noteTableName + " (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 'title' TEXT, 'text' TEXT, 'color' TEXT, 'image_path' TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + noteTableName);
        onCreate(db);
    }

    public boolean insert(String title, String text, String color, String imagePath){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("text", text);
        contentValues.put("color", color);
        contentValues.put("image_path", imagePath);

        db.insertOrThrow( noteTableName, null, contentValues);
        db.close();
        return true;
    }

    public ArrayList<Note> getAllNotes(){

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Note> notes = new ArrayList<>();
        Cursor result = db.rawQuery("SELECT * FROM " + noteTableName, null);

        Log.d("note_edit", "");

        while(result.moveToNext()){
            String shortImagePath = "(...)" + result.getString(result.getColumnIndex("image_path")).split(mainFolderName)[1];

            Log.d("note_edit", "color in table: " + result.getString(result.getColumnIndex("color")));

            notes.add(new Note( // String Title, String Text, String Color, int id
                result.getString(result.getColumnIndex("title")),
                result.getString(result.getColumnIndex("text")),
                result.getString(result.getColumnIndex("color")),
                result.getInt(result.getColumnIndex("_id")),
                shortImagePath
            ));
        }
        return notes;
    }

    public int deleteNoteById(String noteId){
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(
            noteTableName,
            "_id = ? ",
            new String[]{noteId}
        );
    }

    public void updateNote(String noteId, String newTitle, String newColor, String newText){
        SQLiteDatabase db = this.getWritableDatabase();

        Log.d("note_edit", "before update: ");

        ContentValues contentValues = new ContentValues();
        contentValues.put("title", newTitle);
        contentValues.put("color", newColor);
        contentValues.put("text", newText);

        Log.d("note_edit", "content values set: ");
        Log.d("note_edit", "cv: newTitle: " + newTitle);
        Log.d("note_edit", "cv: newColor:  " + newColor);
        Log.d("note_edit", "cv: newTitle:  " + newText);

        int result = db.update(noteTableName, contentValues, "_id = ? ", new String[]{noteId});
        Log.d("note_edit", "update result: " + result);
        db.close();
    }

    public ArrayList<Note> getNotesSortedByTitle(){
        ArrayList<Note> notes =  getAllNotes();
        Collections.sort(notes, new Comparator<Note>() {
            @Override
            public int compare(Note note1, Note note2) {
                return note1.title.compareTo(note2.title);
            }
        });
        return notes;
    }

    public ArrayList<Note> getNotesSortedByColor(){
        ArrayList<Note> notes =  getAllNotes();
        Collections.sort(notes, new Comparator<Note>() {
            @Override
            public int compare(Note note1, Note note2) {
                return note1.color.compareTo(note2.color);
            }
        });
        return notes;
    }

    public ArrayList<Note> getNotesSortedByImagePath(){
        ArrayList<Note> notes =  getAllNotes();
        Collections.sort(notes, new Comparator<Note>() {
            @Override
            public int compare(Note note1, Note note2) {
                return note1.imagePath.compareTo(note2.imagePath);
            }
        });
        return notes;
    }
}
