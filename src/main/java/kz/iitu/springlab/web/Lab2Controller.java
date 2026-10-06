package kz.iitu.springlab.web;

import kz.iitu.springlab.lifecycle.LifecycleBean;
import kz.iitu.springlab.notify.NotificationService;
import kz.iitu.springlab.notify.Notifier;
import kz.iitu.springlab.scope.TicketOffice;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/lab2")
public class Lab2Controller {

    private final NotificationService notifications;
    private final Clock clock;
    private final LifecycleBean lifecycleBean;
    private final TicketOffice ticketOffice;
    private final Notifier repeatingNotifier;

    public Lab2Controller(
            NotificationService notifications,
            Clock clock,
            LifecycleBean lifecycleBean,
            TicketOffice ticketOffice,
            @Qualifier("repeating") Notifier repeatingNotifier) {

        this.notifications = notifications;
        this.clock = clock;
        this.lifecycleBean = lifecycleBean;
        this.ticketOffice = ticketOffice;
        this.repeatingNotifier = repeatingNotifier;
    }

    @GetMapping("/notify")
    public Map<String, Object> notify(
            @RequestParam(defaultValue = "Hello") String text) {

        return Map.of(
                "primary", notifications.viaPrimary(text),
                "console", notifications.viaConsole(text),
                "all", notifications.viaAll(text),
                "beanNames", notifications.names()
        );
    }

    @GetMapping("/time")
    public Instant time() {
        return Instant.now(clock);
    }

    @GetMapping("/lifecycle")
    public String lifecycle() {
        return lifecycleBean.status();
    }

    @GetMapping("/scopes")
    public Map<String, Object> scopes() {
        return ticketOffice.demo();
    }

    @GetMapping("/custom")
    public String custom(
            @RequestParam(defaultValue = "Hello") String text) {

        return repeatingNotifier.send(text);
    }
}