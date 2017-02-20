package com.yxb.contentproviderdemo;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;

/**
 * Created by yangxb on 2017/2/18.
 */

public class MyAsyncQueryHandler extends AsyncQueryHandler {

    public MyAsyncQueryHandler(ContentResolver cr) {
        super(cr);
    }

}
