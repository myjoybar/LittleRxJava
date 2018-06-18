package com.joy.rxjava.observable;

/**
 * Created by joybar on 2018/6/11.
 */

public interface ObservableEmitter<T> extends Emitter<T> {

	ObservableEmitter<T> serialize();
}
