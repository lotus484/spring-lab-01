package kz.iitu.springlab.lifecycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LifecycleBean {

    private static final Logger log =
            LoggerFactory.getLogger(LifecycleBean.class);

    @PostConstruct
    public void init() {
        log.info("LifecycleBean @PostConstruct");
    }

    @PreDestroy
    public void destroy() {
        log.info("LifecycleBean @PreDestroy");
    }

    public String status() {
        return "LifecycleBean is alive";
    }
}