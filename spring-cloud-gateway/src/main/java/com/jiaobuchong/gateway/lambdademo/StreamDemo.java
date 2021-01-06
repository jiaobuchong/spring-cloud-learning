package com.jiaobuchong.gateway.lambdademo;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamDemo {
    public static void main(String[] args) {
//        stream2();
        stream3();
    }

    // 流的迭代
    public static void stream1() {
        int[] nums = {1, 2, 3};
        int sum = 0;
        // 外部迭代
        for (int i : nums) {
            sum += i;
        }
        System.out.println("sum: " + sum);

        // 内部迭代
        // map 是中间操作，返回 stream 的操作
        // sum 是终止操作
        // 惰性求值：没有调用终止操作的话，中间操作不会执行
        int num2 = IntStream.of(nums).map(x -> x * 2).sum();
        System.out.println(num2);
    }

//    流的中间操作
    public static void stream2() {
        String str = "my name is jack";
        // flatMap A -> B 属性（是个集合），最终得到所有 A 元素里面的所有 B 元素集合
        // IntStream LongStream 并不是 Stream 的子类，所以要进行装箱 boxed 然后就变成了 Stream。

        // 这里的 A 属性是一个单词，B 属性是单词里的一个个字符
        Stream.of(str.split(" ")).flatMap(s -> s.chars().boxed())
                .forEach(i -> System.out.println((char) i.intValue()));
    }

//    流的终止操作
    public static void stream3() {
        String str = "my name is jack";
        // 使用并行流
        // 打印出来的字符是乱序的
        str.chars().parallel().forEach(i -> System.out.print((char) i));
        System.out.println();
        System.out.println("-----------------");
        // forEachOrdered 可以保证顺序
        str.chars().parallel().forEachOrdered(i -> System.out.print((char) i));

        List<String> strings = Stream.of(str.split(" ")).collect(Collectors.toList());
        System.out.println(strings);

        // 使用 reduce 拼接字符串
        Optional<String> optionalS = Stream.of(str.split(" ")).reduce((s1, s2) -> s1 + "|" + s2);
        System.out.println(optionalS.orElse(""));

        // 带有默认值的 reduce
        String containDefaultV = Stream.of(str.split(" ")).reduce("", (s1, s2) -> s1 + "|" + s2);
        System.out.println(containDefaultV);

        // 求单词总长度
        Integer length = Stream.of(str.split(" ")).map(String::length)
                .reduce(0, Integer::sum);
        System.out.println(length);

        // 单词最长的
        Optional<String> maxLen = Stream.of(str.split(" ")).max(Comparator.comparingInt(String::length));
        System.out.println(maxLen.get());

        // 使用 findFirst 短路操作
        OptionalInt optionalInt = new Random().ints().findFirst();
        System.out.println(optionalInt.orElse(0));









    }
}
