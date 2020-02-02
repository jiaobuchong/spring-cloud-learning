package com.study.springcloudhystrix.define;

public interface Command<T> {
    T run();

    T fallback();
}
