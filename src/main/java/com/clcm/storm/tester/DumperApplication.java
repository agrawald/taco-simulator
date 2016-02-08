package com.clcm.storm.tester;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * Created by agrawald on 8/02/16.
 */
@SpringBootApplication
public class DumperApplication implements CommandLineRunner{
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    MongoTemplate mongoTemplate;

    @Value("${out.fixture.queue.name:outfixture}")
    String outFixtureQueueName;

    @Value("${out.liability.queue.name:outliability}")
    String outLiabilityQueueName;

    public Runnable dumpFixtures(){
        return new Runnable() {
            @Override
            public void run() {
                List<BasicDBObject> fixtures = mongoTemplate.findAll(BasicDBObject.class, "fixture");
                fixtures.forEach(fixture -> {
                    fixture.remove("_id");
                    fixture.remove("_class");
                    rabbitTemplate.convertAndSend(outFixtureQueueName, fixture.toString());
                });
            }
        };
    }

    public Runnable dumpLiabilities(){
        return new Runnable() {
            @Override
            public void run() {
                List<BasicDBObject> liabilities = mongoTemplate.findAll(BasicDBObject.class, "liabilities");
                liabilities.forEach(liability -> {
                    liability.remove("_id");
                    liability.remove("_class");
                    rabbitTemplate.convertAndSend(outLiabilityQueueName, liability.toString());
                });
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(DumperApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        Executors.newSingleThreadExecutor().submit(dumpFixtures());
        Executors.newSingleThreadExecutor().submit(dumpLiabilities());
    }
}
