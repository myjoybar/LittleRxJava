package com.joy.rxjava.observable;

import com.joy.rxjava.observer.Observer;
import com.joy.rxjava.schedulers.Scheduler;
import com.joy.rxjava.utils.CheckUtils;
import com.joy.rxjava.utils.RLog;

/**
 * Created by joybar on 2018/6/11.
 */

public class ObservableSubscribeOn<T> extends Observable<T> {

	final Scheduler scheduler;
	final ObservableSource<T> source;

	public ObservableSubscribeOn(ObservableSource<T> source, Scheduler scheduler) {
		this.source = source;
		this.scheduler = scheduler;
	}

	public final ObservableSource<T> source() {
		return source;
	}

	@Override
	public void subscribeActual(final Observer<? super T> s) {
		final SubscribeOnObserver<T> parent = new SubscribeOnObserver<T>(s);
		scheduler.scheduleDirect(new Scheduler.Worker() {
			@Override
			protected void execute() {
				RLog.printInfo("我在这里切换");
				source.subscribe(parent);
			}
		});
	}


	static final class SubscribeOnObserver<T>  implements Observer<T> {
		final Observer<? super T> actual;

		SubscribeOnObserver(Observer<? super T> actual) {
			this.actual = actual;
		}
		@Override
		public void onSubscribe() {
			actual.onSubscribe();
		}

		@Override
		public void onNext(T t) {
			CheckUtils.checkNotNull(t, "onNext called parameter can not be null");
			actual.onNext(t);
		}

		@Override
		public void onError(Throwable error) {
			actual.onError(error);

		}

		@Override
		public void onComplete() {
			actual.onComplete();
		}

	}
}
