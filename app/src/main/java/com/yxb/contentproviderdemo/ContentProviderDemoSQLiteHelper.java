package com.yxb.contentproviderdemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yangxb on 2017/2/16.
 */

public class ContentProviderDemoSQLiteHelper extends SQLiteOpenHelper {

    private static final String DBNAME = "ContentProviderDemoSQLite.db";
    private static final int DB_VERSION = 1;
    public static final String USER_INFO = "user_info";
    public static final String USER_NAME = "user_name";
    public static final String USER_PHONE = "user_phone";
    public static final String USER_ID = "id";

    private static final String CreateUserTable = "CREATE TABLE " + USER_INFO + " ("
            + USER_NAME + " TEXT ,"
            + USER_PHONE +" TEXT ,"
            + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
            + ")";


    ContentProviderDemoSQLiteHelper(Context context){
        super(context,DBNAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CreateUserTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
