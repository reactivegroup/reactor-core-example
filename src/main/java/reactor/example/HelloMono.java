package reactor.example;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.stream.Stream;

public class HelloMono {

    public static void main(String[] args) {

        // Flux.just("tom", "jack", "allen")
        //         .map(s -> s.concat("@qq.com"))
        //         .subscribe(System.out::println);

        // Flux.just("123","23")
        //         .flatMap()
        //         .limitRate(10)
        //         .log();

        // .limit;
        // .map;.filter;
        // .flatMap;

        Mono.just("tom")
                .publishOn(Schedulers.boundedElastic())
                .map(s -> s.concat("123"))
                .filter(s -> s.length() > 5)
                .subscribe(System.out::println);

        Stream.of(1)
                .map(integer -> integer + 1)
                .forEach(System.out::println);
    }
}
