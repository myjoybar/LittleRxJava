package com.joy.rxjava.observable;

import com.joy.rxjava.observer.Observer;
import com.joy.rxjava.schedulers.Scheduler;
import com.joy.rxjava.utils.CheckUtils;

/**
 * Created by joybar on 2018/6/13.
 */

public class ObservableObserveOn<T> extends Observable<T> {

	final Scheduler scheduler;
	final ObservableSource<T> source;

	public ObservableObserveOn(ObservableSource<T> source, Scheduler scheduler) {
		this.source = source;
		this.scheduler = scheduler;
	}

	public final ObservableSource<T> source() {
		return source;
	}

	@Override
	public void subscribeActual(final Observer<? super T> s) {
		final ObserveOnObserver<T> parent = new ObserveOnObserver<T>(s, scheduler);
		source.subscribe(parent);

	}

	static final class ObserveOnObserver<T> implements Observer<T> {
		final Observer<? super T> actual;
		final Scheduler scheduler;

		public ObserveOnObserver(Observer<? super T> actual, Scheduler scheduler) {
			this.actual = actual;
			this.scheduler = scheduler;
		}

		@Override
		public void onSubscribe() {
			scheduler.scheduleDirect(new Scheduler.Worker() {
				@Override
				protected void execute() {
					actual.onSubscribe();
				}
			});
		}

		@Override
		public void onNext(final T t) {
			scheduler.scheduleDirect(new Scheduler.Worker() {
				@Override
				protected void execute() {
					CheckUtils.checkNotNull(t, "onNext called parameter can not be null");
					actual.onNext(t);
				}
			});
		}

		@Override
		public void onError(final Throwable error) {
			scheduler.scheduleDirect(new Scheduler.Worker() {
				@Override
				protected void execute() {
					actual.onError(error);
				}
			});

		}

		@Override
		public void onComplete() {
			scheduler.scheduleDirect(new Scheduler.Worker() {
				@Override
				protected void execute() {
					actual.onComplete();
				}
			});
		}
	}

}
