package com.clcm.storm.tester;

import com.clcm.storm.tester.adapters.InFixtureMessageListener;
import com.clcm.storm.tester.adapters.InLiabilityMessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Created by agrawald on 8/02/16.
 */
@SpringBootApplication
public class Application {
    @Value("${in.fixture.queue.name:infixture}")
    String inFixtureQueueName;

    @Value("${in.liability.queue.name:inliability}")
    String inLiabilityQueueName;

    @Value("${out.fixture.queue.name:outfixture}")
    String outFixtureQueueName;

    @Value("${out.liability.queue.name:outliability}")
    String outLiabilityQueueName;

    @Bean
    Queue inFixtureQueue() {
        return new Queue(inFixtureQueueName, false);
    }

    @Bean
    Queue inLiabilityQueue() {
        return new Queue(inLiabilityQueueName, false);
    }

    @Bean
    Queue outFixtureQueue() {
        return new Queue(outFixtureQueueName, false);
    }

    @Bean
    Queue outLiabilityQueue() {
        return new Queue(outLiabilityQueueName, false);
    }

    @Bean(name = "inFixtureMessageListenerAdapter")
    MessageListenerAdapter inFixtureMessageListenerAdapter(MongoTemplate mongoTemplate) {
        return new MessageListenerAdapter(new InFixtureMessageListener(mongoTemplate), "receiveMessage");
    }

    @Bean(name = "inLiabilityListenerAdapter")
    MessageListenerAdapter inLiabilityMessageListenerAdapter(MongoTemplate mongoTemplate) {
        return new MessageListenerAdapter(new InLiabilityMessageListener(mongoTemplate), "receiveMessage");
    }

    @Bean
    SimpleMessageListenerContainer inFixtureContainer(ConnectionFactory connectionFactory,
                                                      @Qualifier("inFixtureMessageListenerAdapter") MessageListenerAdapter inFixtureMessageListenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(inFixtureQueueName);
        container.setMessageListener(inFixtureMessageListenerAdapter);
        return container;
    }

    @Bean
    SimpleMessageListenerContainer inLiabilityContainer(ConnectionFactory connectionFactory,
                                                        @Qualifier("inLiabilityListenerAdapter") MessageListenerAdapter inLiabilityListenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(inLiabilityQueueName);
        container.setMessageListener(inLiabilityListenerAdapter);
        return container;
    }


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
