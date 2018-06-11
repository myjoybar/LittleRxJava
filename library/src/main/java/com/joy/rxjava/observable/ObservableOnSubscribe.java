package com.joy.rxjava.observable;

/**
 * Created by joybar on 2018/6/11.
 */

public interface ObservableOnSubscribe<T> {


	void subscribe(ObservableEmitter<T> emitter) throws Exception;
}
