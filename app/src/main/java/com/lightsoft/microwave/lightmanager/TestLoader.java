package com.lightsoft.microwave.lightmanager;

import android.content.Context;
import android.content.Loader;

/**
 * Created by lightwave on 30.07.15.
 */
public class TestLoader extends Loader<Integer> {

    /**
     * Stores away the application context associated with context.
     * Since Loaders can be used across multiple activities it's dangerous to
     * store the context directly; always use {@link #getContext()} to retrieve
     * the Loader's Context, don't use the constructor argument directly.
     * The Context returned by {@link #getContext} is safe to use across
     * Activity instances.
     *
     * @param context used to retrieve the application context.
     */
    public TestLoader(Context context) {
        super(context);

    }

    @Override
    protected void onStartLoading(){

    }

    protected void onStopLoading(){

    }

    protected void onForceLoading(){

    }

    @Override
    protected void onAbandon() {
        super.onAbandon();
    }

    @Override
    protected void onReset() {
        super.onReset();
    }

    void getResultFromTask(Integer res){

    }
}
