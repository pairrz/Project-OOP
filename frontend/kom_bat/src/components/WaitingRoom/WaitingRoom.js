import React, { useEffect } from 'react';
import connectWebSocket from './socket/WebSocketClient';

const WaitingRoom = () => {
  useEffect(() => {
    const stompClient = connectWebSocket((msg) => {
      console.log("✅ ได้รับจาก server:", msg);
    });

    return () => {
      if (stompClient) stompClient.deactivate(); // ✅ disconnect ตอนออก
    };
  }, []);

  return (
    <div>
      <h1>รอเพื่อนเชื่อมต่อ...</h1>
    </div>
  );
};

export default WaitingRoom;
