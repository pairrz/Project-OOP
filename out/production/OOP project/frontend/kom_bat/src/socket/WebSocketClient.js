import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

const connectWebSocket = (onMessage) => {
  const socket = new SockJS('http://localhost:8080/ws');
  const stompClient = new Client({
    webSocketFactory: () => socket,
    onConnect: () => {
      console.log("✅ Connected to Spring WebSocket");

      // ✅ Subscribe รอ message จากห้อง lobby
      stompClient.subscribe('/room/lobby', (message) => {
        console.log("✅ Message from server:", message.body);
        if (onMessage) onMessage(message.body);
      });

      // ✅ ทดสอบส่งข้อความ
      stompClient.publish({ destination: '/app/join', body: 'Player_1' });
    }
  });

  stompClient.activate();
  return stompClient;
};

export default connectWebSocket;
