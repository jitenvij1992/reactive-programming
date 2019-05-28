package com.reactive.reactiveprogramming;

import static org.assertj.core.api.Assertions.assertThat;
import static com.jayway.awaitility.Awaitility.await;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.junit.Test;

public class ColdProducerTest {

    @Test
    public void testColdProducer() {

        AtomicInteger atomicInteger = new AtomicInteger();
        SubmissionPublisher<Integer> hotPublisher = new SubmissionPublisher<>();
        Flow.Subscriber<Integer> subscriber = new Flow.Subscriber<Integer>() {
            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                System.out.println("Called onSubscribe");
                subscription.request(1);
            }

            @Override
            public void onNext(Integer item) {
                System.out.println("Next called: "+ item);
                atomicInteger.incrementAndGet();
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error happened");
            }

            @Override
            public void onComplete() {
                System.out.println("Completed");
            }
        };


        hotPublisher.subscribe(subscriber);

        Stream<Integer> items = Stream.of(1,2,3,4,5,6,78,9);
        //List<String> items = List.of("Hello World 1", "Hello World 2");
        items.forEach(hotPublisher::submit);
        hotPublisher.close();

        await()
                .atMost(20000, TimeUnit.MILLISECONDS)
                .until(() -> assertThat(atomicInteger.get()).isEqualTo(1));
    }

}
