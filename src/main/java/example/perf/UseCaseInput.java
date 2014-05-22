package example.perf;

import java.util.concurrent.CountDownLatch;

import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.logic.BlackHole;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@State(Scope.Thread)
public class UseCaseInput {
    @Param({ "1", "1024" })
    public int size;

    public Subscriber<String> subscriber;

    private CountDownLatch latch;

    @Setup
    public void setup() {
        final BlackHole bh = new BlackHole();
        latch = new CountDownLatch(1);

        subscriber = new Subscriber<String>() {

            long c = 0;

            @Override
            public void onSubscribe(Subscription s) {
                s.request(Integer.MAX_VALUE);
            }

            @Override
            public void onNext(String t) {
                bh.consume(t);
            }

            @Override
            public void onError(Throwable t) {
                throw new RuntimeException(t);
            }

            @Override
            public void onComplete() {
                latch.countDown();
            }

        };

    }

    public void awaitCompletion() throws InterruptedException {
        latch.await();
    }
}