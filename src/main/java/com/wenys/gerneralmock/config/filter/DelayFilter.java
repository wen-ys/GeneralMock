package com.wenys.gerneralmock.config.filter;

import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;
import java.util.Random;

public class DelayFilter extends Filter {

    private final int delayTimeInMilliSec;
    private final int deltaTimeInMilliSec;
    private final int delayPercent;
    private final Random random = new Random();

    DelayFilter(String args) {
        Map<String, String> argMap = parseArgs(args);
        delayPercent = Integer.parseUnsignedInt(argMap.getOrDefault("delayPercent", "100"));
        delayTimeInMilliSec = Integer.parseUnsignedInt(argMap.get("delayTime"));
        deltaTimeInMilliSec = Integer.parseUnsignedInt(argMap.getOrDefault("deltaTime", "1"));
    }

    @Override
    public <T> Mono<T> doFilter(Mono<T> response) {
        int delayTime = 0;
        if(random.nextInt(100) < delayPercent) {
            delayTime = delayTimeInMilliSec + random.nextInt(deltaTimeInMilliSec);
        }
        return response.delayElement(Duration.ofMillis(delayTime));
    }
}
