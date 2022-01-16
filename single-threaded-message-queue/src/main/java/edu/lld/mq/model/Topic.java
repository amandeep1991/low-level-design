package edu.lld.mq.model;

import edu.lld.mq.service.SingleThreadedConsumer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
public class Topic<MessageContentType> {
    private String topicName;
    private List<Message<MessageContentType>> messages;
    private HashMap<String, SingleThreadedConsumer<MessageContentType>> subscribersMap;
}
