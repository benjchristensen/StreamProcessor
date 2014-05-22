#### Playing with stream processing on top of [Reactive Streams](https://github.com/reactive-streams/reactive-streams).

Examples in [Test.java](https://github.com/benjchristensen/StreamProcessor/blob/master/src/main/java/example/Test.java)

Perf test in [PerfTest.java](https://github.com/benjchristensen/StreamProcessor/blob/master/src/main/java/example/perf/PerfTest.java)



To test performance:

```
./gradlew benchmarks '-Pjmh=-f 1 -tu s -bm thrpt -wi 5 -i 5 -r 5 -prof GC .*PerfTest.*'
```

