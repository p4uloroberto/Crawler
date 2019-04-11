package com.nosbielc.crawler.central.domain.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Document(collection = "base")
public final class Base {

    @Id
    private final String id;
    private final String origem;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Base)) return false;
        Base base = (Base) o;
        return id.equals(base.id) &&
                origem.equals(base.origem);
    }

    @PersistenceConstructor
    Base(String id, String origem) {
        this.id = id;
        this.origem = origem;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, origem);
    }

    public String getId() {
        return id;
    }

    public String getOrigem() {
        return origem;
    }
}



