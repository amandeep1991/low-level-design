package edu.lld.mq.deprecated.actuallyForMultiThreaded.service.impl;

import edu.lld.mq.deprecated.actuallyForMultiThreaded.service.MultiThreadedConsumer;
import edu.lld.mq.model.Message;

@Deprecated
public class DefaultMultiThreadedConsumerImpl<MessageContentType> extends MultiThreadedConsumer<MessageContentType> {

    protected DefaultMultiThreadedConsumerImpl(String topicName, String subscriberName) {
        super(topicName, subscriberName);
    }

    @Override
    public boolean consume(Message<MessageContentType> message) {
        System.out.println(message.getMessageContent());

        if (message.getMessageContent().equals("RETRY_ME")) {
            return false;
        }
        return true;
    }

}
