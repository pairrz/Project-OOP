import B1 from './ปุ่ม/Button_ 2.png';
import './mode_2.css';
import { useNavigate } from 'react-router-dom';

const Mode_2 = () => {
  const navigate = useNavigate();

  const handleClick = async () => {
    try {
      const response = await fetch('http://localhost:8080/create', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          playerOneName: "Player1",
          playerTwoName: "Bot",
          gameMode: "SOLITAIRE_MODE"  // 👈 ส่งค่าโหมด
        })
      });

      if (response.ok) {
        navigate('/Character');
      } else {
        console.error("Failed to create game");
      }
    } catch (error) {
      console.error("Error:", error);
    }
  };

  return (
      <img
          className="image2"
          src={B1}
          alt="Hover Image"
          width="1000"
          style={{ cursor: 'pointer' }}
          onClick={handleClick}
      />
  );
};

export default Mode_2;