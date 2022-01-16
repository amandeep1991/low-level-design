package edu.lld.mq.service.impl;

import edu.lld.mq.model.Message;
import edu.lld.mq.service.SingleThreadedConsumer;

public class DefaultSingleThreadedConsumerImpl<MessageContentType> extends SingleThreadedConsumer<MessageContentType> {

    protected DefaultSingleThreadedConsumerImpl(String topicName, String subscriberName) {
        super(topicName, subscriberName);
    }

    @Override
    public boolean consume(Message<MessageContentType> message) {
        System.out.println(String.format("%s :: %s", this.getConsumerName(), message.getMessageContent()));

        if (message.getMessageContent().equals("RETRY_ME")) {
            return false;
        }
        return true;
    }

}
