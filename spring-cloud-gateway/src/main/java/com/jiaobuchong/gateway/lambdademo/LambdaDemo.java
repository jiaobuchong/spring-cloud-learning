package com.jiaobuchong.gateway.lambdademo;

import java.util.function.Function;

public class LambdaDemo {
    public static void main(String[] args) {
        Function<Integer, Function<Integer, Integer>> func = x -> y -> x + y;
        // 级联表达式
        // 柯里化: 把多个参数的函数转换为只有一个参数的函数
        // 柯里化的目的: 函数标准化，每个函数只有一个参数
        System.out.println(func.apply(3).apply(2));

        // x + y + z
        Function<Integer, Function<Integer, Function<Integer, Integer>>> multiFunc =
                x -> y -> z -> x + y + z;
        System.out.println(multiFunc.apply(2).apply(3).apply(4));

        int[] arr = {2, 3, 4};
        Function f = multiFunc;
        for (int i = 0; i < arr.length; i++) {
            if (multiFunc instanceof Function) {
                Object obj = f.apply(arr[i]);
                if (obj instanceof Function) {
                    f = (Function) obj;
                } else {
                    System.out.println("result: " + obj);
                }
            }
        }

        // 高阶函数：就是返回函数的函数，方法（函数）返回值是函数


    }
}
