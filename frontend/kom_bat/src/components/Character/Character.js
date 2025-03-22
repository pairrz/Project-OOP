import React, { useState } from 'react';
import BackBotton from '../BackBotton/BackBotton';
import './Character.css';
import { useNavigate } from 'react-router-dom';

const characters = [
  { id: 1, name: "ผีดูดเลือด", img: "/images/ghost1.png" },
  { id: 2, name: "ผีกุมาร", img: "/images/ghost2.png" },
  { id: 3, name: "ผีเปรต", img: "/images/ghost3.png" },
  { id: 4, name: "ผีนางรำ", img: "/images/ghost4.png" },
  { id: 5, name: "ผีตายโหง", img: "/images/ghost5.png" },
];

export default function Character() {
  const [selected, setSelected] = useState([]);
  const navigate = useNavigate();

  const toggleSelect = (id) => {
    if (selected.includes(id)) {
      setSelected(selected.filter((item) => item !== id));
    } else {
      if (selected.length < 5) setSelected([...selected, id]);
    }
  };

  const handleConfirm = () => {
    localStorage.setItem('selectedCharacters', JSON.stringify(selected));
    navigate('/select');
  };

  return (
    <div className="character-container">
      <h1>เลือกเหล่าวิญญาณร้ายที่คุณต้องการจะใช้</h1>
      <div className="character-list">
        {characters.map((char) => (
          <div
            key={char.id}
            className={`character-card ${selected.includes(char.id) ? 'selected' : ''}`}
            onClick={() => toggleSelect(char.id)}
          >
            <img src={char.img} alt={char.name} />
            <p>{char.name}</p>
          </div>
        ))}
      </div>
      {selected.length > 0 && (
        <button className="confirm-btn" onClick={handleConfirm}>ยืนยัน</button>
      )}
      <BackBotton />
    </div>
  );
}