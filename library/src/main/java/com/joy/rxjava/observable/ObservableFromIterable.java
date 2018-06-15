package com.joy.rxjava.observable;

import com.joy.rxjava.observer.Observer;

import java.util.Iterator;

/**
 * Created by joybar on 2018/6/15.
 */

public final class ObservableFromIterable<T> extends Observable<T> {
	final Iterable<? extends T> source;

	public ObservableFromIterable(Iterable<? extends T> source) {
		this.source = source;
	}

	@Override
	protected void subscribeActual(Observer<? super T> observer) {
		Iterator<? extends T> it = source.iterator();
		FromIterableDisposable<T> d = new FromIterableDisposable<T>(observer, it);
		observer.onSubscribe();
		d.run();
	}

	static final class FromIterableDisposable<T> {

		final Observer<? super T> actual;
		final Iterator<? extends T> it;

		FromIterableDisposable(Observer<? super T> actual, Iterator<? extends T> it) {
			this.actual = actual;
			this.it = it;
		}

		public void run() {
			while (it.hasNext()) {
				T v = it.next();
				actual.onNext(v);
			}
			actual.onComplete();
		}
	}

}