#### Playing with stream processing on top of [Reactive Streams](https://github.com/reactive-streams/reactive-streams).

Examples in [Test.java](https://github.com/benjchristensen/StreamProcessor/blob/master/src/main/java/example/Test.java)

Perf test in [PerfTest.java](https://github.com/benjchristensen/StreamProcessor/blob/master/src/main/java/example/perf/PerfTest.java)



To test performance:

```
./gradlew benchmarks '-Pjmh=-f 1 -tu s -bm thrpt -wi 5 -i 5 -r 5 -prof GC .*PerfTest.*'
```


#### Results: May 22, 2014 - Macbook Pro

```
Benchmark                                        (size)   Mode   Samples         Mean   Mean error    Units
e.p.PerfTest.liftMultiStep                            1  thrpt         5 15471737.553   197644.846    ops/s
e.p.PerfTest.liftMultiStep                         1024  thrpt         5    12279.060      121.447    ops/s
e.p.PerfTest.liftMultiSubscribe                       1  thrpt         5 14447955.163   287840.378    ops/s
e.p.PerfTest.liftMultiSubscribe                    1024  thrpt         5     8378.420      404.662    ops/s
e.p.PerfTest.liftSingleSubscribe                      1  thrpt         5 26781152.737   383087.870    ops/s
e.p.PerfTest.liftSingleSubscribe                   1024  thrpt         5    16862.970      573.027    ops/s
e.p.PerfTest.processWithSupplier                      1  thrpt         5 27625375.587   585380.799    ops/s
e.p.PerfTest.processWithSupplier                   1024  thrpt         5    16968.807      303.570    ops/s
e.p.PerfTest.processWithSupplierMultiSubscribe        1  thrpt         5 15348067.120   127768.007    ops/s
e.p.PerfTest.processWithSupplierMultiSubscribe     1024  thrpt         5     8529.277      108.034    ops/s
e.p.PerfTest.processWithSupplierMultistep             1  thrpt         5 13377367.313   147047.679    ops/s
e.p.PerfTest.processWithSupplierMultistep          1024  thrpt         5    11976.350       73.694    ops/s
e.p.PerfTest.processWithoutSupplier                   1  thrpt         5 28002030.117   203893.966    ops/s
e.p.PerfTest.processWithoutSupplier                1024  thrpt         5    17249.917      251.941    ops/s
```

The interesting lines to look at here are:

```
Benchmark                                        (size)   Mode   Samples         Mean   Mean error    Units
e.p.PerfTest.liftMultiStep                            1  thrpt         5 15471737.553   197644.846    ops/s
e.p.PerfTest.processWithSupplierMultistep             1  thrpt         5 13377367.313   147047.679    ops/s
```

These are a stream doing 3 transformations, with `lift` winning at 15.4m ops/second.

```
e.p.PerfTest.liftSingleSubscribe                      1  thrpt         5 26781152.737   383087.870    ops/s
e.p.PerfTest.processWithSupplier                      1  thrpt         5 27625375.587   585380.799    ops/s
```

These are a stream doing a single transformation with `process` winning at 27.6m ops/second.

The analysis of these results is that:

- [`process`](https://github.com/benjchristensen/StreamProcessor/blob/master/src/main/java/example/APublisher.java#L19) is slightly faster (27.6m vs 26.7m ops/second) than [`lift`](https://github.com/benjchristensen/StreamProcessor/blob/master/src/main/java/example/APublisher.java#L27) when doing a single transformation. 
- `lift` is slightly faster (15.4m vs 13.3m ops/second) than `process` when doing multi-step transformations.

