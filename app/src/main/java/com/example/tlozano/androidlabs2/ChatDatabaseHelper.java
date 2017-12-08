package com.example.tlozano.androidlabs2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;



public class ChatDatabaseHelper extends SQLiteOpenHelper {

    static String ACTIVITY_NAME = "ChatDatabaseHelper";

    private static int VERSION_NUM = 3;
    private static String DATABASE_NAME = "Messages.db";
    public static String ANDROID_TABLE = "android_table";
    private static String KEY_ID = "_ID";
    private static String KEY_MESSAGE = "MESSAGE";
    private static String[] COLUMNS_DB = {KEY_ID, KEY_MESSAGE};
    SQLiteDatabase database;
    static Cursor cursor;

    public ChatDatabaseHelper(Context ctx){
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + ANDROID_TABLE + " (" +
        KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        KEY_MESSAGE + " TEXT);");

        Log.i("ChatDatabaseHelper", "Calling onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + ANDROID_TABLE);
        Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVer + " newVersion=" + newVer);
        onCreate(db);
    }


    public ArrayList<String> getChatMessagesFromDB(){
        Log.i(ACTIVITY_NAME, "-- creating arraylist");
        ArrayList<String> message = new ArrayList<>();

        Log.i(ACTIVITY_NAME, "-- starting cursor");
        cursor = database.query(ANDROID_TABLE, COLUMNS_DB, null, null, null, null, null, null);
        Log.i(ACTIVITY_NAME, "Cursor’s  column count = " + cursor.getColumnCount() );


       /* while(!cursor.isAfterLast() )
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString( cursor.getColumnIndex( ChatDatabaseHelper.KEY_MESSAGE) ) );
*/
        if(cursor.moveToFirst()){
            do{
                String messageReturned = cursor.getString(cursor.getColumnIndex(KEY_MESSAGE));
                message.add(messageReturned);
            } while (cursor.moveToNext());
        }

        return message;
    }

    public long insertChatDB(String message){
        ContentValues content = new ContentValues();
        content.put(KEY_MESSAGE, message);
        long rowDB = database.insertWithOnConflict(ANDROID_TABLE, "NULL FIELD", content, SQLiteDatabase.CONFLICT_IGNORE);
        return rowDB;

    }
}
