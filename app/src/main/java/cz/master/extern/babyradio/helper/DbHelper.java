package cz.master.extern.babyradio.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Yasir Iqbal on 7/17/2016.
 */
public class DbHelper extends SQLiteOpenHelper {

    String logTableName = "logTable", colLog = "log";


    public DbHelper(Context context) {

        super(context, "babyradio", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableLog = "create table " + logTableName + " (" + colLog + " text);";
        db.execSQL(createTableLog);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertLog(String logMessage) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(colLog, logMessage);
        long res = db.insert(logTableName, null, values);
        db.close();
        return res;
    }

    public ArrayList<String> getAllLog() {
        ArrayList<String> allLog = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + logTableName, null, null);
        if (cursor.moveToFirst()) {
            do {
                allLog.add(cursor.getString(cursor.getColumnIndex(colLog)));
            } while (cursor.moveToNext());
        }
        db.close();
        return allLog;
    }

    public void deleteAllLog() {
        SQLiteDatabase db = getWritableDatabase();
        int deleteRecord = db.delete(logTableName, null, null);
        Log.d("deleted Record", deleteRecord + "");
        db.close();
    }
}
