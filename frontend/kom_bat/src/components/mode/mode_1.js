import B1 from './ปุ่ม/Button_ 1.png';
import './mode_1.css';
import { useNavigate } from 'react-router-dom';

const Mode_1 = () => {
    const navigate = useNavigate();

    const handleClick = async () => {
        try {
            const response = await fetch('http://localhost:8080/create', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    playerOneName: "Player1",
                    playerTwoName: "",
                    gameMode: "DUEL_MODE"  // 👈 ส่งค่าโหมด
                })
            });

            if (response.ok) {
                navigate('/WaitingRoom'); // ✅ กดแล้วไป waitingroom
            } else {
                console.error("Failed to create game");
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