package reactor.example;

import java.util.stream.Stream;

public class StreamExample {

    public static void main(String[] args) {
        Stream.of(1)
                .map(integer -> integer + 1)
                .forEach(System.out::println);
    }
}
