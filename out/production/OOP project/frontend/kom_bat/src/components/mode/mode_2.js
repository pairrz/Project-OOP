import B1 from './ปุ่ม/Button_ 2.png';
import './mode_2.css';
import { useNavigate } from 'react-router-dom';

const Mode_2 = () => {
  const navigate = useNavigate();

  const handleClick = () => {
    // ส่งข้อมูลไปที่ API แต่ไม่รอผลตอบกลับ
    fetch('http://localhost:8080/api/game/create', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        playerOneName: "Player1",
        playerTwoName: "Bot",
        gameMode: "SOLITAIRE_MODE"  // ส่งค่าโหมด 2
      })
    }).catch((error) => {
      console.error("Error:", error);  // เก็บข้อผิดพลาดถ้ามี
    });

    // ไปที่หน้า Character ทันที โดยไม่รอ API
    navigate('/character');
  };

    // const handleClick = async () => {
    //     try {
    //         const response = await fetch('http://localhost:8080/api/game/create', {
    //             method: 'POST',
    //             headers: { 'Content-Type': 'application/json' },
    //             body: JSON.stringify({
    //                 playerOneName: "Player1",
    //                 playerTwoName: "Bot",
    //                 gameMode: "SOLITAIRE_MODE"
    //             })
    //         });
    //
    //         // ตรวจสอบสถานะการตอบกลับ
    //         if (response.ok) {
    //             console.log('Game created successfully');
    //             // ไปที่หน้า Character
    //             navigate('/character');
    //         } else {
    //             console.error('Failed to create game');
    //         }
    //     } catch (error) {
    //         console.error('Error:', error);
    //     }
    // };

    return (
      <img
          className="image2"
          src={B1}
          alt="Hover Image"
          width="1000"
          style={{ cursor: 'pointer' }}
          onClick={handleClick}  // คลิกแล้วไปหน้า Character ทันที
      />
  );
};

export default Mode_2;