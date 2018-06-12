package com.joy.rxjava.observable;

import com.joy.rxjava.observer.Observer;
import com.joy.rxjava.utils.CheckUtils;
import com.joy.rxjava.utils.RLog;

/**
 * Created by joybar on 2018/6/11.
 */

public final class ObservableCreate<T> extends Observable<T> {

	final ObservableOnSubscribe<T> source;//create 中传递的对象，执行subscribe会真正开始执行

	public ObservableCreate(ObservableOnSubscribe<T> source) {
		this.source = source;
	}

	@Override
	protected void subscribeActual(Observer<? super T> observer) {
		//observer为观察者，当emitter中的方法执行时，会调用observer中的相关方法
		CreateEmitter<T> parent = new CreateEmitter<T>(observer);
		//通知观察者被订阅，
		observer.onSubscribe();
		try {
			//真正开始执行，执行emitter中的方法
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
			RLog.printInfo("ObservableCreate: onNext");
			CheckUtils.checkNotNull(t,"onNext called parameter can not be null");
			if (!isDisposed()) {
				observer.onNext(t);
			}
		}

		@Override
		public void onError(Throwable error) {
			if (error == null) {
				error = new NullPointerException("onError called with null. Null values are generally not allowed ");
			}
			if (!isDisposed()) {
				observer.onError(error);
			}

		}

		@Override
		public void onComplete() {
			RLog.printInfo("ObservableCreate: onComplete");
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
