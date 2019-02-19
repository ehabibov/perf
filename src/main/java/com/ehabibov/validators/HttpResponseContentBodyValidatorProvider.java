package com.ehabibov.validators;

import com.ehabibov.util.ContentWellnessValidator;
import com.griddynamics.jagger.coordinator.NodeContext;
import com.griddynamics.jagger.engine.e1.collector.ResponseValidator;
import com.griddynamics.jagger.engine.e1.collector.ResponseValidatorProvider;
import com.griddynamics.jagger.invoker.v2.JHttpEndpoint;
import com.griddynamics.jagger.invoker.v2.JHttpQuery;
import com.griddynamics.jagger.invoker.v2.JHttpResponse;

/** Validator checks that response body is a valid JSON or XML dependent on expectation provided in its constructor:
 * Content.JSON or Content.XML
 */
public class HttpResponseContentBodyValidatorProvider implements ResponseValidatorProvider {

    private Content content;

    private HttpResponseContentBodyValidatorProvider(Content content) {
        this.content = content;
    }

    public static HttpResponseContentBodyValidatorProvider of(Content content) {
        return new HttpResponseContentBodyValidatorProvider(content);
    }

    @Override
    public ResponseValidator<JHttpQuery, JHttpEndpoint, JHttpResponse> provide(String sessionId, String taskId, NodeContext kernelContext) {
        return new ResponseValidator<JHttpQuery, JHttpEndpoint, JHttpResponse>(sessionId, taskId, kernelContext) {

            @Override
            public String getName() {
                return String.format("%s response content type body validator", content.toString());
            }

            @Override
            public boolean validate(JHttpQuery query, JHttpEndpoint endpoint, JHttpResponse result, long duration) {
                byte[] response = (byte[]) result.getBody();
                switch (content){
                    case XML:
                        return ContentWellnessValidator.isValidXML(response);
                    case JSON:
                        return ContentWellnessValidator.isValidJSON(response);
                    default:
                        return false;
                }
            }
        };
    }
}
