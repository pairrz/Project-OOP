import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const WaitingRoom = () => {
    const [playerName, setPlayerName] = useState('');
    const [roomCode, setRoomCode] = useState('');
    const [createdRoomCode, setCreatedRoomCode] = useState('');
    const [roomId, setRoomId] = useState('');  // เพิ่ม state สำหรับ roomId
    const navigate = useNavigate();

    // ฟังก์ชันสำหรับการสร้างห้อง
    const createRoom = async () => {
        try {
            const response = await fetch(`http://localhost:8080/api/room/create?playerName=${playerName}`, {
                method: 'POST',
            });

            if (response.ok) {
                const data = await response.json();
                setCreatedRoomCode(data.roomCode);  // ตั้งค่ารหัสห้องจากข้อมูลที่ได้รับ
                setRoomId(data.roomId);  // ตั้งค่า ID ของห้องที่ได้รับ
                alert(`Room Created! Room Code: ${data.roomCode}, Room ID: ${data.roomId}`);  // แจ้งรหัสห้องและ ID
            } else {
                console.error("Failed to create room");
            }
        } catch (error) {
            console.error("Error:", error);  // เก็บข้อผิดพลาดถ้ามี
        }
    };

    // ฟังก์ชันสำหรับการเข้าร่วมห้อง
    const joinRoom = async () => {
        try {
            const response = await fetch(`http://localhost:8080/api/room/join?roomCode=${roomCode}&playerName=${playerName}`, {
                method: 'POST',
            });
            const data = await response.json();
            if (data.status === 'success') {
                alert('Successfully joined the room!');
                navigate('/character');  // เปลี่ยนเป็นหน้า Character
            } else {
                alert('Failed to join room!');
            }
        } catch (error) {
            console.error("Error:", error);
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

            {createdRoomCode && (
                <div>
                    <p>Room Code: {createdRoomCode}</p>
                    <p>Room ID: {roomId}</p>  {/* แสดง ID ของห้อง */}
                    <input
                        type="text"
                        placeholder="Enter Room Code to Join"
                        value={roomCode}
                        onChange={(e) => setRoomCode(e.target.value)}
                    />
                    <button onClick={joinRoom}>Join Room</button>
                </div>
            )}
        </div>
    );
};

export default WaitingRoom;
