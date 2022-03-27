package com.wenys.gerneralmock.config.route.info;

import com.wenys.gerneralmock.controller.RoutingHandler;
import com.wenys.gerneralmock.log.LoggingHandler;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Component
public class LocalRoutingInfoLoader extends RoutingInfoLoader {

    @Value("${initRoutingSource:}")
    private String initRoutingSource;

    public LocalRoutingInfoLoader(RoutingHandler routingHandler) {
        super(routingHandler);
    }

    @PostConstruct
    private void initLoading() {
        LoggingHandler.log("loading : " + initRoutingSource);
        if (!StringUtil.isNullOrEmpty(initRoutingSource)) {
            load(initRoutingSource);
        }
    }

    @Override
    protected InputStream getInputStream(String source) {
        try {
            return new FileInputStream(source);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return InputStream.nullInputStream();
    }
}
