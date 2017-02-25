package com.yxb.contentproviderdemo;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yangxb on 2017/2/25.
 */

public abstract class ContentProviderCommand<T>  {
    // 线程池
    private static ExecutorService sExecutor = Executors.newSingleThreadExecutor();
    //
    private final static Handler sUIHandler = new Handler(Looper.myLooper());

    public final void execute(){
        // 类似于加入线程池，等待调用
        sExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    postResult(doCommand());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 执行查询
     * @return
     */
    protected abstract T doCommand();

    /**
     *  结束时回调
     * @param result
     */
    protected void onPostExecute(T result){

    }

    private void postResult(final T result){
        sUIHandler.post(new Runnable() {
            @Override
            public void run() {
                onPostExecute(result);
            }
        });
    }
}
