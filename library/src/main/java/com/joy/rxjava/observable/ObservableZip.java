package com.joy.rxjava.observable;

import com.joy.rxjava.functions.Function;
import com.joy.rxjava.observer.Observer;
import com.joy.rxjava.utils.RLog;

import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by joybar on 2018/6/11.
 */

public class ObservableZip<T, R> extends Observable<R> {

    final Function<? super Object[], ? extends R> zipper;
    final ObservableSource<? extends T>[] sources;

    public ObservableZip(ObservableSource<? extends T>[] sources, Function<? super Object[], ?
            extends R> zipper) {
        this.sources = sources;
        this.zipper = zipper;
    }

    @Override
    public void subscribeActual(Observer<? super R> observer) {
        ObservableSource<? extends T>[] sources = this.sources;
        ZipCoordinator<T, R> zc = new ZipCoordinator<T, R>(observer, sources, zipper);
        zc.subscribe();
    }


    static final class ZipCoordinator<T, R> {
        final Observer<? super R> actual;
        final ObservableSource<? extends T>[] sources;
        final Function<? super Object[], ? extends R> zipper;
        final ZipObserver<T, R>[] observers;
        final T[] row;

        ZipCoordinator(Observer<? super R> actual, ObservableSource<? extends T>[] sources,
                       Function<? super Object[], ? extends R> zipper) {
            this.actual = actual;
            this.sources = sources;
            this.zipper = zipper;
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
                RLog.printInfo("ZipCoordinator: =========");
                sources[i].subscribe(observers[i]);
            }
        }

        public void drain() {
            final T[] os = row;
            outer:
            for (; ; ) {
                int length = observers.length;
                for (int i = 0; i < length; i++) {
                    RLog.printInfo("ZipCoordinator: =========drain--》" + i);
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
                                R result = zipper.apply(os);
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
            RLog.printInfo("ZipObserver: onSubscribe");
        }

        @Override
        public void onNext(T t) {
            RLog.printInfo("ZipObserver: onNext");
            queue.offer(t);
            parent.drain();
        }

        @Override
        public void onError(Throwable t) {
            RLog.printInfo("ZipObserver: onError");
            done = true;
            parent.drain();
        }

        @Override
        public void onComplete() {
            RLog.printInfo("ZipObserver: onComplete");
            done = true;
            parent.drain();
        }
    }


}


