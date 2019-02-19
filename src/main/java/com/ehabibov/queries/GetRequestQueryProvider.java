package com.ehabibov.queries;

import com.griddynamics.jagger.invoker.v2.JHttpQuery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GetRequestQueryProvider implements Iterable {

    @Override
    public Iterator iterator() {
        List<JHttpQuery> queries = new ArrayList<>();
        queries.add(new JHttpQuery()
                .get()
                .path("get"));
        return queries.iterator();
    }
}
