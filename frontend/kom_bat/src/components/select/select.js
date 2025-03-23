import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import BackBotton from '../BackBotton/BackBotton';
import './Select.css';
import ghost1 from '../Character/รูป/G1.png';
import ghost2 from '../Character/รูป/G2.png';
import ghost3 from '../Character/รูป/G3.png';
import ghost4 from '../Character/รูป/G4.png';
import ghost5 from '../Character/รูป/G5.png';

export default function Select() {
  const navigate = useNavigate();
  const [characters, setCharacters] = useState([]);
  const [formData, setFormData] = useState([]);

  const characterData = [
    { id: 1, name: "ผีเวตาล", img: ghost1 },
    { id: 2, name: "ผีกุมาร", img: ghost2 },
    { id: 3, name: "ผีเปรต", img: ghost3 },
    { id: 4, name: "ผีนางรำ", img: ghost4 },
    { id: 5, name: "ผีตายโหง", img: ghost5 },
  ];

  useEffect(() => {
    const selected = JSON.parse(localStorage.getItem('selectedCharacters')) || [];
    const autoData = selected.map((charId) => ({
      id: charId,
      strategy: '',
      hp: '',
      def: '',
      isAuto: false,
    }));
    setCharacters(selected);
    setFormData(autoData);
  }, []);

  const handleChange = (index, field, value) => {
    const updated = [...formData];
    updated[index][field] = value;
    setFormData(updated);
  };

  const handleAuto = (index) => {
    const updated = [...formData];
    updated[index] = {
      ...updated[index],
      strategy: 'Auto-Strategy',
      hp: 100,
      def: 50,
      isAuto: true
    };
    setFormData(updated);
  };

  const handleConfirm = () => {
    const incomplete = formData.some(
      (char) => !char.strategy || !char.hp || !char.def
    );
    if (incomplete) {
      alert('กรุณากรอก Strategy, HP, DEF หรือเลือก Auto ให้ครบทุกตัว');
      return;
    }
    localStorage.setItem('finalCharacters', JSON.stringify(formData));
    navigate('/play');
  };

  return (
    
    <div className="select-container">
  <h1>ปลุกเสกวิญญาณ</h1>
<div className="character-list">   {/* ✅ ใส่ครอบตัวละคร */}
  {characters.map((charId, index) => {
      const charInfo = characterData.find((c) => c.id === charId);
      return (
        <div className="character-box" key={index}>
          {charInfo && <img src={charInfo.img} alt={charInfo.name} className="character-img" />}
          <p>{charInfo?.name}</p>
          <input
            type="text"
            placeholder="Strategy"
            value={formData[index]?.strategy}
            onChange={(e) => handleChange(index, 'strategy', e.target.value)}
            disabled={formData[index]?.isAuto}
          />
          <input
            type="number"
            placeholder="HP"
            value={formData[index]?.hp}
            onChange={(e) => handleChange(index, 'hp', e.target.value)}
            disabled={formData[index]?.isAuto}
          />
          <input
            type="number"
            placeholder="DEF"
            value={formData[index]?.def}
            onChange={(e) => handleChange(index, 'def', e.target.value)}
            disabled={formData[index]?.isAuto}
          />
          <button className="auto-btn" onClick={() => handleAuto(index)}>Auto</button>
        </div>
      );
    })}
  </div>

  <button className="confirm-btn" onClick={handleConfirm}>ยืนยัน</button>
  <BackBotton />
</div>



  );
}