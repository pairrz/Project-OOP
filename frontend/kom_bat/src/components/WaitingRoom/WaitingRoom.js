import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import logo from './ตกแต่ง/logo.png';
import './WaitingRoom.css';
import BackBotton from '../BackBotton/BackBotton';

const WaitingRoom = () => {
    const [playerName, setPlayerName] = useState('');
    const [roomCode, setRoomCode] = useState('');
    const [createdRoomCode, setCreatedRoomCode] = useState('');  // เก็บรหัสห้องที่สร้างขึ้น
    const [roomId, setRoomId] = useState('');  // เก็บ ID ของห้อง
    const navigate = useNavigate();

    // ฟังก์ชันสำหรับการสร้างห้อง
    const createRoom = async () => {
        try {
            const response = await fetch(`http://localhost:8080/api/room/create?playerName=${playerName}`, {
                method: 'POST',
            });
    
            if (response.ok) {
                const data = await response.json();
                setCreatedRoomCode(data.roomCode);  // ตั้งค่ารหัสห้อง
                setRoomId(data.roomId);  // ตั้งค่า ID ห้อง
                alert(`Room Created! Room Code: ${data.roomCode}, Room ID: ${data.roomId}`);
            } else {
                console.error("Failed to create room");
            }
        } catch (error) {
            console.error("Error:", error);  // จัดการข้อผิดพลาด
        }
    };

    // ฟังก์ชันสำหรับการเข้าร่วมห้อง
    const joinRoom = async () => {
        if (!roomCode || !playerName) {
            alert("กรุณากรอกชื่อและรหัสห้องให้ครบถ้วน");
            return;
        }
        try {
            const response = await fetch(`http://localhost:8080/api/room/join?roomCode=${roomCode}&playerName=${playerName}`, {
                method: 'POST',
            });
            const data = await response.json();
            if (data.status === 'success') {
                alert('Successfully joined the room!');
                navigate('/character');  // ไปยังหน้า Character
            } else {
                alert('Failed to join room!');
            }
        } catch (error) {
            console.error("Error:", error);  // จัดการข้อผิดพลาด
        }
    };

    return (
        <div className='waiting-room'>
            <h1 className="game-title">
                <img src={logo} alt="คอมแบท" className="title-logo" />
            </h1>
            <input
                type="text"
                placeholder="Enter your name"
                value={playerName}
                onChange={(e) => setPlayerName(e.target.value)}
            />
            <button onClick={createRoom}>Create Room</button>

            {createdRoomCode && (
                <div>
                    <p>Room Code: {createdRoomCode}</p> {/* แสดงรหัสห้องที่สร้าง */}
                    <p>Room ID: {roomId}</p>  {/* แสดง ID ห้อง */}
                    <input
                        type="text"
                        placeholder="Enter Room Code to Join"
                        value={roomCode}
                        onChange={(e) => setRoomCode(e.target.value)} // รับค่า Room Code
                    />
                    <button onClick={joinRoom}>Join Room</button> {/* ปุ่ม Join Room */}
                </div>
            )}
             <BackBotton />
        </div>
    );
};

export default WaitingRoom;
