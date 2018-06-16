package com.joy.rxjava.observable;

import com.joy.rxjava.observer.Observer;
import com.joy.rxjava.queue.SimpleQueue;
import com.joy.rxjava.utils.RLog;

import java.util.Iterator;

/**
 * Created by joybar on 2018/6/15.
 */

public final class ObservableFromIterable<T> extends Observable<T> {
	final Iterable<? extends T> source;

	public ObservableFromIterable(Iterable<? extends T> source) {
		this.source = source;
	}

	public Iterable<? extends T> getSource() {
		return source;
	}

	@Override
	protected void subscribeActual(Observer<? super T> observer) {

		System.out.println("ObservableFromIterable subscribeActual");

		Iterator<? extends T> it = source.iterator();
		FromIterableDisposable<T> d = new FromIterableDisposable<T>(observer, it);
		observer.onSubscribe();

		//把it传给ObservableFlapMap#InnerObserver#onSubscribe(Disposable s)
//		FromIterableDisposable<T> d = new FromIterableDisposable<T>(source, it);
//		source.onSubscribe(d);

		observer.onSubscribe();
	}

	static final class FromIterableDisposable<T>  implements SimpleQueue<T> {

		final Observer<? super T> actual;
		final Iterator<? extends T> it;

		FromIterableDisposable(Observer<? super T> actual, Iterator<? extends T> it) {
			this.actual = actual;
			this.it = it;
		}

		@Override
		public T poll() throws Exception {
			RLog.printInfo("FromIterableDisposable: poll");
			return null;
		}

	}

}