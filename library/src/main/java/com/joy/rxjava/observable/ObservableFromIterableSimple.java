package com.joy.rxjava.observable;

import com.joy.rxjava.observer.Observer;

/**
 * Created by joybar on 2018/6/15.
 */

public final class ObservableFromIterableSimple<T> extends Observable<T> {
    final Iterable<? extends T> source;

    public ObservableFromIterableSimple(Iterable<? extends T> source) {
        this.source = source;
    }

    public Iterable<? extends T> getSource() {
        return source;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {

    }
}