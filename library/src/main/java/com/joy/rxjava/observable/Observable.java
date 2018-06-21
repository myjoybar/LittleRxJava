package com.joy.rxjava.observable;

import com.joy.rxjava.functions.BiFunction;
import com.joy.rxjava.functions.Function;
import com.joy.rxjava.observer.Observer;
import com.joy.rxjava.schedulers.Scheduler;
import com.joy.rxjava.utils.CheckUtils;


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

	public final <R> Observable<R> flatMapIterable(Function<? super T, Iterable<R>> mapper) {
		return new ObservableFlapMapIterable<>(this, mapper);
	}
	public final <R> Observable<R> flatMapArray(Function<? super T, R[]> mapper) {
		return new ObservableFlapMapArray<>(this, mapper);
	}

	public static <T> Observable<T> fromIterableSimple(Iterable<? extends T> source) {
		return new ObservableFromIterableSimple<>(source);
	}

	public static <T1, T2, R> Observable<R> zip(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, BiFunction<? super
			T1, ? super T2, ? extends R> zipper) {
		// return zipArray(toFunction(zipper), source1, source2);
		return zipArrayBiFunction((BiFunction<? super Object, ? super Object, R>) zipper, source1, source2);
	}


	public static <T1, T2, R> Function<Object[], R> toFunction(final BiFunction<? super T1, ? super T2, ? extends R> f) {
		return new Function<Object[], R>() {
			@Override
			public R apply(Object[] a) throws Exception {
				CheckUtils.checkError(a.length != 2, "Array of size 2 expected but got " + a.length);
				return ((BiFunction<Object, Object, R>) f).apply(a[0], a[1]);
			}
		};
	}

	public static <T, R> Observable<R> zipArray(Function<? super Object[], ? extends R> zipper, ObservableSource<? extends T>... sources) {
		// return new ObservableZip<T, R>(sources, zipper);
		return new ObservableZip<T, R>(sources, null);
	}

	public static <T, R> Observable<R> zipArrayBiFunction(BiFunction<? super Object, ? super Object, R> biFunction, ObservableSource<? extends T>...
			sources) {
		return new ObservableZip<T, R>(sources, biFunction);
	}

}
