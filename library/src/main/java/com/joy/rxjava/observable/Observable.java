package com.joy.rxjava.observable;

import com.joy.rxjava.observer.Observer;

/**
 * Created by joybar on 2018/6/11.
 */

public abstract class Observable<T> implements ObservableSource<T> {


	public static <T> Observable<T> create(ObservableOnSubscribe<T> source) {
		return new ObservableCreate<T>(source);
	}
	@Override
	public void subscribe(Observer<? super T> observer) {
		subscribeActual(observer);
	}

	protected abstract void subscribeActual(Observer<? super T> observer);
}
