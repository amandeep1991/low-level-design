package edu.lld.mq.service;

import edu.lld.mq.model.Message;
import edu.lld.mq.model.Topic;
import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

@Data
public abstract class SingleThreadedConsumer<MessageContentType>{

    private Topic<MessageContentType> topic;
    private String consumerName;
    private String topicName;

    //@Value("${retry.count:3}") // Can make it config driven | can expose it via actuator as well
    private int RETRY_COUNT=3;

    protected SingleThreadedConsumer(String topicName, String consumerName) {
        this.topicName = topicName;
        this.consumerName = consumerName;
    }

    public void consumeInternal(Message<MessageContentType> msg) {
        for (int i = 0; i < RETRY_COUNT; i++) {
            if (this.consume(msg)) {
                break;
            }
        }
    }

    public abstract boolean consume(Message<MessageContentType> message);
}
