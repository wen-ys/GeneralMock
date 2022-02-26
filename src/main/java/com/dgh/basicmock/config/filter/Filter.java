package com.dgh.basicmock.config.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Setter
@Getter
@NoArgsConstructor
public class Filter {

    private String type;
    private String args;
    private Filter delegate;

    public void init() {
        delegate = FilterFactory.create(type, args);
    }

    protected Map<String, String> parseArgs(String args) {

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = new HashMap<>();
        try {
            map = mapper.readValue(args, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return map;
    }

    public <T> Mono<T> doFilter(Mono<T> response) {
        return delegate.doFilter((response));
    }
}
