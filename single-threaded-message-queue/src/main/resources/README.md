# Problem Statement:
## Message Queuing System

* Functional requirements of this system have been described below.
  * Create your own queue that will hold messages in the form of JSON(Standard library with queue implementation were not allowed).
  * There can be more than one queue at any given point of time.
  * There will be one publisher that can generate messages to a queue.
  * There are multiple subscribers that will listen to queues for messages.
  * Subscribers should not be tightly coupled to the system and can be added or removed at runtime.
  * When a subscriber is added to the system, It registers a callback function which makes an API call to the given end point with the json payload, this callback function will be invoked in case some message arrives.
  * There must be a retry mechanism for handling error cases when some exception occurs in listening/processing a message, that must be retried.


# Solution
## Classes/Entities
* edu.lld.mq.model.Publisher
  1. publisherId

* edu.lld.mq.model.Message
  1. messageId
  2. messageContent

* edu.lld.mq.model.Topic
  1. topicName
  2. List<Message> messagesList
  3. List<Subscriber> subscriberList

* edu.lld.mq.model.Subscriber
  1. subscriberName
  2. topicName

* edu.lld.mq.service.AbstractSubscriberService implements Runnable
  1. void run
  2. abstract consume
* edu.lld.mq.service.DefaultSubscriberServiceImpl extend AbstractSubscriber
  1. Subscriber
  2. consume
  
* edu.lld.mq.service.IMessageQueueService
* edu.lld.mq.service.DefaultMessageQueueServiceImpl
  1. Map<String topicName, Topic topic>

  3. createTopic
  4. subscribeToTopic(String topic)
  5. publishMsgToTopic(String topicName, String msg)
     1. _readAllMsgs


## Flow
