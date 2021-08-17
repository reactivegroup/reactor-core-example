package reactor.example;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class MonoFlatMapExample {

    public static void main(String[] args) {
        // 同步执行
        Mono.just("tom")
                .map(s -> s.concat("123"))
                .filter(s -> s.length() > 5)
                .subscribe(System.out::println);

        // 同步阻塞
        Mono.just(new CompletableFuture<String>())
                .map(f -> f.join())
                .map(s -> s.concat("123"))
                .filter(s -> s.length() > 5)
                .subscribe(System.out::println);

        // 在同步执行中开启异步链
        Mono.just(new CompletableFuture<String>())
                .map(f -> {
                    f.whenComplete((v, throwable) -> {
                        Mono.just(v)
                                .map(s -> s.concat("123"))
                                .filter(s -> s.length() > 5)
                                .subscribe(System.out::println);
                    });
                    return null;
                })
                .subscribe();

        Mono.just(new CompletableFuture<String>())
                .map(f -> Mono.fromCompletionStage(f))
                .map(m -> m
                        .map(s -> s.concat("123"))
                        .filter(s -> s.length() > 5)
                        .subscribe(System.out::println))
                .subscribe();

        // 在同步执行中开启异步链 -> 多重异步
        Mono.just(new CompletableFuture<String>())
                .map(f -> Mono.fromCompletionStage(f))
                .map(m1 -> m1
                        .map(s1 ->
                                Mono.just(new CompletableFuture<String>())
                                        .map(f -> Mono.fromCompletionStage(f))
                                        .map(m2 -> m2
                                                .map(s -> s.concat("123"))
                                                .filter(s -> s.length() > 5)
                                                .subscribe(System.out::println))
                                        .subscribe(System.out::println))
                        .subscribe())
                .subscribe();

        // 使用flatmap开启异步链
        Mono.just(new CompletableFuture<String>())
                .flatMap(f -> Mono.fromCompletionStage(f))
                .map(s -> s.concat("123"))
                .filter(s -> s.length() > 5)
                .subscribe(System.out::println);

        Mono.just(new CompletableFuture<String>())
                .flatMap(f -> Mono.fromCompletionStage(f))
                .map(s -> s.concat("123"))
                .filter(s -> s.length() > 5)
                .flatMapMany(s -> Flux.from(Mono.fromCompletionStage(new CompletableFuture<String>())))
                .map(s -> s.concat("123"))
                .filter(s -> s.length() > 5)
                .subscribe(System.out::println);

        /*
         * 1123123
         * 1123123
         * 1123123
         * 2123123
         * 2123123
         * 2123123
         */
        Stream.of("1", "2")
                .map(s -> s.concat("123"))
                .flatMap(s -> Stream.of(s, s, s))
                .map(s -> s.concat("123"))
                .forEach(System.out::println);
    }
}
