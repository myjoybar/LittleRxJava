package com.joy.rxjava.observable;

import com.joy.rxjava.functions.Function;
import com.joy.rxjava.observer.Observer;
import com.joy.rxjava.queue.SimpleQueue;
import com.joy.rxjava.utils.RLog;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by joybar on 2018/6/11.
 */

public class ObservableFlapMap<T, U> extends Observable<U> {

    final Function<? super T, ? extends ObservableSource<? extends U>> function;
    final ObservableSource<T> source;

    public ObservableFlapMap(ObservableSource<T> source, Function<? super T, ? extends
            ObservableSource<? extends U>> function) {
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
        // mapObserver为桥梁Observer对象，桥梁Observer的对应事件会分发到传入的observer对应的事件中，在apply方法中，把传入的泛型转成R
        // ，这样就完成了map操作符的功能
        MergeObserver mapObserver = new MergeObserver<T, U>(observer, function);
        //让ObservableCreate订阅桥梁Observer对象 ，订阅成功后，ObservableCreate中emitter中的事件会分发到桥梁Observer的对应事件
        source.subscribe(mapObserver);
    }

    static final class MergeObserver<T, U> implements Observer<T> {
        final Observer<? super U> actual;
        final Function<? super T, ? extends ObservableSource<? extends U>> mapper;

        final AtomicReference<InnerObserver<?, ?>[]> observers;
        static final InnerObserver<?, ?>[] EMPTY = new InnerObserver<?, ?>[0];

        MergeObserver(Observer<? super U> actual, Function<? super T, ? extends
                ObservableSource<? extends U>> mapper) {
            this.actual = actual;
            this.mapper = mapper;
            this.observers = new AtomicReference<InnerObserver<?, ?>[]>(EMPTY);
        }

        @Override
        public void onSubscribe() {
            actual.onSubscribe();
        }

        @Override
        public void onNext(T t) {
            RLog.printInfo("ObservableFlapMap: onNext");

            ObservableSource<? extends U> p = null;
            try {
                //p 的类型为ObservableSource<?>
                p = mapper.apply(t);
            } catch (Exception e) {
                onError(e);
                e.printStackTrace();
            }

            RLog.printInfo("p 的类型为ObservableSource<?>" + p.getClass().getName());
            InnerObserver<T, U> inner = new InnerObserver<T, U>(this);
            p.subscribe(inner);

        }

        @Override
        public void onError(Throwable error) {
            RLog.printInfo("ObservableFlapMap: onError");
            actual.onError(error);

        }

        @Override
        public void onComplete() {
            RLog.printInfo("ObservableFlapMap: onComplete");
            actual.onComplete();
        }

        void drain() {
            drainLoop();
        }

        void drainLoop() {
            RLog.printInfo("MergeObserver: drainLoop");

            final Observer<? super U> child = this.actual;
            InnerObserver<?, ?>[] inner = observers.get();
            InnerObserver<T, U> is = (InnerObserver<T, U>) inner[0];
            SimpleQueue<U> q = is.queue;
            U o = null;
            for (; ; ) {
                try {
                    o = q.poll();

                } catch (Throwable ex) {
                }
                if (o == null) {
                    break;
                }
                child.onNext(o);
            }
        }


        static final class InnerObserver<T, U> implements Observer<U> {

            final MergeObserver<T, U> parent;
            volatile SimpleQueue<U> queue;

            InnerObserver(MergeObserver<T, U> parent) {
                this.parent = parent;
            }

            @Override
            public void onSubscribe() {
                RLog.printInfo("inner: onSubscribe");
                parent.drain();
                //RxJava 里面是通过public void onSubscribe(Disposable s) 里面的参数s为queue赋值，
                // 参数s是通过ObservableFromIterable中source.onSubscribe(d)传值过来
            }

            @Override
            public void onNext(U t) {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        }

    }


}
