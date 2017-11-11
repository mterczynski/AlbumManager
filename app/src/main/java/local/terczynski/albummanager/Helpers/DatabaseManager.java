package local.terczynski.albummanager.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by 4ia2 on 2017-10-10.
 */
public class DatabaseManager extends SQLiteOpenHelper {

    // constructors:

    public DatabaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    // overrided methods:


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE tabela1 (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 'a' TEXT, 'b' TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tabela1");
        onCreate(db);
    }

    // methods:
    public ArrayList<Note> getAll(){

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Note> notes = new ArrayList<Note>();
        Cursor result = db.rawQuery("SELECT * FROM tabela1" , null);
        while(result.moveToNext()){
//            notes.add( new Note(
//                    "", "" , ""
//                //result.getString(result.getColumnIndex("title")),
//                //result.getString(result.getColumnIndex("text")),
//               // result.getString(result.getColumnIndex("filePath"))
//            ));
        }
        return notes;
    }

    public boolean insert(String title, String text, String color, String imagePath){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("text", text);
        contentValues.put("color", color);
        contentValues.put("image_path", imagePath);

        db.insertOrThrow("terczynski4ia2.db" /*nazwa bazy danych*/, null, contentValues);
        db.close();
        return true;
    }
}
