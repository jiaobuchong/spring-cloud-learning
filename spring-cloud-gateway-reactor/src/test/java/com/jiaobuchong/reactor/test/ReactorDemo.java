package com.jiaobuchong.reactor.test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;
import reactor.test.StepVerifier;

/**
 * Created on 2021-02-01
 */
public class ReactorDemo {

    @Test
    public void testStream() {
        List<String> collected = Stream.of("a", "b").collect(Collectors.toList());

        List<Integer> figure = collected.stream().map(s -> {
            Integer i;
            switch (s) {
                case "a":
                    i = 1;
                    break;
                case "b":
                    i = 2;
                    break;
                default:
                    i = -1;
                    break;
            }
            return i;
        }).collect(Collectors.toList());

        List<Integer> a = new ArrayList<>();
        a.add(1);
        a.add(2);
        List<Integer> b = new ArrayList<>();
        b.add(3);
        b.add(4);
        List<Integer> figures = Stream.of(a, b).flatMap(u -> u.stream()).collect(Collectors.toList());
        figures.forEach(f -> System.out.println(f));
    }

    @Test
    public void test1() {
        Flux.just(1, 2, 3, 4, 5, 6).subscribe(System.out::println);
        Mono.just(3).subscribe(System.out::println);

        Flux.just(1, 2, 3, 4, 5, 6).subscribe(
                System.out::println,
                System.err::println,
                () -> System.out.println("Completed!"));
    }

    // map 将一种类型的值转换成另外一种类型的值
    @Test
    public void testMap() {
        StepVerifier.create(Flux.range(1, 6)    // 1
                .map(i -> i * i))   // 2
                .expectNext(1, 4, 9, 16, 25, 36)    //3
                .expectComplete().verify();  // 4
    }


    //    对于每一个字符串s，将其拆分为包含一个字符的字符串流；
    //    对每个元素延迟100ms；
    //    对每个元素进行打印（注doOnNext方法是“偷窥式”的方法，不会消费数据流）；
    //    验证是否发出了8个元素。
    @Test
    public void testFlatMap() {
        StepVerifier.create(
                Flux.just("flux", "mono")
                        // 将每个元素转换/映射为一个流，然后将这些流合并为一个大的数据流，就是多个 Flux<String> 最后是一个 Flux<String>，
                        // 跟 Java flatMap 一样
                        .flatMap(s -> Flux.fromArray(s.split("\\s*"))   // 1
                                .delayElements(Duration.ofMillis(100))) // 2
                        .doOnNext(System.out::println)) // 3
                .expectNextCount(8) // 4
                .verifyComplete();
    }


    @Test
    public void testViaStepVerifier() {
        //
        StepVerifier.create(generateFluxFrom1To6())
                .expectNext(1, 2, 3, 4, 5, 6)
                .expectComplete()
                .verify();
        StepVerifier.create(generateMonoWithError())
                .expectErrorMessage("some error")
                .verify();
    }

    @Test
    public void testSimpleOperators() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);  // 2
        Flux.zip(
                getZipDescFlux(),
                Flux.interval(Duration.ofMillis(200)))  // 3
                .subscribe(t -> System.out.println(t.getT1() + ": " + t.getT2()), null,
                        countDownLatch::countDown);    // 4
        countDownLatch.await(10, TimeUnit.SECONDS);     // 5
    }


    // 资源清理
    @Test
    public void testFinally() {
        LongAdder statsCancel = new LongAdder();    // 1

        Flux<String> flux =
                Flux.just("foo", "bar", "xx")
                        .doFinally(type -> {
                            if (type == SignalType.CANCEL)  // 2
                                statsCancel.increment();  // 3
                        })
                        .take(1);   // 4
        flux.subscribe(System.out::println);
        System.out.println(statsCancel.intValue());
    }

    private Flux<String> getZipDescFlux() {
        String desc =
                "Zip two sources together, that is to say wait for all the sources to emit one element and combine "
                        + "these elements once into a Tuple2.";
        return Flux.fromArray(desc.split("\\s+"));  // 1
    }


    private Flux<Integer> generateFluxFrom1To6() {
        return Flux.just(1, 2, 3, 4, 5, 6);
    }

    private Mono<Integer> generateMonoWithError() {
        return Mono.error(new Exception("some error"));
    }
}
