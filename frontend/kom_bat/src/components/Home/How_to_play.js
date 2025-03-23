import React, { useState } from 'react';
import B2 from './ปุ่ม/B2.png';         // ปุ่มเรียก popup
import closeImg from './ปุ่ม/B5.png';   // รูปปุ่มกากบาท
import popupBG from './พื้นหลัง/BG2.png'; // รูป popup (รองรับ .png)
import './How_to_play.css';

const How_to_play = () => {
  const [showPopup, setShowPopup] = useState(false);

  const openPopup = () => setShowPopup(true);
  const closePopup = () => setShowPopup(false);

  return (
    <div>
      {/* ปุ่มเรียก popup */}
      <img 
        className="hover-image2" 
        src={B2} 
        alt="How to Play" 
        width="200" 
        onClick={openPopup} 
        style={{ cursor: 'pointer' }}
      />

      {/* Popup แสดงรูป + ข้อความ + ปุ่มปิด */}
      {showPopup && (
        <div className="popup-overlay">
          <img 
            src={popupBG} 
            alt="popup" 
            className="popup-image"
          />

          {/* ✅ ข้อความซ้อนบนรูป */}
          <div className="popup-text">
            <h1>วิธีการเล่นเกม</h1>
            <p>
            
              ใส่คำอธิบายวิธีการเล่นตรงนี้ได้เลย สามารถบรรยายหลายบรรทัด
              <br /> 
              เพิ่มเติมเนื้อหาได้ตามต้องการ
            </p>
          </div>

          {/* ปุ่มกากบาท */}
          <img 
            src={closeImg} 
            alt="close" 
            className="close-btn-img" 
            onClick={closePopup}
          />
        </div>
      )}
    </div>
  );
};

export default How_to_play;
