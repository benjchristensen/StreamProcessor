package example.perf;

import java.util.concurrent.atomic.AtomicLong;

import org.reactivestreams.Subscription;

import example.APublisher;

/**
 * Synchronous and finite so we test object allocations etc, not the scheduling or threading
 */
public class FiniteSynchronousLongPublisher extends APublisher<Long> {

    public FiniteSynchronousLongPublisher(long num) {
        super((s) -> {
            final AtomicLong i = new AtomicLong();
            s.onSubscribe(new Subscription() {

                final AtomicLong capacity = new AtomicLong();

                @Override
                public void request(int n) {
                    if (capacity.getAndAdd(n) == 0) {
                        // start sending again if it wasn't already running
                        send();
                    }
                }

                private void send() {
                    // this would normally use an eventloop, actor, whatever
                    do {
                        long _i = i.incrementAndGet();
                        if (_i < num) {
                            s.onNext(_i);
                        } else {
                            s.onComplete();
                            return;
                        }
                    } while (capacity.decrementAndGet() > 0);
                }

                @Override
                public void cancel() {
                    capacity.set(-1);
                }

            });
        });
    }

}
