import B1 from './ปุ่ม/Button_ 1.png';
import './mode_1.css';
import { useNavigate } from 'react-router-dom';

const Mode_1 = () => {
    const navigate = useNavigate();

    const handleClick = async () => {
        try {
            const response = await fetch('http://localhost:8080/waiting-room/create?playerName=Player1', {
                method: 'POST'
            });

            if (response.ok) {
                const data = await response.json();
                console.log("Room Created:", data.roomCode); // ✅ Debug
                navigate('/WaitingRoom');
            } else {
                console.error("Failed to create room");
            }
        } catch (error) {
            console.error("Error:", error);
        }
    };

    return (
        <img
            className="image1"
            src={B1}
            alt="Hover Image"
            onClick={handleClick}
            style={{ cursor: 'pointer' }}
        />
    );
};

export default Mode_1;