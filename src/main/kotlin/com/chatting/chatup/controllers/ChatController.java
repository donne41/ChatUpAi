package com.chatting.chatup.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {
    private final Logger log = LoggerFactory.getLogger(ChatController.class);

    @GetMapping("/")
    public ResponseEntity homeScreen() {
        log.atInfo().log("Get Request recived");
        return new ResponseEntity(new MessageResponse("First", "TIME"), HttpStatus.OK);
    }
}
