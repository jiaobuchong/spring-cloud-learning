package com.jiaobuchong.webflux.controller;

import com.jiaobuchong.webflux.domain.User;
import com.jiaobuchong.webflux.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserRepository userRepository;

    // 这种比起 Autowired 会和 spring 耦合度低一些
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 以数组形式一次性返回数据
     *
     * @return
     */
    @GetMapping("/list")
    public Flux<User> listUser() {
        return userRepository.findAll();
    }

    /**
     * 以 sse 形式多次返回数据
     *
     * @return
     */
    @GetMapping(value = "/list/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> listUserStream() {
        return userRepository.findAll();
    }

    @PostMapping("/create")
    public Mono<User> createUser(@RequestBody User user) {
        // spring data jpa 里面，新增和修改都是save，有 id 是修改，id 为空是新增
        // 根据实际情况是否置空id
        user.setId(null);
        return userRepository.save(user);
    }

    /**
     * 根据id删除用户，存在返回200，不存在返回404
     *
     * @param id
     * @return
     */
    @PostMapping("/delete/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
//        Mono<Void> Void 的意思就是没有返回值的
        return userRepository.findById(id)
                // 当你要操作数据，并返回一个 Mono，这个时候用 flatMap
                // 当不操作数据，只是转换数据，使用 map
                .flatMap(user -> this.userRepository.delete(user)
                        .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));


    }

    // 修改成功返回 User 对象
    // 存在的时候返回200，不存在的时候返回404
    @PostMapping("/update/{id}")
    public Mono<ResponseEntity<User>> update(@PathVariable String id, @RequestBody User user) {
        return userRepository.findById(id)
                // flatMap 是对数据进行操作的，返回一个Mono，
                .flatMap(u -> {
                    u.setAge(user.getAge());
                    u.setName(user.getName());
                    return userRepository.save(u);
                })
                // 对上一步返回的 Mono 数据进行转换
                .map(u -> new ResponseEntity<>(u, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/get/{id}")
    public Mono<ResponseEntity<User>> getUserById(@PathVariable String id) {
        return userRepository.findById(id)
                // 对上一步返回的 Mono 数据进行转换
                .map(u -> new ResponseEntity<>(u, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/age/{start}/{end}")
    public Flux<User> findByAge(@PathVariable String start, @PathVariable String end) {

    }

}
