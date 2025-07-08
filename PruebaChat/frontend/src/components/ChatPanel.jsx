import React, { useEffect, useState } from 'react';
import './ChatPanel.css';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

const WS_URL = 'http://localhost:8083/ws'; // Cambia el puerto si tu chat-aggregator usa otro

function ChatPanel() {
  const [messages, setMessages] = useState([]);

  useEffect(() => {
    const client = new Client({
      webSocketFactory: () => new SockJS(WS_URL),
      debug: function (str) { /* console.log(str); */ },
      reconnectDelay: 5000,
    });

    client.onConnect = () => {
      client.subscribe('/topic/chat', (msg) => {
        try {
          const data = JSON.parse(msg.body);
          setMessages(prev => [...prev.slice(-99), data]); // Máximo 100 mensajes
        } catch {}
      });
    };
    client.activate();
    return () => client.deactivate();
  }, []);

  return (
    <div className="chat-panel">
      <h2>Chat en tiempo real</h2>
      <div className="chat-messages">
        {messages.map((msg, idx) => (
          <div key={idx} className={`chat-message chat-${msg.platform}`}>
            <span className="chat-platform">[{msg.platform}]</span> <b>{msg.username}:</b> {msg.message}
          </div>
        ))}
      </div>
    </div>
  );
}

export default ChatPanel; 