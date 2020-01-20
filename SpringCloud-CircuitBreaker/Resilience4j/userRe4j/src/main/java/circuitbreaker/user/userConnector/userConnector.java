package circuitbreaker.user.userConnector;

import circuitbreaker.item.model.item;
import circuitbreaker.user.model.user;
import com.sun.deploy.net.HttpResponse;
import io.github.resilience4j.bulkhead.*;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.core.SupplierUtils;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.vavr.CheckedFunction0;
import io.vavr.CheckedFunction1;
import io.vavr.CheckedRunnable;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.cache.Cache;
import javax.cache.Caching;
import java.io.IOException;
import java.net.ConnectException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Service
public class userConnector {

    @Autowired
    RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public List<user> getItem(String name) {

        /*
         *  Circuit Breaker
         */
        // NOTE step#1 basic
        // CircuitBreakerRegistry를 만든다.
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults();
        // CircuitBreakerRegistry를 CircuitBreaker에 적용
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("lamda");
        // CircuitBreaker를 method에 decorate
        Supplier<List<user>> decoratedSupplier = CircuitBreaker.decorateSupplier(circuitBreaker, () -> getItemBody(name));
        // decoratedSupplier의 실행을 Try, 오류가 났다면 recover
        return Try.ofSupplier(decoratedSupplier).recover(throwable -> {
            return getFallBack(name);
        }).get();

        // NOTE step#2 custom
//        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
//                // 실패율을 백분율로 계산, 값보다 크거나 같으면 CircuitBreaker 상태가 OPEN
//                .failureRateThreshold(50)
//                // slowCallDurationThreshold의 값만큼 느리다면 실패로 간주한다.
//                .slowCallDurationThreshold(Duration.ofSeconds(60))
//                // 호출이 느려 실패한 요청의 백분율이 해당 값보다 크거나 같다면 CircuitBreaker 상태가 OPEN
//                .slowCallRateThreshold(100)
//                // HALF_OPEN 상태에서 허용되는 요청의 수를 설정
//                .permittedNumberOfCallsInHalfOpenState(10)
//                // COUNT_BASES와 TIME_BASED, 각각 OPEN 상태에서 요청 건수와 요청의 시간을 기록한다.
//                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
//                // 요청을 기록하는 slidingWindow의 size를 설정
//                .slidingWindowSize(100)
//                // 실패율을 계산하기 전에 필요한 최소 요청 수
//                .minimumNumberOfCalls(10)
//                // OPEN 상태에서 자동으로 HALF_OPEN으로 전환될 것인가.
//                .automaticTransitionFromOpenToHalfOpenEnabled(false)
//                // 설정한 값에 따라서 OPEN 상태에서 HALF_OPEN으로 변경
//                .waitDurationInOpenState(Duration.ofSeconds(60))
//                // 실패로 기록하거나 기록하지 않을 오류 설정
//                .recordExceptions(ConnectException.class, IOException.class)
//                .ignoreExceptions(IndexOutOfBoundsException.class)
//                .build();
//        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);
//        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("config");
//        CheckedFunction0<List<user>> checkedSupplier = CircuitBreaker.decorateCheckedSupplier(circuitBreaker, () -> {
//            return getItemBody(name);
//        });
//        return Try.of(checkedSupplier).recover(throwable -> getFallBack(name)).get();

        // NOTE step#3 실패를 records하기 전에 recover를 원할때
//        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults();
//        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("test");
//        Supplier<List<user>> supplier = () -> {
//            return getItemBody(name);
//        };
//        Supplier<List<user>> supplierWithRecovery = SupplierUtils.recover(supplier, (exception) -> getFallBack(name));
//        return circuitBreaker.executeSupplier(supplierWithRecovery);

        // NOTE step#4 SupplierUtills나 CallableUtils은 andThen ( chain functions ) 포함하고 있다.
//        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults();
//        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("test");
//        Supplier<List<user>> supplier = () -> {
//            return getItemBody(name);
//        };
//        Supplier<List<user>> supplierWithResultAndExceptionHandler = SupplierUtils
//                .andThen(supplier, (result, exception) -> {
//                    System.out.println(result);
//                    if (result == null) {
//                        return getFallBack(name);
//                    } else {
//                        return result;
//                    }
//                });
//        return circuitBreaker.executeSupplier(supplierWithResultAndExceptionHandler);

        // NOTE etc...
        // circuitBreaker를 처음 상태로
//        circuitBreaker.reset();
        // 상태 수동 조작
//        circuitBreaker.transitionToDisabledState();  // 요청이 실패해도 계속 CLOSE
//        circuitBreaker.transitionToClosedState(); // CLOSED 상태로 전환
//        circuitBreaker.transitionToForcedOpenState(); // 요청이 성공하여도 계속 OPEN

        /*
         * Retry
         */

        // NOTE step#1 basic
//        CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("backendA");
//        RetryRegistry retryRegistry = RetryRegistry.ofDefaults();
//        Retry retryWithDefault = retryRegistry.retry("backendA");
//        Supplier<List<user>> decorateSupplier = CircuitBreaker.decorateSupplier(circuitBreaker, () -> getItemBody(name));
//        decorateSupplier = Retry.decorateSupplier(retryWithDefault, decorateSupplier);
//        return Try.ofSupplier(decorateSupplier)
//                .recover(throwable -> {
//                    return getFallBack(name);
//                }).get();

        // NOTE step#2 custom
//        CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("backendB");
//        RetryConfig retryConfig = RetryConfig.custom()
//                .maxAttempts(3)
//                .waitDuration(Duration.ofMillis(500))
//                .intervalFunction(IntervalFunction.ofDefaults())
//                .retryOnResult(response -> {
//                    if (response.equals(null)) {
//                        return true;
//                    } else {
//                        return false;
//                    }
//                })
//                .retryOnException(e -> e instanceof ConnectException)
//                .ignoreExceptions(IndexOutOfBoundsException.class)
//                .build();
//        RetryRegistry retryRegistry = RetryRegistry.of(retryConfig);
//        Retry retryWithCustom = retryRegistry.retry("backendB");
//        Supplier<List<user>> decorateSupplier = CircuitBreaker.decorateSupplier(circuitBreaker, () -> getItemBody(name));
//        decorateSupplier = Retry.decorateSupplier(retryWithCustom, decorateSupplier);
//        return Try.ofSupplier(decorateSupplier)
//                .recover(throwable -> {
//                    return getFallBack(name);
//                }).get();

        // NOTE step#3 advance decorate
//        Retry retry = Retry.ofDefaults("backendC");
//        CheckedFunction0<List<user>> retryableSupplier = Retry.decorateCheckedSupplier(retry, () -> getItemBody(name));
//        return Try.of(retryableSupplier).recover(throwable -> {
//            return getFallBack(name);
//        }).get();

        // NOTE step#4 RegistryEvents
//        Logger logger = LoggerFactory.getLogger(userConnector.class);
//        RetryRegistry retryRegistry = RetryRegistry.ofDefaults();
//        retryRegistry.getEventPublisher()
//                .onEntryAdded(entryAddedEvent -> {
//                    Retry addedRetry = entryAddedEvent.getAddedEntry();
//                    logger.info("Retry {} added " + addedRetry.getName());
//                })
//                .onEntryRemoved(entryRemovedEvent -> {
//                    Retry removedRetry = entryRemovedEvent.getRemovedEntry();
//                    logger.info("Retry {} removed" + removedRetry.getName());
//                });
//        Retry retry = retryRegistry.retry("backendD");
//        CheckedFunction0<List<user>> retryableSupplier = Retry.decorateCheckedSupplier(retry, () -> getItemBody(name));
//        return Try.of(retryableSupplier).recover(throwable -> {
//            return getFallBack(name);
//        }).get();

        /*
         * RateLimiter
         */

        // NOTE step#1 basic
//        RateLimiterConfig rateLimiterConfig = RateLimiterConfig.custom()
//                // 스레드가 권한을 기다리는 대기 시간
//                .timeoutDuration(Duration.ofSeconds(5))
//                // 각 새로고침의 시간 제한
//                .limitRefreshPeriod(Duration.ofNanos(500))
//                // 한 번의 새로 고침 기간 동안 사용 가능한 permissions
//                .limitForPeriod(50)
//                .build();
//        RateLimiterRegistry rateLimiterRegistry = RateLimiterRegistry.of(rateLimiterConfig);
//        RateLimiter rateLimiterWithDefaultConfig = rateLimiterRegistry.rateLimiter("limiterA");
//        Supplier<List<user>> restrictedSupplier = RateLimiter.decorateSupplier(rateLimiterWithDefaultConfig, () -> getItemBody(name));
//
//        Try<List<user>> firstTry = Try.ofSupplier(restrictedSupplier);
//        Try<List<user>> secondTry = Try.ofSupplier(restrictedSupplier);
//
//        System.out.println(firstTry.isSuccess());
//        System.out.println(secondTry.isSuccess());
//
//        return Try.ofSupplier(restrictedSupplier).recover(throwable -> {
//            return getFallBack(name);
//        }).get();

        // NOTE step#2 advanced decorate
//        RateLimiter rateLimiter = RateLimiter.ofDefaults("limiterB");
//        CheckedRunnable restrictedCall = RateLimiter.decorateCheckedRunnable(rateLimiter, () -> getItemBody(name));
//        Try.run(restrictedCall)
//                .andThenTry(restrictedCall)
//                .onFailure((throwable) -> {
//                    System.out.println("try again");
//                });

        /*
         *  Bulkhead
         */

        // NOTE step#1 semaphoreBulkhead basic
//        BulkheadConfig bulkheadConfig = BulkheadConfig.custom()
//                // 벌크 헤드가 허용하는 최대 병렬 실행 값
//                .maxConcurrentCalls(25)
//                // 포화된 격벽에 들어가려고 할 때 스레드를 차단해야하는 최대 시간 값
//                .maxWaitDuration(Duration.ZERO)
//                .build();
//        BulkheadRegistry bulkheadRegistry = BulkheadRegistry.of(bulkheadConfig);
//        Bulkhead bulkheadWithDefaultConfig = bulkheadRegistry.bulkhead("bulkheadA");

        // NOTE step#2 threadPoolBulkhead basic
//        ThreadPoolBulkheadConfig threadPoolBulkheadConfig = ThreadPoolBulkheadConfig.custom()
//                // 최대 스레드 풀 크기
//                .maxThreadPoolSize(10)
//                // 코어 스레드 풀 크기
//                .coreThreadPoolSize(10)
//                // 큐 용량
//                .queueCapacity(20)
//                // 스레드 수가 코어보다 크면 초과된 스레드가 새로운 task를 기다리는 최대 시간
//                .keepAliveDuration(Duration.ofMillis(20))
//                .build();
//        ThreadPoolBulkheadRegistry threadPoolBulkheadRegistry = ThreadPoolBulkheadRegistry.of(threadPoolBulkheadConfig);
//        ThreadPoolBulkhead bulkheadWithDefaultConfig = threadPoolBulkheadRegistry.bulkhead("threadA");

        // NOTE step#3 advanced decorate
//        Bulkhead bulkhead = Bulkhead.ofDefaults("bulkheadB");
//        CheckedFunction0<List<user>> decorateSupplier = Bulkhead.decorateCheckedSupplier(bulkhead, () -> getItemBody(name));
//        return Try.of(decorateSupplier)
//                .map(value -> {
//                    System.out.println(value);
//                    return value;
//                }).recover(throwable -> {
//                    return getFallBack(name);
//                }).get();
    }

    private List<user> getItemBody(String name) {
        List<user> usersList = new ArrayList<user>();
        List<item> itemList = (List<item>) restTemplate.exchange("http://localhost:8082/users/" + name + "/items"
                , HttpMethod.GET, null
                , new ParameterizedTypeReference<List<item>>() {
                }).getBody();
        usersList.add(new user(name, "getItemBody@mygoogle.com", itemList));
        return usersList;
    }

    @SuppressWarnings("unused")
    public List<user> getFallBack(String name) {
        List<user> usersList = new ArrayList<user>();
        usersList.add(new user(name, "getFallBack@mygoogle.com"));
        return usersList;
    }
}
