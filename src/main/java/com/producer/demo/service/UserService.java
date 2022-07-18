package com.producer.demo.service;

import com.alibaba.fastjson.JSON;
import com.producer.demo.model.User;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "test";

    // 同步发送
    public User SyncSendMessage(String name) throws ExecutionException, InterruptedException {
        User user = new User(name, "PM", 1000L);
        String message = JSON.toJSONString(user);
        kafkaTemplate.send(TOPIC, message).get();
        return user;
    }

    // 异步回调
    public User AsyncSendMessage(String name){
        User user = new User(name, "PM", 1000L);
        String message = JSON.toJSONString(user);
        ListenableFuture<SendResult<String, Object>>  future = kafkaTemplate.send(TOPIC, message);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                System.out.println("Topic ：" + result.getRecordMetadata().topic()+ " Partition: "+ result.getRecordMetadata().partition());
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Error :" + ex.getMessage());
            }
        });
        return user;
    }

    // 指定分区
    public User partition(String name){
        User user = new User(name, "PM", 1000L);
        String message = JSON.toJSONString(user);
        kafkaTemplate.send(TOPIC, 1,"test",message);
        return user;
    }

    // 事务发送
    public User TransactionsSend(String name){
        User user= new User(name, "PM", 1000L);
        String message = JSON.toJSONString(user);
        kafkaTemplate.executeInTransaction( t ->{
            t.send(TOPIC,message);
            return true;
        });
        return user;
    }
}
