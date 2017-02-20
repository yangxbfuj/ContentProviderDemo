package com.yxb.contentproviderdemo;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class MyContentProvider extends ContentProvider {

    public static final String CONTENT = "content://";
    public static final String AUTHORY = "com.yxb.contentproviderdemo.MyContentProvider";

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORY;
    public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd." + AUTHORY;

    public static final Uri USER_INTO_URI = Uri.parse(CONTENT + AUTHORY + "/" + ContentProviderDemoSQLiteHelper.USER_INFO);

    static final int USER_INFOS = 1;
    static final int USER_ITEM = 2;

    static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORY,ContentProviderDemoSQLiteHelper.USER_INFO,USER_INFOS);
        uriMatcher.addURI(AUTHORY,ContentProviderDemoSQLiteHelper.USER_INFO + "/#",USER_ITEM);
    }

    private SQLiteDatabase mSQLiteDatabase;

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return mSQLiteDatabase.delete(ContentProviderDemoSQLiteHelper.USER_INFO,selection,selectionArgs);
    }

    @Override
    public String getType(Uri uri) {
        Log.d(this.getClass().getSimpleName(),uri.toString());
        switch (uriMatcher.match(uri)){
            case USER_INFOS:
                return CONTENT_TYPE;
                //return null;
            case USER_ITEM:
                return CONTENT_TYPE_ITEM;
                //return null;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(this.getClass().getSimpleName(),uri.toString());
        long newId = 0;
        Uri newUri = null;
        Log.d(this.getClass().getSimpleName(),"match uri is " + uriMatcher.match(uri));
        switch (uriMatcher.match(uri)){
            case USER_INFOS:
                newId = mSQLiteDatabase.insert(ContentProviderDemoSQLiteHelper.USER_INFO,null,values);
                newUri = Uri.parse(CONTENT + AUTHORY + "/"
                    + ContentProviderDemoSQLiteHelper.USER_INFO + "/" + newId);
                break;
            default:
                break;
        }
        if(newId > 0){
            return newUri;
        }
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        mSQLiteDatabase = new ContentProviderDemoSQLiteHelper(getContext()).getWritableDatabase();
        // TODO: Implement this to initialize your content provider on startup.
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(this.getClass().getSimpleName(),"query match uri is " + uriMatcher.match(uri));
        Log.d(getClass().getSimpleName(),uri.toString());
        Cursor cursor = null;
        switch (uriMatcher.match(uri)){
            case USER_INFOS:
                cursor = mSQLiteDatabase.query(ContentProviderDemoSQLiteHelper.USER_INFO,projection,selection,
                        selectionArgs,null,null,sortOrder);
                break;
            case USER_ITEM:
                String id = uri.getPathSegments().get(1);
                cursor = mSQLiteDatabase.query(ContentProviderDemoSQLiteHelper.USER_INFO,projection,"id = ?",
                        new String[]{id},null,null,sortOrder);
                break;
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
         return mSQLiteDatabase.update(uri.getPath(),values,selection,selectionArgs);
    }
}
