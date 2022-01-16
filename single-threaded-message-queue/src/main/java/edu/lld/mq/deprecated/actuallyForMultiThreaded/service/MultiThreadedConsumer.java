package edu.lld.mq.deprecated.actuallyForMultiThreaded.service;

import edu.lld.mq.model.Message;
import edu.lld.mq.model.Topic;
import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.concurrent.atomic.AtomicInteger;

@Data
@Deprecated
public abstract class MultiThreadedConsumer<MessageContentType> extends Thread{

    private Topic<MessageContentType> topic;
    private AtomicInteger offset;
    private String subscriberName;
    private String topicName;

    //@Value("${retry.count:3}") // Can make it config driven | can expose it via actuator as well
    private int RETRY_COUNT=3;

    protected MultiThreadedConsumer(String topicName, String subscriberName) {
        this.topicName = topicName;
        this.subscriberName = subscriberName;
        this.offset = new AtomicInteger(0);
    }

    @Override
    @SneakyThrows
    public void run() {
        while (true) {
            synchronized (this.topic) {
                if (this.topic.getMessages().size() <= this.offset.get()) {
                    this.topic.wait();
                } else {
                    int offset = this.offset.getAndIncrement();
                    for (int i = 0; i < RETRY_COUNT; i++) {
                        if (this.consume(topic.getMessages().get(offset))) {
                            break;
                        }
                    }
                }
            }
        }
    }

    public abstract boolean consume(Message<MessageContentType> message);
}
