package com.yxb.contentproviderdemo;

import android.database.ContentObserver;
import android.os.Handler;

/**
 * Created by yangxb on 2017/2/21.
 */

public class MyContentObserver extends ContentObserver {
    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public MyContentObserver(Handler handler) {
        super(handler);
    }
}
