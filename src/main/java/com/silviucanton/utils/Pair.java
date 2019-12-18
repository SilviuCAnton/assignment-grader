package com.silviucanton.utils;

import java.io.Serializable;

/**
 * Pair utility class
 *
 * @param <T> - type of the first item of the pair
 * @param <E> - type of the second item of the pair
 */
public interface Pair<T, E> extends Serializable {

    /**
     * returns the first element of the pair
     *
     * @return first - T
     */
    public T getFirst();

    /**
     * sets the first element of the pair
     *
     * @param first - T
     */
    public void setFirst(T first);

    /**
     * returns the second element of the pair
     *
     * @return second - E
     */
    public E getSecond();

    /**
     * sets the second element of the pair
     *
     * @param second - E
     */
    public void setSecond(E second);
}

