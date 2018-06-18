package com.joy.rxjava.observable;

import com.joy.rxjava.functions.Function;
import com.joy.rxjava.observer.Observer;
import com.joy.rxjava.utils.CheckUtils;
import com.joy.rxjava.utils.RLog;

/**
 * Created by joybar on 2018/6/11.
 */

public class ObservableMap<T, U> extends Observable<U> {

	final Function<? super T, ? extends U> function;
	final ObservableSource<T> source;

	public ObservableMap(ObservableSource<T> source, Function<? super T, ? extends U> function) {
		this.source = source;
		this.function = function;
	}

	public final ObservableSource<T> source() {
		return source;
	}

	@Override
	public void subscribeActual(Observer<? super U> observer) {
		//observer 为传入的观察者
		//source 为  Observable.create中创建的ObservableCreate对象，mapObserver
		// mapObserver为桥梁Observer对象，桥梁Observer的对应事件会分发到传入的observer对应的事件中，在apply方法中，把传入的泛型转成R，这样就完成了map操作符的功能
		MapObserver mapObserver = new MapObserver<T, U>(observer, function);
		//让ObservableCreate订阅桥梁Observer对象 ，订阅成功后，ObservableCreate中emitter中的事件会分发到桥梁Observer的对应事件中,又会把事件分发到真正
		//的最后的观察者observer中，在桥梁Observerapply方法中，把传入的泛型转成结果R，通过onNext发送出去，这样就完成了map操作符的功能
		source.subscribe(mapObserver);
	}


	static final class MapObserver<T, U> implements Observer<T> {
		protected final Observer<? super U> actual;
		final Function<? super T, ? extends U> mapper;

		MapObserver(Observer<? super U> actual, Function<? super T, ? extends U> mapper) {
			this.actual = actual;
			this.mapper = mapper;
		}

		@Override
		public void onSubscribe() {
			RLog.printInfo("ObservableMap: onSubscribe");

		}

		@Override
		public void onNext(T t) {
			RLog.printInfo("ObservableMap: onNext");
			CheckUtils.checkNotNull(t, "onNext called parameter can not be null");
			U v = null;
			try {
				v = mapper.apply(t);
			} catch (Exception e) {
				e.printStackTrace();
			}
			actual.onNext(v);
		}

		@Override
		public void onError(Throwable error) {
			RLog.printInfo("ObservableMap: onError");
			actual.onError(error);

		}

		@Override
		public void onComplete() {
			RLog.printInfo("ObservableMap: onComplete");
			actual.onComplete();

		}

	}
}
