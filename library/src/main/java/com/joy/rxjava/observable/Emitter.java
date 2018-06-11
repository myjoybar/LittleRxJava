package com.joy.rxjava.observable;

/**
 * Created by joybar on 2018/6/11.
 */
public interface Emitter<T> {


	void onNext(T value);


	void onError(Throwable error);


	void onComplete();
}
