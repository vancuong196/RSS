package com.cuong.rss;



        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;

        import java.util.ArrayList;
        import java.util.List;



/**
 * Created by USER on 19/10/2017.
 */

public class DataBaseAcess extends SQLiteOpenHelper {


    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "rss_application_db";
    private static final String TABLE_NAME = "RSS";
    private static final String ID = "Rss_Id";
    private static final String NAME = "Rss_Name";
    private static final String LINK = "Rss_Link";



    public DataBaseAcess(Context context)
    {
        super(context,DATABASE_NAME,null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String script = "CREATE TABLE " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY, " +
                NAME + " TEXT, " +
                LINK + " TEXT" + ")";
        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }

    public int addRss(RSS rss){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, rss.getmTitle());
        values.put(LINK, rss.getmLink());
        db.insert(TABLE_NAME, null, values);
        db = this.getReadableDatabase();
        String statement = "SELECT * FROM " + TABLE_NAME +" ORDER BY " + ID + " DESC LIMIT 1";
        Cursor cursor = db.rawQuery(statement, null);
        if (cursor != null)
            cursor.moveToFirst();
        db.close();
        return Integer.parseInt(cursor.getString(0));
    }

    public RSS getRss(int id){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{ ID, NAME, LINK},
                ID + " =?", new String[]{String.valueOf(id)},null,null,null,null);
        if (cursor != null)
            cursor.moveToFirst();

        RSS rss = new RSS(cursor.getString(0),cursor.getString(1),cursor.getString(2));

        return rss;
    }

    public ArrayList<RSS> getAllRss(){

        ArrayList<RSS> rssList = new ArrayList<RSS>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){
            do {
                RSS rss = new RSS();
                rss.setId(cursor.getString(0));
                rss.setmTitle(cursor.getString(1));
                rss.setmLink(cursor.getString(2));
                rssList.add(rss);
            } while (cursor.moveToNext());
        }
        return rssList;
    }

    public int getCount(){

        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery,null);

        int count = cursor.getCount();

        cursor.close();

        return count;
    }

    public int updateRss(RSS rss){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, rss.getmTitle());
        values.put(LINK, rss.getmLink());

        return  db.update(TABLE_NAME, values, ID + " =?", new String[]{rss.getId()});

    }
    public void deleteRss(RSS rss){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID + " =?", new String[]{String.valueOf(rss.getId())});
        db.close();
    }

    public  void deleteAll(){

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME,null,null);
        db.close();
    }
}