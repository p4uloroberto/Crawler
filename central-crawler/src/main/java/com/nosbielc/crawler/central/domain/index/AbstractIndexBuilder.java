package com.nosbielc.crawler.central.domain.index;

public abstract class AbstractIndexBuilder<T> {

    protected final T index;

    protected AbstractIndexBuilder(T index) {
        this.index = index;
    }

    public abstract T create();
}
