package example;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.reactivestreams.Operator;
import org.reactivestreams.Processor;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public class APublisher<T> implements Publisher<T> {

    private final Consumer<Subscriber<T>> f;

    protected APublisher(Consumer<Subscriber<T>> f) {
        this.f = f;
    }

    public <R> Publisher<R> process(Supplier<Processor<T, R>> supplier) {
        return new APublisher<R>((s) -> {
            Processor<T, R> p = supplier.get();
            p.subscribe(s);
            f.accept(p);
        });
    }

    public <R> Publisher<R> lift(Operator<T, R> lift) {
        return new APublisher<R>((s) -> {
            f.accept(lift.call(s));
        });
    }

    /**
     * This will blow up because it happens in the wrong order
     * 
     * @param p
     * @return
     */
    public <R> Publisher<R> processSimple(Processor<T, R> p) {
        subscribe(p);
        return p;
    }

    /**
     * This works for a single subscription, but not when subscribed to multiple times because the `Processor` instance gets reused
     * 
     * @param p
     * @return
     */
    public <R> Publisher<R> process(Processor<T, R> p) {
        return new APublisher<R>((s) -> {
            p.subscribe(s);
            f.accept(p);
        });
    }

    @Override
    public void subscribe(Subscriber<T> s) {
        f.accept(s);
    }

}
