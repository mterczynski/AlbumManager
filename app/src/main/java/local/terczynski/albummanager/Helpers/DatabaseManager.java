package local.terczynski.albummanager.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

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

        while(result.moveToNext()){
            String shortImagePath = "(...)" + result.getString(result.getColumnIndex("image_path")).split(mainFolderName)[1];

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
}
