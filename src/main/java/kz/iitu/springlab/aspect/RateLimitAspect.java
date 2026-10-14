package kz.iitu.springlab.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
@Order(4)
public class RateLimitAspect {

    private static final long WINDOW_MS = 60_000;

    private final Map<String, Deque<Long>> calls = new ConcurrentHashMap<>();

    @Around("@annotation(rateLimit)")
    public Object limit(
            ProceedingJoinPoint pjp,
            RateLimit rateLimit
    ) throws Throwable {

        String methodName =
                pjp.getSignature().toLongString();

        long now = System.currentTimeMillis();

        Deque<Long> timestamps =
                calls.computeIfAbsent(
                        methodName,
                        key -> new ArrayDeque<>()
                );

        synchronized (timestamps) {

            while (!timestamps.isEmpty()
                    && now - timestamps.peekFirst() >= WINDOW_MS) {
                timestamps.removeFirst();
            }

            if (timestamps.size() >= rateLimit.perMinute()) {
                throw new RateLimitExceededException(
                        "Rate limit exceeded for "
                                + methodName
                                + ". Limit: "
                                + rateLimit.perMinute()
                                + " calls per minute."
                );
            }

            timestamps.addLast(now);

            System.out.println(
                    "[RATE] "
                            + methodName
                            + " calls="
                            + timestamps.size()
                            + "/"
                            + rateLimit.perMinute()
            );
        }

        return pjp.proceed();
    }
}