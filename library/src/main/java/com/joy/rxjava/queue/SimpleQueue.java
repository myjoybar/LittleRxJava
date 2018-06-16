package com.joy.rxjava.queue;

import android.support.annotation.Nullable;

/**
 * Created by joybar on 16/06/2018.
 */

public interface SimpleQueue<T>  {
    @Nullable
    T poll() throws Exception;
}