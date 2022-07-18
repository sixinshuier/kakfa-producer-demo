package com.producer.demo.controller;

import com.producer.demo.model.User;
import com.producer.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("kafka")
public class UserController {

    @Autowired
    UserService userService;


    @GetMapping("/sync/publish/{name}")
    public User syncSend(@PathVariable("name") final String name) throws ExecutionException, InterruptedException {
        return userService.SyncSendMessage(name);
    }

    @GetMapping("/async/publish/{name}")
    public User async(@PathVariable("name") final String name){
        return userService.AsyncSendMessage(name);
    }

    @GetMapping("/partition/publish/{name}")
    public User partition(@PathVariable("name") final String name){
        return userService.partition(name);
    }

    @GetMapping("/transactions/publish/{name}")
    public User TransactionsSend(@PathVariable("name") final String name) {
        return userService.TransactionsSend(name);
    }


}
