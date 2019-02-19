package com.ehabibov.validators;

import com.ehabibov.util.DatasourceReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.griddynamics.jagger.coordinator.NodeContext;
import com.griddynamics.jagger.engine.e1.collector.ResponseValidator;
import com.griddynamics.jagger.engine.e1.collector.ResponseValidatorProvider;
import com.griddynamics.jagger.invoker.v2.JHttpEndpoint;
import com.griddynamics.jagger.invoker.v2.JHttpQuery;
import com.griddynamics.jagger.invoker.v2.JHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

/** Validator checks that response body contains strictly all key-value pairs from datasource file
 */
public class HttpQueryValidatorProvider implements ResponseValidatorProvider {

    private String datasource;

    public HttpQueryValidatorProvider(String datasource) {
        this.datasource = datasource;
    }

    public static HttpQueryValidatorProvider of(String datasource) {
        return new HttpQueryValidatorProvider(datasource);
    }

    @Override
    public ResponseValidator<JHttpQuery, JHttpEndpoint, JHttpResponse> provide(String sessionId, String taskId, NodeContext kernelContext) {
        return new ResponseValidator<JHttpQuery, JHttpEndpoint, JHttpResponse>(sessionId, taskId, kernelContext) {

            @Override
            public String getName() {
                return String.format("Queries validator from file %s: ", datasource);
            }

            @Override
            public boolean validate(JHttpQuery query, JHttpEndpoint endpoint, JHttpResponse result, long duration) {
                Map<String, String> assertMap = DatasourceReader.toMap(datasource);

                ObjectMapper objectMapper = new ObjectMapper();
                byte[] responseBody = (byte[]) result.getBody();
                Map<?,?> responseMap;
                try {
                    responseMap = objectMapper.readValue(new ByteArrayInputStream(responseBody), Map.class);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }

                return responseMap.entrySet().containsAll(assertMap.entrySet());
            }
        };
    }


}
