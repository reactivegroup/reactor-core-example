package reactor.example;

import reactor.core.publisher.Mono;

public class HelloMono {

    public static void main(String[] args) {
        Mono
                .just("hello")
                .subscribe(System.out::println);
    }
}
