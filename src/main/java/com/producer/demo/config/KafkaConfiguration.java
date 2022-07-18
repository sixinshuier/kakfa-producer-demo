package com.producer.demo.config;

import com.producer.demo.kafka.MyPartitioner;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        // 给 kafka 配置对象添加配置信息:bootstrap.servers
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9091,kafka2:9092,kafka3:9093");
        // 序列化器
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        // 自定分区策略
        config.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, MyPartitioner.class);
        // batch.size:批次大小
        config.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        // RecordAccumulator:缓冲区大小，默认 32M:buffer.memory
        config.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        // compression.type:压缩，默认 none，可配置值 gzip、snappy、 lz4 和 zstd
        config.put(ProducerConfig.COMPRESSION_TYPE_CONFIG,"snappy");
        // 重试次数
        config.put(ProducerConfig.RETRIES_CONFIG, 3);
        config.put(ProducerConfig.ACKS_CONFIG, "-1");
        // 事务ID
//        config.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "tranactional_id_01");
//        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);


        return new DefaultKafkaProducerFactory<>(config);
    }


    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}
