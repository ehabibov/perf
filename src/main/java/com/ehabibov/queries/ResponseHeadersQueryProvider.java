package com.ehabibov.queries;

import com.ehabibov.util.DatasourceReader;
import com.griddynamics.jagger.invoker.v2.JHttpQuery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ResponseHeadersQueryProvider implements Iterable {

    private String datasourcePath;

    public ResponseHeadersQueryProvider(String datasourcePath) {
        this.datasourcePath = datasourcePath;
    }

    @Override
    public Iterator iterator() {
        List<JHttpQuery> queries = new ArrayList<>();
        queries.add(new JHttpQuery()
                .get()
                .path("response-headers")
                .queryParams(DatasourceReader.toMap(datasourcePath))
        );
        return queries.iterator();
    }
}
