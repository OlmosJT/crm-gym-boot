package com.epam.crmgymboot.actuator;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;
import io.micrometer.core.instrument.Counter;

/**
 ** <a href="http://localhost:8080/actuator/metrics/application.get.requests">...</a>
 **/
@Component
public class RequestsCounterMetrics {
    private final Counter getRequestsCounter;
    private final Counter postRequestsCounter;


    public RequestsCounterMetrics(MeterRegistry registry) {
        this.getRequestsCounter = Counter.builder("application.get.requests")
                .description("Counter for GET requests")
                .register(registry);

        this.postRequestsCounter = Counter.builder("application.post.requests")
                .description("Counter for POST requests")
                .register(registry);
    }

    public void incrementGetRequests() {
        getRequestsCounter.increment();
    }

    public void incrementPostRequests() {
        postRequestsCounter.increment();
    }
}
