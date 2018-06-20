package com.joy.rxjava.functions;

/**
 * Created by joybar on 17/06/2018.
 */

public interface BiFunction<T1, T2, R> {

    R apply(T1 t1, T2 t2) throws Exception;
}
