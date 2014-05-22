package example;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import example.perf.FiniteSynchronousLongPublisher;

public class Test {

    public static void main(String[] args) {
        Test t = new Test();
        /* these work */
                t.mapToStringViaOperator();
//        t.mapToStringViaProcessorWithSupplier();

        /* these have problems */
        //        t.mapToStringViaProcessor();
        //        t.mapToStringViaBrokenProcessor();

    }

    private APublisher<Long> getPublisher() {
        //        return new LongPublisher();
        return new FiniteSynchronousLongPublisher(100);
    }

    private void mapToStringViaOperator() {
        Publisher<String> p = getPublisher().lift(new MapOperator<Long, String>(l -> "Num: " + l));
        p.subscribe(new StringSubscriber("A"));
        p.subscribe(new StringSubscriber("B"));
    }

    private void mapToStringViaProcessor() {
        Publisher<String> p = getPublisher().process(new MapProcessor<Long, String>(l -> "Num: " + l));
        p.subscribe(new StringSubscriber("A"));
        p.subscribe(new StringSubscriber("B"));
    }

    private void mapToStringViaProcessorWithSupplier() {
        Publisher<String> p = getPublisher().process(() -> new MapProcessor<Long, String>(l -> "Num: " + l));
        p.subscribe(new StringSubscriber("A"));
        p.subscribe(new StringSubscriber("B"));
    }

    private void mapToStringViaBrokenProcessor() {
        getPublisher().processSimple(new MapProcessor<Long, String>(l -> "Num: " + l)).subscribe(new StringSubscriber("A"));
    }

    private void simpleSubscribeAndTake10() {
        getPublisher().subscribe(new Subscriber<Long>() {

            private Subscription s;

            @Override
            public void onSubscribe(Subscription s) {
                this.s = s;
                s.request(10);
            }

            @Override
            public void onNext(Long t) {
                System.out.println(t);
                if (t >= 10) {
                    s.cancel();
                }
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("completed");
            }

        });
    }

    public static class StringSubscriber implements Subscriber<String> {

        private Subscription s;
        private int count = 0;
        private final String id;

        public StringSubscriber(String id) {
            this.id = id;
        }

        @Override
        public void onSubscribe(Subscription s) {
            System.out.println("Subscriber " + id + " => subscription");
            this.s = s;
            s.request(10);
        }

        @Override
        public void onNext(String t) {
            System.out.println("Subscriber " + id + " => " + t);
            if (count++ >= 10) {
                s.cancel();
            }
        }

        @Override
        public void onError(Throwable t) {
            t.printStackTrace();
        }

        @Override
        public void onComplete() {
            System.out.println("Subscriber " + id + " => Completed");
        }

    };
}
