package edu.lld.mq.service.impl;

import com.google.gson.Gson;
import edu.lld.mq.model.Message;
import lombok.Builder;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class DefaultMessageQueueServiceImplTest {

    public static final String ABC_QUEUE = "ABC_QUEUE";
    @InjectMocks
    private DefaultMessageQueueServiceImpl<String> service;

    @Test
    public void testCreationOfNewQueue() {
        Assert.assertTrue(service.createTopic(ABC_QUEUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreationOfExistingQueue() {
        service.createTopic(ABC_QUEUE);
        service.createTopic(ABC_QUEUE);
    }

    @Test
    public void testCreationOfExistingQueues() {
        service.createTopic("ABC_QUEUE1");
        service.createTopic("ABC_QUEUE2");
        try {
            service.createTopic("ABC_QUEUE1");
        } catch (IllegalArgumentException exception) {
            Assert.assertEquals("Provided topicName already present in the system, current topicNames available in the system are: [ABC_QUEUE1, ABC_QUEUE2]", exception.getMessage());
        }
    }

    @Test
    public void testPubSubWithNoRetries() {
        service.createTopic(ABC_QUEUE);
        DefaultSingleThreadedConsumerImpl<String> consumer_1 = new DefaultSingleThreadedConsumerImpl<>(ABC_QUEUE, "CONSUMER_1");
        DefaultSingleThreadedConsumerImpl<String> consumer_2 = new DefaultSingleThreadedConsumerImpl<>(ABC_QUEUE, "CONSUMER_2");
        service.subscribe(ABC_QUEUE, consumer_1);
        service.subscribe(ABC_QUEUE, consumer_2);
        Message<String> message = new Message<>();
        message.setMessageContent("123");
        service.publishMsgToTopic(ABC_QUEUE, null, message);
        // TODO: On Aman :: Assertions needs to be cool so didn't add for now, please check console
    }

    @Test
    public void testPubSubWithRetries() {
        service.createTopic(ABC_QUEUE);
        DefaultSingleThreadedConsumerImpl<String> consumer_1 = new DefaultSingleThreadedConsumerImpl<>(ABC_QUEUE, "CONSUMER_1");
        service.subscribe(ABC_QUEUE, consumer_1);
        Message<String> message = new Message<>();
        message.setMessageContent("RETRY_ME");
        service.publishMsgToTopic(ABC_QUEUE, null, message);
        // TODO: On Aman :: Assertions needs to be cool so didn't add for now, please check console
    }

    @Test
    public void testPubSubForJsonTypeOfMessage() {
        service.createTopic(ABC_QUEUE);
        DefaultSingleThreadedConsumerImpl<String> consumer_1 = new DefaultSingleThreadedConsumerImpl<>(ABC_QUEUE, "CONSUMER_1");
        service.subscribe(ABC_QUEUE, consumer_1);
        Message<String> message = new Message<>();
        message.setMessageContent(new Gson().toJson(TestObject.builder().integerField(1).stringField("1").build()));
        service.publishMsgToTopic(ABC_QUEUE, null, message);
        // TODO: On Aman :: Assertions needs to be cool so didn't add for now, please check console
    }

    @Test
    public void testPubSubForJsonTypeOfMessageAndUnsubscribeAsWell() {
        service.createTopic(ABC_QUEUE);
        DefaultSingleThreadedConsumerImpl<String> consumer_1 = new DefaultSingleThreadedConsumerImpl<>(ABC_QUEUE, "CONSUMER_1");
        service.subscribe(ABC_QUEUE, consumer_1);
        DefaultSingleThreadedConsumerImpl<String> consumer_2 = new DefaultSingleThreadedConsumerImpl<>(ABC_QUEUE, "CONSUMER_2");
        service.subscribe(ABC_QUEUE, consumer_2);
        Message<String> message = new Message<>();
        message.setMessageContent(new Gson().toJson(TestObject.builder().integerField(1).stringField("1").build()));
        service.publishMsgToTopic(ABC_QUEUE, null, message);
        service.unSubscribe(ABC_QUEUE, consumer_1);
        message.setMessageContent(new Gson().toJson(TestObject.builder().integerField(2).stringField("2").build()));
        service.publishMsgToTopic(ABC_QUEUE, null, message);
        // TODO: On Aman :: Assertions needs to be cool so didn't add for now, please check console

    }

}



@Data
@Builder
class TestObject{
    int integerField;
    String stringField;
}