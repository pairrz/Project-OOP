import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const JoinRoom = () => {
  const [roomId, setRoomId] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleJoin = () => {
    if (!roomId) {
      setError('กรุณาใส่ Room ID');
      return;
    }

    // ✅ เช็คกับ Spring ว่าห้องมีจริง
    axios.get(`http://localhost:8080/api/room/check/${roomId}`)
      .then(response => {
        if (response.data.exists) {
          navigate(`/waitingroom?roomId=${roomId}`); // ✅ วิ่งเข้า waitingroom พร้อมส่ง roomId
        } else {
          setError('ไม่พบห้องนี้');
        }
      })
      .catch(() => setError('เกิดข้อผิดพลาด'));
  };

  return (
    <div>
      <h1>เข้าร่วมห้อง</h1>
      <input
        type="text"
        value={roomId}
        onChange={(e) => setRoomId(e.target.value)}
        placeholder="กรอก Room ID"
      />
      <button onClick={handleJoin}>Join</button>
      {error && <p style={{ color: 'red' }}>{error}</p>}
    </div>
  );
};

export default JoinRoom;
