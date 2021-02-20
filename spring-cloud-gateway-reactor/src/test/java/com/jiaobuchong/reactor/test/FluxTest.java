package com.jiaobuchong.reactor.test;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created on 2021-01-29
 */
public class FluxTest {

    public static Map<String, String> idNameMap = Maps.newHashMap();
    public static Map<String, Integer> idStatMap = Maps.newHashMap();

    static {
        idNameMap.put("103", "Joe");
        idNameMap.put("104", "Bart");
        idNameMap.put("105", "Henry");
        idNameMap.put("106", "Nicole");

        idStatMap.put("103", 232);
        idStatMap.put("104", 234);
        idStatMap.put("105", 235);
        idStatMap.put("106", 236);
    }


    @Test
    public void test1() {
        // 返回 FluxArray 对象
        Flux<String> ids = Flux.fromArray(new String[] {"103", "104", "105", "106"}); // <1>

        Flux<String> combinations =
                ids.flatMap(id -> { // <2>
                    // 对系列中的每一个元素，异步地处理它
                    Mono<String> nameTask = ifhrName(id); // <3>
                    Mono<Integer> statTask = ifhrStat(id); // <4> 异步处理

//                    异步地组合两个值
                    return nameTask.zipWith(statTask, // <5>
                            (name, stat) -> "Name " + name + " has stats " + stat);
                });
        // 元素的到位，被收集到一个 List 中
        Mono<List<String>> result = combinations.collectList(); // <6>

//        在生成流的环节，我们可以继续异步地操作 Flux 流，对其进行组合和订阅（subscribe）。
//        最终我们很可能得到一个 Mono 。由于是测试，我们阻塞住（block()），等待流处理过程结束，
//        然后直接返回集合。
        List<String> results = result.block(); // <7>
        System.out.println(results);

    }

    private Mono<Integer> ifhrStat(String id) {
        return Mono.just(idStatMap.getOrDefault(id, 0));
    }

    private Mono<String> ifhrName(String id) {
        return Mono.just(idNameMap.getOrDefault(id, "xxxxx"));
    }
}
