Playing with stream processing on top of [Reactive Streams](https://github.com/reactive-streams/reactive-streams).



To test performance:

```
./gradlew benchmarks '-Pjmh=-f 1 -tu s -bm thrpt -wi 5 -i 5 -r 5 -prof GC .*PerfTest.*'
```

