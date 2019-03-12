package com.ehabibov.queries;

import com.griddynamics.jagger.invoker.v2.JHttpQuery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractQueryProvider implements Iterable {

    String endpoint;

    public AbstractQueryProvider(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public Iterator iterator() {
        List<JHttpQuery> queries = new ArrayList<>();
        queries.add(new JHttpQuery()
                .get()
                .path(endpoint));
        return queries.iterator();
    }
}
