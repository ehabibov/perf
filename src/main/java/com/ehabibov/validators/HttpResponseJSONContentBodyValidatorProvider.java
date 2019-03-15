package com.ehabibov.validators;

import com.ehabibov.util.ContentWellnessValidator;
import com.griddynamics.jagger.coordinator.NodeContext;
import com.griddynamics.jagger.engine.e1.collector.ResponseValidator;
import com.griddynamics.jagger.engine.e1.collector.ResponseValidatorProvider;
import com.griddynamics.jagger.invoker.v2.JHttpEndpoint;
import com.griddynamics.jagger.invoker.v2.JHttpQuery;
import com.griddynamics.jagger.invoker.v2.JHttpResponse;

/** Validator checks that response body is a valid JSON  */

public class HttpResponseJSONContentBodyValidatorProvider implements ResponseValidatorProvider {

    private static final String name =  "JSON response content type body validator";

    public static String getName(){
        return name;
    }

    @Override
    public ResponseValidator<JHttpQuery, JHttpEndpoint, JHttpResponse> provide(String sessionId, String taskId, NodeContext kernelContext) {
        return new ResponseValidator<JHttpQuery, JHttpEndpoint, JHttpResponse>(sessionId, taskId, kernelContext) {

            @Override
            public String getName() {
                return name;
            }

            @Override
            public boolean validate(JHttpQuery query, JHttpEndpoint endpoint, JHttpResponse result, long duration) {
                return ContentWellnessValidator.isValidJSON((byte[]) result.getBody());
            }
        };
    }
}
