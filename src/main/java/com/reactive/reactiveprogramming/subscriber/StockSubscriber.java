package com.reactive.reactiveprogramming.subscriber;

import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicInteger;
import org.assertj.core.util.VisibleForTesting;
import com.reactive.reactiveprogramming.model.StockDTO;

public class StockSubscriber implements Flow.Subscriber<StockDTO> {

  @VisibleForTesting protected AtomicInteger consumedMessages = new AtomicInteger();
  private AtomicInteger howMuchMessageToConsume;
  private Flow.Subscription subscription;

  public StockSubscriber(Integer howMuchMessageToConsume) {
    this.howMuchMessageToConsume = new AtomicInteger(howMuchMessageToConsume);
  }

  @Override
  public void onSubscribe(Flow.Subscription subscription) {
    this.subscription = subscription;
    subscription.request(1);
  }

  @Override
  public void onNext(StockDTO item) {
    howMuchMessageToConsume.decrementAndGet();
    if (isFailFast(item)) {
      throw new IllegalArgumentException("Process failed with item: "+item);
    } else {
      consumedMessages.incrementAndGet();
    }
    if (howMuchMessageToConsume.get() > 0) {
      subscription.request(1);
    }
  }

  @Override
  public void onError(Throwable throwable) {
    System.out.println("Error occurred while processing the data");
  }

  @Override
  public void onComplete() {
    System.out.println("Process completed");
  }

  private boolean isFailFast(StockDTO stockDTO) {
      return stockDTO.getName().contains("XXX");
  }
}
