package edu.lld.mq.model;

import lombok.Builder;
import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

@Data
public class Message <MessageContentType> {
    private MessageContentType messageContent;
}
