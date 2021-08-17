package reactor.example;

import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Learning {@code dapr-java-sdk} reactor process
 *
 * @apiNote moved from {@code dapr#DaprClientHttp}
 */
public class DaprMonoExample {

    public static void main(String[] args) {
        Mono.subscriberContext()
                .flatMap(context -> invokeApi(context))
                .flatMap(s -> Mono.just(s.toUpperCase()))
                .contextWrite(context -> context.put("Test", "TestValue"))
                .subscribe(s -> System.out.println(Thread.currentThread().getName() + s));
    }

    private static Mono<String> invokeApi(Context context) {
        // fromCallable() is needed so the invocation does not happen early, causing a hot mono.
        return Mono.fromCallable(() -> doInvokeApi(context))
                .flatMap(f -> Mono.fromFuture(f));
    }

    private static CompletableFuture<String> doInvokeApi(Context context) {
        CompletableFuture<String> future = new CompletableFuture<>();
        Executors.newScheduledThreadPool(1,
                r -> {
                    Thread thread = new Thread(r);
                    thread.setName("schedule");
                    return thread;
                })
                .schedule(() -> {
                            future.complete(context.getOrDefault("Test", "Default"));
                        },
                        2,
                        TimeUnit.SECONDS);
        future.whenComplete((s, throwable) -> System.out.println(Thread.currentThread().getName() + s));
        return future;
    }
}
