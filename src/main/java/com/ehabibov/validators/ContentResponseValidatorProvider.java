package com.ehabibov.validators;

import com.griddynamics.jagger.coordinator.NodeContext;
import com.griddynamics.jagger.engine.e1.collector.ResponseValidator;
import com.griddynamics.jagger.engine.e1.collector.ResponseValidatorProvider;
import com.griddynamics.jagger.invoker.v2.JHttpEndpoint;
import com.griddynamics.jagger.invoker.v2.JHttpQuery;
import com.griddynamics.jagger.invoker.v2.JHttpResponse;

/** Validator checks that response body contains provided string. */

public class ContentResponseValidatorProvider implements ResponseValidatorProvider {

    private static final String name =  "String value response validator";
    private final String stringValue;

    private ContentResponseValidatorProvider(String stringValue) {
        this.stringValue = stringValue;
    }

    public static ContentResponseValidatorProvider of(String stringValue) {
        return new ContentResponseValidatorProvider(stringValue);
    }

    public static String getName(){
        return name;
    }

    public ResponseValidator<JHttpQuery, JHttpEndpoint, JHttpResponse> provide(String sessionId, String taskId, NodeContext kernelContext) {
        return new ResponseValidator<JHttpQuery, JHttpEndpoint, JHttpResponse>(taskId, sessionId, kernelContext) {

            public String getName() {
                return name;
            }

            public boolean validate(JHttpQuery query, JHttpEndpoint endpoint, JHttpResponse result, long duration) {
                return new String((byte[]) result.getBody()).contains(stringValue);
            }
        };
    }
}