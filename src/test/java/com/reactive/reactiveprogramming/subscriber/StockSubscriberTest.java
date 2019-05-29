package com.reactive.reactiveprogramming.subscriber;

import static org.assertj.core.api.Assertions.assertThat;
import static com.jayway.awaitility.Awaitility.await;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import com.reactive.reactiveprogramming.model.StockDTO;
import com.reactive.reactiveprogramming.publisher.StockPublisher;

public class StockSubscriberTest {

  @Test
  public void testSubscriberForValidInput() {

    StockSubscriber stockSubscriber = new StockSubscriber(2);
    List<StockDTO> stocks =
        List.of(
            new StockDTO("Stock1", new BigDecimal(10.98)),
            new StockDTO("Stock2", new BigDecimal(234)));
    StockPublisher stockPublisher = new StockPublisher();
    stockPublisher.subscribe(stockSubscriber);
    stocks.forEach(stockPublisher::submit);
    await()
        .atMost(1000, TimeUnit.MILLISECONDS)
        .until(() -> assertThat(stockSubscriber.consumedMessages.get()).isEqualTo(2));
  }


  @Test
  public void testSubscriberForInValidInput() {

    StockSubscriber stockSubscriber = new StockSubscriber(2);
    List<StockDTO> stocks =
            List.of(
                    new StockDTO("XXX", new BigDecimal(10.98)),
                    new StockDTO("Stock2", new BigDecimal(234)));
    StockPublisher stockPublisher = new StockPublisher();
    stockPublisher.subscribe(stockSubscriber);
    stocks.forEach(stockPublisher::submit);
    await()
            .atMost(1000, TimeUnit.MILLISECONDS)
            .until(() -> assertThat(stockSubscriber.consumedMessages.get()).isEqualTo(0));
  }
}
