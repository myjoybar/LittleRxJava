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
				source.subscribe(parent);
			}
		});
	}


	static final class SubscribeOnObserver<T>  implements Observer<T> {
		private static final long serialVersionUID = 8094547886072529208L;
		final Observer<? super T> actual;

		SubscribeOnObserver(Observer<? super T> actual) {
			this.actual = actual;
		}
		@Override
		public void onSubscribe() {

		}

		@Override
		public void onNext(T t) {
			RLog.printInfo("ObservableSubscribeOn: onNext");
			CheckUtils.checkNotNull(t, "onNext called parameter can not be null");
			actual.onNext(t);
		}

		@Override
		public void onError(Throwable error) {
			RLog.printInfo("ObservableSubscribeOn: onComplete");
			actual.onError(error);

		}

		@Override
		public void onComplete() {
			RLog.printInfo("ObservableSubscribeOn: onComplete");
			actual.onComplete();

		}

	}
}
