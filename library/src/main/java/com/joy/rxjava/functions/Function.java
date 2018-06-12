package com.joy.rxjava.functions;

/**
 * Created by joybar on 2018/6/11.
 */
public interface Function<T, R> {

	R apply(T t) throws Exception;
}

