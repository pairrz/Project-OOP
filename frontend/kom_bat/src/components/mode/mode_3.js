import B1 from './ปุ่ม/Button_ 3.png';
import './mode_3.css';
import { useNavigate } from 'react-router-dom';

const Mode_3 = () => {
  const navigate = useNavigate();

  const handleClick = async () => {
    try {
      // ส่งข้อมูลไปที่ API แต่ไม่สนใจผลตอบกลับ
      fetch('http://localhost:8080/api/game/create', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          playerOneName: "Bot1",
          playerTwoName: "Bot2",
          gameMode: "AUTO_MODE"
        })
      }).catch((error) => {
        console.error("Error:", error); // แค่จับข้อผิดพลาด
      });

      // ไปที่หน้า Character ทันที
      navigate('/character');
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