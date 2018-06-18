package com.joy.rxjava.functions;

/**
 * Created by joybar on 17/06/2018.
 */

public interface BiFunction<T1, T2, R> {

    /**
     * Calculate a value based on the input values.
     * @param t1 the first value
     * @param t2 the second value
     * @return the result value
     * @throws Exception on error
     */
    R apply(T1 t1, T2 t2) throws Exception;
}
