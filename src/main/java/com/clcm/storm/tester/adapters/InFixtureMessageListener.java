package com.clcm.storm.tester.adapters;

import com.mongodb.util.JSON;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Created by agrawald on 8/02/16.
 */
public class InFixtureMessageListener {
    MongoTemplate mongoTemplate;

    public InFixtureMessageListener(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void receiveMessage(byte[] message) {
        System.out.println("Received <" + message + ">");
        mongoTemplate.insert(JSON.parse(new String(message)), "fixture");
    }
}
