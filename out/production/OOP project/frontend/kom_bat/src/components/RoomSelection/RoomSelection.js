import React from 'react';
import { useNavigate } from 'react-router-dom';

const RoomSelection = () => {
  const navigate = useNavigate();

  const handleCreate = () => {
    navigate('/waitingroom');  // ✅ สร้างห้องใหม่
  };

  const handleJoin = () => {
    navigate('/joinroom');     // ✅ วิ่งไปหน้ากรอก Room ID
  };

  return (
    <div className="room-selection">
      <h1>เลือกการเชื่อมต่อ</h1>
      <button onClick={handleCreate}>สร้างห้อง</button>
      <button onClick={handleJoin}>เข้าร่วมห้อง</button>
    </div>
  );
};

export default RoomSelection;
