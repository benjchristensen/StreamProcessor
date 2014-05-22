package example;

import java.util.concurrent.atomic.AtomicLong;

import org.reactivestreams.Subscription;

public class LongPublisher extends APublisher<Long> {

    public LongPublisher() {
        super((s) -> {
            final AtomicLong i = new AtomicLong();
            s.onSubscribe(new Subscription() {

                final AtomicLong capacity = new AtomicLong();

                @Override
                public void request(int n) {
                    System.out.println("signalAdditionalDemand => " + n);
                    if (capacity.getAndAdd(n) == 0) {
                        // start sending again if it wasn't already running
                        send();
                    }
                }

                private void send() {
                    System.out.println("send => " + capacity.get());
                    // this would normally use an eventloop, actor, whatever
                    new Thread(new Runnable() {

                        public void run() {
                            do {
                                s.onNext(i.incrementAndGet());
                            } while (capacity.decrementAndGet() > 0);
                        }
                    }).start();
                }

                @Override
                public void cancel() {
                    capacity.set(-1);
                }

            });
        });
    }

}
