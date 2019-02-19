package com.ehabibov.listeners;

import com.griddynamics.jagger.engine.e1.Provider;
import com.griddynamics.jagger.engine.e1.collector.*;
import com.griddynamics.jagger.engine.e1.collector.invocation.InvocationInfo;
import com.griddynamics.jagger.engine.e1.collector.invocation.InvocationListener;
import com.griddynamics.jagger.engine.e1.services.ServicesAware;
import com.griddynamics.jagger.invoker.InvocationException;
import com.griddynamics.jagger.invoker.v2.JHttpEndpoint;
import com.griddynamics.jagger.invoker.v2.JHttpQuery;
import com.griddynamics.jagger.invoker.v2.JHttpResponse;

/** Listener collects response body size in bytes of every iteration and as summary provides sum size of all
 * received bodies with average size through JLoadTest
 */
public class HttpResponseBodySizeInvocationListener extends ServicesAware implements Provider<InvocationListener> {

    private final String metricName = "response-body-size-metric";

    @Override
    protected void init() {
        this.getMetricService().createMetric((new MetricDescription(metricName))
                .displayName("Response body size (bytes)")
                .showSummary(true)
                .plotData(true)
                .addAggregator(new SumMetricAggregatorProvider())
                .addAggregator(new AvgMetricAggregatorProvider())
        );
    }

    @Override
    public InvocationListener<JHttpQuery, JHttpResponse, JHttpEndpoint> provide() {

        return new InvocationListener<JHttpQuery, JHttpResponse, JHttpEndpoint>() {

            @Override
            public void onStart(InvocationInfo<JHttpQuery, JHttpResponse, JHttpEndpoint> invocationInfo) { }

            @Override
            public void onSuccess(InvocationInfo<JHttpQuery, JHttpResponse, JHttpEndpoint> invocationInfo) {
                if (invocationInfo.getResult() != null) {
                    byte[] responseBody = (byte[]) invocationInfo.getResult().getBody();
                    getMetricService().saveValue(metricName, responseBody.length);
                }
            }

            @Override
            public void onFail(InvocationInfo invocationInfo, InvocationException e) { }

            @Override
            public void onError(InvocationInfo invocationInfo, Throwable error) { }
            };
        }
    }

