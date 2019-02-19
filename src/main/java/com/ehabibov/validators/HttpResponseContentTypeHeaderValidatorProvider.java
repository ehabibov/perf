package com.ehabibov.validators;

import com.griddynamics.jagger.coordinator.NodeContext;
import com.griddynamics.jagger.engine.e1.collector.ResponseValidator;
import com.griddynamics.jagger.engine.e1.collector.ResponseValidatorProvider;
import com.griddynamics.jagger.invoker.v2.JHttpEndpoint;
import com.griddynamics.jagger.invoker.v2.JHttpQuery;
import com.griddynamics.jagger.invoker.v2.JHttpResponse;
import org.springframework.http.MediaType;

/** Validator checks that response's application media type contains expected one which provided in constructor.
 */
public class HttpResponseContentTypeHeaderValidatorProvider implements ResponseValidatorProvider {

    private MediaType mediaType;

    private HttpResponseContentTypeHeaderValidatorProvider(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public static HttpResponseContentTypeHeaderValidatorProvider of(MediaType mediaType) {
        return new HttpResponseContentTypeHeaderValidatorProvider(mediaType);
    }

    @Override
    public ResponseValidator<JHttpQuery, JHttpEndpoint, JHttpResponse> provide(String sessionId, String taskId, NodeContext kernelContext) {
        return new ResponseValidator<JHttpQuery, JHttpEndpoint, JHttpResponse>(sessionId, taskId, kernelContext) {

            @Override
            public String getName() {
                return String.format("%s response content type header validator", mediaType);
            }

            @Override
            public boolean validate(JHttpQuery query, JHttpEndpoint endpoint, JHttpResponse result, long duration) {
                return result.getHeaders().getContentType().includes(mediaType);
            }
        };
    }
}
