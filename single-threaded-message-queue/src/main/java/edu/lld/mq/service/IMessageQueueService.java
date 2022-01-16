package edu.lld.mq.service;

import edu.lld.mq.model.Message;

public interface IMessageQueueService<MessageContentType> {
    boolean createTopic(String topicName);

    boolean subscribe(String topicName, SingleThreadedConsumer<MessageContentType> SingleThreadedConsumer);

    boolean unSubscribe(String topicName, SingleThreadedConsumer<MessageContentType> SingleThreadedConsumer);

    boolean publishMsgToTopic(String topicName, IPublisher publisher, Message<MessageContentType> message);
}
