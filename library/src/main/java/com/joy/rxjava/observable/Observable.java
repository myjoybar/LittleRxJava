package com.joy.rxjava.observable;

import com.joy.rxjava.functions.Function;
import com.joy.rxjava.observer.Observer;
import com.joy.rxjava.schedulers.Scheduler;

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

	public final <R> Observable<R> map(Function<? super T, ? extends R> mapper) {

		return new ObservableMap<T, R>(this, mapper);
	}

	public final Observable<T> subscribeOn(Scheduler scheduler) {
		return new ObservableSubscribeOn<T>(this, scheduler);
	}

	public final Observable<T> observeOn(Scheduler scheduler) {
		return new ObservableObserveOn<T>(this, scheduler);
	}

	public final <R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends R>> mapper) {
		return new ObservableFlapMap(this, mapper);
	}
	public static <T> Observable<T> fromIterable(Iterable<? extends T> source) {
		return new ObservableFromIterable<T>(source);
	}

	public final <R> Observable<R> flatMapSimple(Function<? super T, ? extends ObservableSource<? extends R>> mapper) {
		return new ObservableFlapMapSimple<>(this, mapper);
	}

	public static <T> Observable<T> fromIterableSimple(Iterable<? extends T> source) {
		return new ObservableFromIterableSimple<>(source);
	}
}
