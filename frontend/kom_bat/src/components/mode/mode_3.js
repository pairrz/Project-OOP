import B1 from './ปุ่ม/Button_ 3.png';
import './mode_3.css';
import { useNavigate } from 'react-router-dom';

const Mode_3 = () => {
  const navigate = useNavigate();

  const handleClick = async () => {
    try {
      const response = await fetch('http://localhost:8080/create', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          playerOneName: "Bot1",
          playerTwoName: "Bot2",
          gameMode: "AUTO_MODE"  // 👈 ส่งค่าโหมด
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
          className="image3"
          src={B1}
          alt="Hover Image"
          width="1000"
          height="auto"
          style={{ cursor: 'pointer' }}
          onClick={handleClick}
      />
  );
};

export default Mode_3;