package org.reactivestreams;

public interface Operator<T, R> {

    public Subscriber<T> call(final Subscriber<R> o);

}
