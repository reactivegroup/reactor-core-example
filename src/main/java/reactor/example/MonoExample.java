package reactor.example;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class MonoExample {

    public static void main(String[] args) {
        Mono.just("tom")
                .publishOn(Schedulers.boundedElastic())
                .map(s -> s.concat("123"))
                .filter(s -> s.length() > 5)
                .subscribe(System.out::println);
    }
}
