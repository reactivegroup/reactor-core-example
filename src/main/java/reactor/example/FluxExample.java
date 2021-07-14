package reactor.example;

import reactor.core.publisher.Flux;

public class FluxExample {

    public static void main(String[] args) {
        Flux.just("tom", "jack", "allen")
                .map(s -> s.concat("@qq.com"))
                .subscribe(System.out::println);
    }
}
