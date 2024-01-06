package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Message;


@CrossOrigin(origins = "http://localhost:3001")                          //allow client origins
@RestController
@RequestMapping("/api/messages")
public class MessageController {
	
	private List<Message> receivedMessages = new ArrayList<>();          // For load sent the messages

	@Autowired
    private SimpMessagingTemplate template;                               //For convert msg to websocket

	@PostMapping("/send")                                                 // send message
	public ResponseEntity<Void> sendMessage(@RequestBody Message message) {        
	    template.convertAndSend("/topic/message", message);               // For converAndSend msg
	    System.out.println("WebSocket Msg From SendMessage");             // sysout the send msg call 
	    receivedMessages.add(message);                                    // load the messages
	    return new ResponseEntity<>(HttpStatus.OK);
	}
	  
	 @GetMapping("/all")                                                   // get all api
	 public ResponseEntity<List<Message>> getAllMessages() { 
	    	System.out.println("WebSocket Msg From AllMessages");          // sysout the all msg call
	        return new ResponseEntity<>(receivedMessages, HttpStatus.OK);
	    }
}
