package com.glauber.android.popularmovies;

/**
 * Created by glauberl on 1/9/2017.
 */

public interface IAsyncTaskDelegate<T> {
    void onFinished(T t);
}