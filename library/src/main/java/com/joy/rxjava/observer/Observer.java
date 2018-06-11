package com.joy.rxjava.observer;

/**
 * Created by joybar on 2018/6/11.
 */

public interface Observer<T> {

	void onSubscribe();

	void onNext(T value);

	void onError(Throwable e);

	void onComplete();
}
