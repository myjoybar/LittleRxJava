package com.joy.rxjava.observable;

import com.joy.rxjava.observer.Observer;

/**
 * Created by joybar on 2018/6/11.
 */

public final class ObservableCreate<T> extends Observable<T> {

	final ObservableOnSubscribe<T> source;

	public ObservableCreate(ObservableOnSubscribe<T> source) {
		this.source = source;
	}

	@Override
	protected void subscribeActual(Observer<? super T> observer) {
		CreateEmitter<T> parent = new CreateEmitter<T>(observer);
		observer.onSubscribe();
		try {
			source.subscribe(parent);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	static final class CreateEmitter<T> implements ObservableEmitter<T> {
		private static final long serialVersionUID = -3434801548987643227L;

		final Observer<? super T> observer;

		CreateEmitter(Observer<? super T> observer) {
			this.observer = observer;
		}

		@Override
		public void onNext(T t) {
			if (t == null) {
				onError(new NullPointerException("onNext called with null"));
				return;
			}
			if (!isDisposed()) {
				observer.onNext(t);
			}
		}

		@Override
		public void onError(Throwable error) {
			if (error == null) {
				error = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
			}
			if (!isDisposed()) {
				observer.onError(error);
			}

		}

		@Override
		public void onComplete() {
			if (!isDisposed()) {
				observer.onComplete();

			}
		}

		@Override
		public boolean isDisposed() {
			return false;
		}

		@Override
		public ObservableEmitter<T> serialize() {
			return null;
		}
	}
}
