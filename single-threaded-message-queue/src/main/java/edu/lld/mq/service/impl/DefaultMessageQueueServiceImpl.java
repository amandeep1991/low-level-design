package edu.lld.mq.service.impl;

import edu.lld.mq.model.Message;
import edu.lld.mq.model.Topic;
import edu.lld.mq.service.IMessageQueueService;
import edu.lld.mq.service.IPublisher;
import edu.lld.mq.service.SingleThreadedConsumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DefaultMessageQueueServiceImpl<MessageContentType> implements IMessageQueueService<MessageContentType> {

    private Map<String, Topic<MessageContentType>> topicNameToTopicMap;

    public DefaultMessageQueueServiceImpl() {
        this.topicNameToTopicMap = new HashMap<>();
    }

    @Override
    public boolean createTopic(String topicName) {
        if (topicNameToTopicMap.containsKey(topicName)) {
            throw new IllegalArgumentException(String.format("Provided topicName already present in the system, current topicNames available in the system are: %s", topicNameToTopicMap.keySet()));
        }
        topicNameToTopicMap.put(topicName, new Topic(topicName, new ArrayList<>(), new HashMap<>()));
        return true;
    }

    @Override
    public boolean subscribe(String topicName, SingleThreadedConsumer<MessageContentType> singleThreadedConsumer) {
        if (!topicNameToTopicMap.containsKey(topicName)) {
            throw new IllegalArgumentException(String.format("Provided topicName is not present in the system, current topicNames available in the system are: %s", topicNameToTopicMap.keySet()));
        } else {
            Topic<MessageContentType> messageContentTypeTopic = topicNameToTopicMap.get(topicName);
            messageContentTypeTopic.getSubscribersMap().put(singleThreadedConsumer.getConsumerName(), singleThreadedConsumer);
            return true;
        }
    }

    @Override
    public boolean unSubscribe(String topicName, SingleThreadedConsumer<MessageContentType> SingleThreadedConsumer) {
        if (!topicNameToTopicMap.containsKey(topicName)) {
            throw new IllegalArgumentException(String.format("Provided topicName is not present in the system, current topicNames available in the system are: %s", topicNameToTopicMap.keySet()));
        } else {
            topicNameToTopicMap.get(topicName).getSubscribersMap().remove(SingleThreadedConsumer.getConsumerName());
            return true;
        }
    }

    @Override
    public boolean publishMsgToTopic(String topicName, IPublisher publisher, Message<MessageContentType> message) {
        if (!topicNameToTopicMap.containsKey(topicName)) {
            throw new IllegalArgumentException(String.format("Provided topicName is not present in the system, current topicNames available in the system are: %s", topicNameToTopicMap.keySet()));
        } else {
            Topic<MessageContentType> messageContentTypeTopic = topicNameToTopicMap.get(topicName);
            messageContentTypeTopic.getMessages().add(message);
            messageContentTypeTopic.getSubscribersMap().values().stream().forEach(x -> x.consumeInternal(message));
            return true;
        }
    }
}
