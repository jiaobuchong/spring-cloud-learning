package com.jiaobuchong.webflux;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Slf4j
@RestController
public class TestController {

    @GetMapping("/test1")
    public String test1() {
        log.info("test1 start");
        String str = createStr("xxxx");
        log.info("test1 end");
        return str;
    }

    @GetMapping("/test2")
    public Mono<String> test2() {
        log.info("tes2 start");
        Mono<String> str = Mono.fromSupplier(() -> createStr("webflux"));
        log.info("test2 end");
        return str;
    }


    /**
     * flux 返回0-n个元素
     * @return
     */
//    @GetMapping(value = "/test3", produces = "text/event-stream")
    @GetMapping(value = "/test3", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> flux() {
        Flux<String> res = Flux.fromStream(IntStream.range(1, 5).mapToObj(i -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "flux data--" + i;
        }));
        return res;
    }

    private String createStr(String str) {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if ("webflux".equals(str)) {
            return "hello, webflux";
        } else {
            return "hello, spring mvc";
        }
    }
}
