package com.reactive.reactiveprogramming;

import static org.assertj.core.api.Assertions.assertThat;
import static com.jayway.awaitility.Awaitility.await;

import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

public class ReactiveTestProducerConsumer {

  @Test
  public void testReactiveFlowOfProducerAndConsumers() {

    AtomicInteger atomicInteger = new AtomicInteger();
    SubmissionPublisher<String> publisher = new SubmissionPublisher<>();
    Flow.Subscriber<String> subscriber =
        new Flow.Subscriber<String>() {
          @Override
          public void onSubscribe(Flow.Subscription subscription) {
            System.out.println("Called onSubscribe");
            subscription.request(Integer.MAX_VALUE);
          }

          @Override
          public void onNext(String item) {
            System.out.println("Next called");
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

    publisher.subscribe(subscriber);

    List<String> items = List.of("Hello World 1", "Hello World 2");
    items.forEach(publisher::submit);
    publisher.close();

    await()
        .atMost(20000, TimeUnit.MILLISECONDS)
        .until(() -> assertThat(atomicInteger.get()).isEqualTo(2));
  }
}
