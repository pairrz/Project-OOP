import React, { useEffect } from 'react';
import connectWebSocket from './socket/WebSocketClient';

import React, { useState } from "react";

const WaitingRoom = () => {
  const [playerName, setPlayerName] = useState("");
  const [roomCode, setRoomCode] = useState("");
  const [createdRoomCode, setCreatedRoomCode] = useState("");

    const createRoom = async () => {
        const response = await fetch("http://localhost:8080/waiting-room/create?playerName=" + playerName, {
            method: "POST",
        });
        const data = await response.json();
        setCreatedRoomCode(data.roomCode);
    };

    const joinRoom = async () => {
        const response = await fetch(`http://localhost:8080/waiting-room/join?roomCode=${roomCode}&playerName=${playerName}`, {
            method: "POST",
        });
        const data = await response.json();
        if (data.status === "success") {
            alert("Game Started!");
            navigate('/GameBoard');
        }
    };

    return (
      <div>
        <h2>Waiting Room</h2>
        <input
            type="text"
            placeholder="Enter your name"
            value={playerName}
            onChange={(e) => setPlayerName(e.target.value)}
        />
        <button onClick={createRoom}>Create Room</button>

        {createdRoomCode && <p>Room Code: {createdRoomCode}</p>}

        <input
            type="text"
            placeholder="Enter Room Code"
            value={roomCode}
            onChange={(e) => setRoomCode(e.target.value)}
        />
        <button onClick={joinRoom}>Join Room</button>
      </div>
  );
};

export default WaitingRoom;
