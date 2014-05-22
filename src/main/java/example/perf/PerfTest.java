package example.perf;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;

import example.MapOperator;
import example.MapProcessor;

public class PerfTest {

    @GenerateMicroBenchmark
    public void liftSingleSubscribe(UseCaseInput input) throws InterruptedException {
        new FiniteSynchronousLongPublisher(input.size).lift(new MapOperator<Long, String>(l -> "Num: " + l)).subscribe(input.subscriber);
        input.awaitCompletion();
    }

    @GenerateMicroBenchmark
    public void liftMultiSubscribe(UseCaseInput input) throws InterruptedException {
        FiniteSynchronousLongPublisher p = new FiniteSynchronousLongPublisher(input.size);
        p.lift(new MapOperator<Long, String>(l -> "Num: " + l)).subscribe(input.subscriber);
        p.lift(new MapOperator<Long, String>(l -> "Num: " + l)).subscribe(input.subscriber);
        input.awaitCompletion();
    }

    @GenerateMicroBenchmark
    public void liftMultiStep(UseCaseInput input) throws InterruptedException {
        new FiniteSynchronousLongPublisher(input.size)
                .lift(new MapOperator<Long, String>(l -> String.valueOf(l)))
                .lift(new MapOperator<String, Long>(s -> Long.parseLong(s)))
                .lift(new MapOperator<Long, String>(l -> String.valueOf(l)))
                .subscribe(input.subscriber);
        input.awaitCompletion();
    }

    @GenerateMicroBenchmark
    public void processWithoutSupplier(UseCaseInput input) throws InterruptedException {
        new FiniteSynchronousLongPublisher(input.size).process(new MapProcessor<Long, String>(l -> "Num: " + l)).subscribe(input.subscriber);
        input.awaitCompletion();
    }

    @GenerateMicroBenchmark
    public void processWithSupplier(UseCaseInput input) throws InterruptedException {
        new FiniteSynchronousLongPublisher(input.size).process(() -> new MapProcessor<Long, String>(l -> "Num: " + l)).subscribe(input.subscriber);
        input.awaitCompletion();
    }

    @GenerateMicroBenchmark
    public void processWithSupplierMultistep(UseCaseInput input) throws InterruptedException {
        new FiniteSynchronousLongPublisher(input.size)
                .process(() -> new MapProcessor<Long, String>(l -> String.valueOf(l)))
                .process(() -> new MapProcessor<String, Long>(s -> Long.parseLong(s)))
                .process(() -> new MapProcessor<Long, String>(l -> String.valueOf(l)))
                .subscribe(input.subscriber);
        input.awaitCompletion();
    }

    @GenerateMicroBenchmark
    public void processWithSupplierMultiSubscribe(UseCaseInput input) throws InterruptedException {
        FiniteSynchronousLongPublisher p = new FiniteSynchronousLongPublisher(input.size);
        p.process(() -> new MapProcessor<Long, String>(l -> "Num: " + l)).subscribe(input.subscriber);
        p.process(() -> new MapProcessor<Long, String>(l -> "Num: " + l)).subscribe(input.subscriber);
        input.awaitCompletion();
    }

}
