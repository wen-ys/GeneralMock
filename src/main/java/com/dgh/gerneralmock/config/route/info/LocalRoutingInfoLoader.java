package com.dgh.gerneralmock.config.route.info;

import com.dgh.gerneralmock.controller.RoutingHandler;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Component
public class LocalRoutingInfoLoader extends RoutingInfoLoader {

    public LocalRoutingInfoLoader(RoutingHandler routingHandler) {
        super(routingHandler);
    }

    @Override
    protected InputStream getInputStream(String source) {
        try {
            return new FileInputStream(source);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        return InputStream.nullInputStream();
    }
}
