package reactor.example;

import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

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
                .subscribe(System.out::println);
    }

    private static Mono<String> invokeApi(Context context) {
        // fromCallable() is needed so the invocation does not happen early, causing a hot mono.
        return Mono.fromCallable(() -> doInvokeApi(context))
                .flatMap(f -> Mono.fromFuture(f));
    }

    private static CompletableFuture<String> doInvokeApi(Context context) {
        context.put("Test", "TestValue");

        CompletableFuture<String> future = new CompletableFuture<>();
        future.whenCompleteAsync((s, throwable) -> context.readOnly().getOrDefault("Test", "TestDefault"),
                Executors.newSingleThreadExecutor());
        return future;
    }
}
