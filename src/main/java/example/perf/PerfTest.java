package example.perf;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;

import example.MapOperator;
import example.MapProcessor;

public class PerfTest {

    @GenerateMicroBenchmark
    public void lift(UseCaseInput input) throws InterruptedException {
        new FiniteSynchronousLongPublisher(input.size).lift(new MapOperator<Long, String>(l -> "Num: " + l)).subscribe(input.subscriber);
        input.awaitCompletion();
    }

    @GenerateMicroBenchmark
    public void processWithoutSupplier(UseCaseInput input) throws InterruptedException {
        new FiniteSynchronousLongPublisher(input.size).process(new MapProcessor<Long, String>(l -> "Num: " + l)).subscribe(input.subscriber);
        input.awaitCompletion();
    }

    @GenerateMicroBenchmark
    public void processWithSupplies(UseCaseInput input) throws InterruptedException {
        new FiniteSynchronousLongPublisher(input.size).process(() -> new MapProcessor<Long, String>(l -> "Num: " + l)).subscribe(input.subscriber);
        input.awaitCompletion();
    }

}
