package com.joy.rxjava.functions;

/**
 * Created by joybar on 17/06/2018.
 */

public interface BiFunction<T1, T2, R> {

    R apply(T1 value1, T2 value2) throws Exception;
}
