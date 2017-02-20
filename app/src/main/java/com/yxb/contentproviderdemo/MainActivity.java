package com.yxb.contentproviderdemo;

import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.v4.content.ContentResolverCompat;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.idescout.sql.SqlScoutServer;

import java.util.ArrayList;
import java.util.Collection;

import static android.drm.DrmStore.DrmObjectType.CONTENT;
import static com.yxb.contentproviderdemo.MyContentProvider.AUTHORY;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        insertWithContentResolver();
//        insertWithContentProviderClient();
//        insertWithClientAndOperation();
//        insertWithResolverAndOperation();
//        insertWithAsyncQueryHandler();
        initView();
    }

    private void initView() {
        Button insert = (Button) findViewById(R.id.btn_insert);
        Button queryAll = (Button) findViewById(R.id.btn_query_all);
        Button queryOne = (Button) findViewById(R.id.btn_query_one);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //insertWithClientAndOperation();
                insertFromInput();
            }
        });
        queryAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Cursor cursor = queryAllFromProvider();
//                Log.d(MainActivity.this.getClass().getSimpleName(), "Cursor size is " + cursor.getCount());
                queryPerformanceTest();
            }
        });
        queryOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = queryOneFromProvider();
                cursor.moveToFirst();
                Log.d(MainActivity.this.getClass().getSimpleName(), "Cursor size is " + cursor.getCount() + " and id is "
                        + cursor.getString(cursor.getColumnIndex(ContentProviderDemoSQLiteHelper.USER_ID)));
            }
        });
    }

    private void insertWithContentResolver() {
        long timeB = SystemClock.uptimeMillis();
        ContentResolver contentResolverCompat = getContentResolver();
        int loop = 0;
        while (loop++ < 100) {
            ContentValues v = new ContentValues();
            v.put(ContentProviderDemoSQLiteHelper.USER_NAME, "A");
            v.put(ContentProviderDemoSQLiteHelper.USER_PHONE, "1");
            contentResolverCompat.insert(MyContentProvider.USER_INTO_URI, v);
        }
        long timeA = SystemClock.uptimeMillis();
        Log.d(getClass().getSimpleName(), String.format("Insert value in insertWithContentResolver() %s cost %d ms.",
                "all info", timeA - timeB));
    }

    private void insertWithContentProviderClient() {
        long timeB = SystemClock.uptimeMillis();
        ContentProviderClient contentProviderClient = getContentResolver().acquireContentProviderClient(MyContentProvider.USER_INTO_URI);
        int loop = 0;
        try {
            while (loop++ < 100) {
                ContentValues v = new ContentValues();
                v.put(ContentProviderDemoSQLiteHelper.USER_NAME, "A");
                v.put(ContentProviderDemoSQLiteHelper.USER_PHONE, "1");
                contentProviderClient.insert(MyContentProvider.USER_INTO_URI, v);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            contentProviderClient.release();
        }
        long timeA = SystemClock.uptimeMillis();
        Log.d(getClass().getSimpleName(), String.format("Insert value in insertWithContentProviderClient() %s cost %d ms.", "all info ", timeA - timeB));
    }

    private void insertWithClientAndOperation() {
        long timeB = SystemClock.uptimeMillis();
        ContentProviderClient contentProviderClient = getContentResolver().acquireContentProviderClient(MyContentProvider.USER_INTO_URI);
        int index = 0;
        ArrayList<ContentProviderOperation> ops = new ArrayList<>(100);
        try {
            while (index++ < 100) {
                ops.add(ContentProviderOperation.newInsert(MyContentProvider.USER_INTO_URI)
                        .withValue(ContentProviderDemoSQLiteHelper.USER_NAME, "A")
                        .withValue(ContentProviderDemoSQLiteHelper.USER_PHONE, "1")
                        .build());
            }
            timeB = SystemClock.uptimeMillis();
            ContentProviderResult[] results = contentProviderClient.applyBatch(ops);
        } catch (OperationApplicationException e) {
            Log.e(getClass().getSimpleName(), "e", e);
            e.printStackTrace();
        } catch (RemoteException e) {
            Log.e(getClass().getSimpleName(), "e", e);
            e.printStackTrace();
        } finally {
            contentProviderClient.release();
        }
        long timeA = SystemClock.uptimeMillis();
        Log.d(getClass().getSimpleName(), String.format("Insert value in insertWithClientAndOperation() %s cost %d ms.", "all info ", timeA - timeB));
    }

    private void insertWithResolverAndOperation() {
        long timeB = SystemClock.uptimeMillis();
        ContentResolver contentResolver = getContentResolver();
        int index = 0;
        ArrayList<ContentProviderOperation> ops = new ArrayList<>(100);
        try {
            while (index++ < 100) {
                ops.add(ContentProviderOperation.newInsert(MyContentProvider.USER_INTO_URI)
                        .withValue(ContentProviderDemoSQLiteHelper.USER_NAME, "A")
                        .withValue(ContentProviderDemoSQLiteHelper.USER_PHONE, "1")
                        .build());
            }
            timeB = SystemClock.uptimeMillis();
            contentResolver.applyBatch(AUTHORY, ops);
        } catch (OperationApplicationException e) {
            Log.e(getClass().getSimpleName(), "e", e);
            e.printStackTrace();
        } catch (RemoteException e) {
            Log.e(getClass().getSimpleName(), "e", e);
            e.printStackTrace();
        } finally {
            // do s.th
        }
        long timeA = SystemClock.uptimeMillis();
        Log.d(getClass().getSimpleName(), String.format("Insert value in insertWithResolverAndOperation() %s cost %d ms.", "all info ", timeA - timeB));
    }

    private void insertWithAsyncQueryHandler() {
        MyAsyncQueryHandler asyncQueryHandler = new MyAsyncQueryHandler(getContentResolver());
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContentProviderDemoSQLiteHelper.USER_NAME, "A");
        contentValues.put(ContentProviderDemoSQLiteHelper.USER_PHONE, "1");
        asyncQueryHandler.startInsert(0, null, MyContentProvider.USER_INTO_URI, contentValues);
    }

    private Cursor queryAllFromProvider() {
        long timeB = SystemClock.uptimeMillis();
        Cursor cursor = getContentResolver().query(MyContentProvider.USER_INTO_URI, new String[]{ContentProviderDemoSQLiteHelper.USER_NAME,
                ContentProviderDemoSQLiteHelper.USER_PHONE}, null, null, null);
        long timeA = SystemClock.uptimeMillis();
        Log.d(getClass().getSimpleName(), String.format("queryAllFromProvider() %s cost %d ms.", "all info ", timeA - timeB));
        return cursor;
    }

    private void queryPerformanceTest() {
        new Thread(new Runnable() {
            int loop = 100;
            @Override
            public void run() {
                long timeB = SystemClock.uptimeMillis();
                while (loop-- > 0) {
                    Cursor cursor = getContentResolver().query(MyContentProvider.USER_INTO_URI, new String[]{ContentProviderDemoSQLiteHelper.USER_NAME,
                            ContentProviderDemoSQLiteHelper.USER_PHONE}, ContentProviderDemoSQLiteHelper.USER_NAME + "=? and "
                            + ContentProviderDemoSQLiteHelper.USER_PHONE + "=?", new String[]{"A", "1"}, null);
                }
                long timeA = SystemClock.uptimeMillis();
                Log.d(MainActivity.this.getClass().getSimpleName(),
                        "queryPerformanceTest() loop " + loop + " times cost " + (timeA - timeB) + "ms");
            }
        }).start();

    }


    private Cursor queryOneFromProvider() {
        Uri uri = Uri.parse(MyContentProvider.CONTENT + MyContentProvider.AUTHORY + "/" + ContentProviderDemoSQLiteHelper.USER_INFO + "/2");
        Log.d(getClass().getSimpleName(), uri.toString());
        Cursor cursor = getContentResolver().query(uri,
                null, null, null, null);
        return cursor;
    }

    private void insertFromInput() {
        EditText nameText = (EditText) findViewById(R.id.name_input_edit);
        EditText phoneText = (EditText) findViewById(R.id.phone_input_edit);
        String name = nameText.getText().toString();
        String phone = phoneText.getText().toString();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "输入信息不全", Toast.LENGTH_SHORT).show();
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContentProviderDemoSQLiteHelper.USER_NAME, name);
        contentValues.put(ContentProviderDemoSQLiteHelper.USER_PHONE, phone);
        Uri uri = getContentResolver().insert(MyContentProvider.USER_INTO_URI, contentValues);
        Toast.makeText(this, "插入id为 " + uri.getLastPathSegment(), Toast.LENGTH_SHORT).show();
    }

}
