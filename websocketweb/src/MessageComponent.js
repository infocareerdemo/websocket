import React, { useState, useEffect, useRef } from 'react';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';

  const MessageComponent = () => {
  const [messages, setMessages] = useState([]);
  const stompClientRef = useRef(null);            
  const connect = () => {
    let socket = new SockJS('http://localhost:8080/ws');
    stompClientRef.current = Stomp.over(socket, { websocketFactory: SockJS });
    stompClientRef.current.connect({}, onConnected, onError);
  };

  const onConnected = () => {
    console.log('Connected to server');

    stompClientRef.current.subscribe('/topic/message', (message) => {
      setMessages((prevMessages) => [...prevMessages, JSON.parse(message.body)]);
    });
  };

  const onError = (error) => {
    console.error('Connection error:', error);
  };

  useEffect(() => {
    connect();
    return () => {
      if (stompClientRef.current) {
        stompClientRef.current.disconnect();
      }
    };
  }, []);

  useEffect(() => {   
    fetchMessages();
  }, []);

  const fetchMessages = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/messages/all');
      if (response.ok) {
        const data = await response.json();
        setMessages(data);
      } else {
        console.error('Failed to fetch messages');
      }
    } catch (error) {
      console.error('Error fetching messages:', error);
    }
  };

  return (
    <div>
      <h1>React Messages</h1>
      <ul>
        {messages.map((msg, index) => (
          <li key={index}>{msg.content}</li>
        ))}
      </ul>
    </div>
  );
};

export default MessageComponent;