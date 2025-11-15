package com.example.project.config;

import com.example.project.model.Appointment;

import jakarta.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;

@Configuration
public class MongoConfig {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MongoConfig(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @PostConstruct
    public void initIndexes() {
        IndexOperations ops = mongoTemplate.indexOps(Appointment.class);

        ops.ensureIndex(
                new Index()
                        .on("doctorSlotKey", org.springframework.data.domain.Sort.Direction.ASC)
                        .unique()
                        .named("doctor_slot_unique")
        );
    }
}
