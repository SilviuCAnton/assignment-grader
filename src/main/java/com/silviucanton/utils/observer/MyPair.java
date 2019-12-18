package com.silviucanton.utils.observer;

import com.silviucanton.utils.Pair;

public class MyPair<T, E> implements Pair<T, E> {
    private T first;
    private E second;

    public MyPair(T first, E second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public T getFirst() {
        return first;
    }

    @Override
    public void setFirst(T first) {
        this.first = first;
    }

    @Override
    public E getSecond() {
        return second;
    }

    @Override
    public void setSecond(E second) {
        this.second = second;
    }
}
