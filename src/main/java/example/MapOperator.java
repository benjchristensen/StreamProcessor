package example;

import java.util.function.Function;

import org.reactivestreams.Operator;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class MapOperator<T, R> implements Operator<T, R> {

    private final Function<T, R> mapper;

    public MapOperator(Function<T, R> mapper) {
        this.mapper = mapper;
    }

    @Override
    public Subscriber<T> call(final Subscriber<R> o) {
        return new Subscriber<T>() {

            @Override
            public void onSubscribe(Subscription s) {
                o.onSubscribe(s);
            }

            @Override
            public void onNext(T t) {
                o.onNext(mapper.apply(t));
            }

            @Override
            public void onError(Throwable t) {
                o.onError(t);
            }

            @Override
            public void onComplete() {
                o.onComplete();
            }

        };
    }
}
