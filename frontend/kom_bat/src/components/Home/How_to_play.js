import React, { useState } from 'react';
import B2 from './ปุ่ม/B2.png';         // ปุ่มเรียก popup
import closeImg from './ปุ่ม/B5.png';   // รูปปุ่มกากบาท
import popupBG from './ตกแต่ง/Howto.png'; // รูป popup (รองรับ .png)
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
          {/*<div className="popup-text">
          <h1>วิธีการเล่นเกม</h1>
          <p>
    <strong>การควบคุมเกม</strong><br/>
    ใช้เมาส์และคีย์บอร์ดในการเล่น  
    <br/>
    
    <strong>การเตรียมตัว</strong><br/>
    เมื่อเข้าสู่เกม ผู้เล่นจะเลือกมินเนี่ยนจากตัวเลือกที่มีให้ (5 ตัว) จากนั้นกรอกค่าพลังและสถานะของมินเนี่ยน  
    <br/>
    
    <strong>วิธีการเล่น</strong><br/>
    แต่ละฝ่ายจะเล่นตามเทิร์นของตนเอง  
    ในแต่ละเทิร์น ผู้เล่นสามารถ **ซื้อ, อัญเชิญ, และ จบเทิร์น** ได้<br/> 
    เมื่อจบเทิร์นมินเนี่ยนจะเดินตามที่ผู้เล่นได้เขียนสคริปเอาไว้ 
    <br/>
    
    <strong>เงื่อนไขการจบเกม</strong><br/>
    เกมจะจบลงเมื่อ **มินเนี่ยนของผู้เล่นหมด** หรือ **จำนวนเทิร์นหมด**  
    <br/>
    
    <strong>วิธีการตัดสินผู้ชนะ</strong><br/>
    - หากมีเพียงฝ่ายเดียวที่เหลือมินเนี่ยน ฝ่ายนั้นชนะ <br/> 
    - หากทั้งสองฝ่ายไม่มีมินเนี่ยนเหลือ ให้ดูว่าใครมีมินเนี่ยนมากกว่าก่อนจบเกม<br/>  
    - หากจำนวนมินเนี่ยนเท่ากัน จะวัดจาก **ผลรวมค่าพลังชีวิตของมินเนี่ยน** <br/> 
    - หากค่าพลังชีวิตเท่ากัน ให้พิจารณาจาก **ทรัพยากรที่เหลืออยู่**  
</p>
          </div>*/}

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
