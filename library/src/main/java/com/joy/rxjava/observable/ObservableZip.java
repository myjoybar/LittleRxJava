package com.joy.rxjava.observable;

import com.joy.rxjava.functions.BiFunction;
import com.joy.rxjava.observer.Observer;

import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by joybar on 2018/6/11.
 */

public class ObservableZip<T, R> extends Observable<R> {

    BiFunction<? super Object, ? super Object, R> biFunction;
    final ObservableSource<? extends T>[] sources;

    public ObservableZip(ObservableSource<? extends T>[] sources, BiFunction<? super Object, ? super Object, R> biFunction) {
        this.sources = sources;
        this.biFunction = biFunction;
    }

    @Override
    public void subscribeActual(Observer<? super R> observer) {
        ObservableSource<? extends T>[] sources = this.sources;
        ZipCoordinator<T, R> zc = new ZipCoordinator<T, R>(observer, sources, biFunction);
        zc.subscribe();
    }

    static final class ZipCoordinator<T, R> {
        final Observer<? super R> actual;
        final ObservableSource<? extends T>[] sources;
        final BiFunction<? super Object, ? super Object, R> biFunction;
        final ZipObserver<T, R>[] observers;
        final T[] row;

        ZipCoordinator(Observer<? super R> actual, ObservableSource<? extends T>[] sources,
                       BiFunction<? super Object, ? super Object, R> biFunction) {
            this.actual = actual;
            this.sources = sources;
            this.biFunction = biFunction;
            this.observers = new ZipObserver[sources.length];
            this.row = (T[]) new Object[sources.length];
        }

        public void subscribe() {
            int len = observers.length;
            for (int i = 0; i < len; i++) {
                observers[i] = new ZipObserver<T, R>(this);
            }
            //通知观察者被订阅，
            actual.onSubscribe();
            for (int i = 0; i < len; i++) {
                sources[i].subscribe(observers[i]);
            }
        }

        public void drain() {
            final T[] os = row;
            outer:
            for (; ; ) {
                int length = observers.length;
                for (int i = 0; i < length; i++) {
                    ZipObserver<T, R> zipObserver = observers[i];
                    Queue<T> queue = zipObserver.queue;
                    if (queue.isEmpty()) {
                        if (observers[i].done) {
                            actual.onComplete();
                        }
                        break outer;
                    }
                    if (i == 1) {
                        os[0] = observers[0].queue.poll();
                        os[1] = observers[1].queue.poll();
                        if (null != os[0] && null != os[1]) {
                            try {
                                R result = biFunction.apply(os[0],os[1]);
                                actual.onNext(result);
                                Arrays.fill(os, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                                actual.onError(e);
                            }
                        }
                    }
                }
            }
        }
    }


    static final class ZipObserver<T, R> implements Observer<T> {

        final ZipCoordinator<T, R> parent;
        final Queue<T> queue = new LinkedBlockingQueue<>();
        volatile boolean done;

        ZipObserver(ZipCoordinator<T, R> parent) {
            this.parent = parent;
        }

        @Override
        public void onSubscribe() {
        }

        @Override
        public void onNext(T t) {
            queue.offer(t);
            parent.drain();
        }

        @Override
        public void onError(Throwable t) {
            parent.drain();
        }

        @Override
        public void onComplete() {
            done = true;
            parent.drain();
        }
    }

}


