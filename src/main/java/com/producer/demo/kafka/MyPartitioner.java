package com.producer.demo.kafka;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.InvalidRecordException;
import org.apache.kafka.common.errors.InvalidTopicException;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MyPartitioner implements Partitioner {

    @Override
    public int partition(String topic, Object key, byte[] keyBytes,
                         Object value, byte[] valueBytes, Cluster cluster) {
        String msgValues = value.toString();

        int partition;

        if (msgValues.contains("test")){
            partition = 0;
        }else {
            partition = 1;
        }

        return partition;
    }

    @Override
    public void close() {
        //Nothing to close
    }


    @Override
    public void configure(Map<String, ?> configs) {

    }
}