package com.joy.rxjava.functions;

/**
 * Created by joybar on 2018/6/11.
 */
public interface Function<T, R> {
	// T 表示输入值，R 表示输出值,把T转换成R，
	R apply(T t) throws Exception;
}

