package kz.iitu.springlab.notify;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("repeating")
@Order(3)
public class RepeatingNotifier implements Notifier {

    private static final Logger log =
            LoggerFactory.getLogger(RepeatingNotifier.class);

    @Value("${app.repeat-count:3}")
    private int repeatCount;

    @PostConstruct
    public void init() {
        log.info("REPEATING >> initialized");
    }

    @Override
    public String send(String message) {
        return message.repeat(repeatCount);
    }

    @Override
    public String channel() {
        return "repeating";
    }
}