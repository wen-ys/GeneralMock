package com.wenys.gerneralmock.config.route.info;

import com.wenys.gerneralmock.controller.RoutingHandler;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;

public abstract class RoutingInfoLoader {

    private final RoutingHandler routingHandler;

    protected RoutingInfoLoader(RoutingHandler routingHandler) {
        this.routingHandler = routingHandler;
    }

    private void loadFromInputStream(InputStream is) {
        Yaml yaml = new Yaml(new Constructor(RoutingInfo.class));
        RoutingInfo routingInfo = yaml.load(is);
        routingHandler.setRoutingInfo(routingInfo);
    }

    public void load(String value) {
        try (InputStream is = getInputStream(value)) {
            loadFromInputStream(is);
        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(String.format("'%s' : Config file not found", value));
        }
    }

    protected abstract InputStream getInputStream(String source);
}
