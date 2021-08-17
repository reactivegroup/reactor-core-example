package reactor.example;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.TimeUnit;

public class FluxExample {

    public static void main(String[] args) {
        Flux.generate(synchronousSink -> synchronousSink.next(""))
                .map(flux -> {
                    System.out.println("generate");
                    return (String) flux;
                })
                .publishOn(Schedulers.newSingle("test"))
                .map(s -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                    }
                    return s.concat("@qq.com");
                })
                .subscribe(System.out::println);
    }
}
