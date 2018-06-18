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
	final ObservableSource<T> source;	//source 为create 中创建的ObservableOnSubscribe对象

	public ObservableMap(ObservableSource<T> source, Function<? super T, ? extends U> function) {
		this.source = source;
		this.function = function;
	}

	public final ObservableSource<T> source() {
		return source;
	}

	@Override
	public void subscribeActual(Observer<? super U> observer) {
		//传入的observer为被订阅的观察者
		// mapObserver也是一个Observer对象，起到了桥接source（被观察者）和Observer（观察者）的作用，
		// 的对应事件会分发到传入的observer对应的事件中，在apply方法中，把传入的泛型转成R，这样就完成了map操作符的功能
		MapObserver mapObserver = new MapObserver<T, U>(observer, function);
		//source订阅mapObserver之后 ，订阅成功后，source的emitter中的事件会分发给mapObserver,
		// 在mapObserver通过apply方法，把传入的泛型T转成结果R，再通过onNext发送给真正的观察者actual，这样就完成了事件消息的传递和转换
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
			actual.onSubscribe();
		}

		@Override
		public void onNext(T t) {
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
			actual.onError(error);
		}

		@Override
		public void onComplete() {
			actual.onComplete();
		}

	}
}
