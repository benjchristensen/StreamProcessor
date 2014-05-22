package example;

import java.util.function.Function;

import org.reactivestreams.Processor;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class MapProcessor<T, R> implements Processor<T, R> {

    private final Function<T, R> mapper;

    public MapProcessor(Function<T, R> mapper) {
        this.mapper = mapper;
    }

    private Subscriber<R> s;

    @Override
    public void subscribe(Subscriber<R> s) {
        this.s = s;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        if (s == null) {
            throw new IllegalStateException("Need a Subscriber before subscribing upwards");
        }
        s.onSubscribe(subscription);
    }

    @Override
    public void onNext(T t) {
        s.onNext(mapper.apply(t));
    }

    @Override
    public void onError(Throwable t) {
        s.onError(t);
    }

    @Override
    public void onComplete() {
        s.onComplete();
    }

}
