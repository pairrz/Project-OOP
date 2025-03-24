import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import BackBotton from '../BackBotton/BackBotton';
import './Select.css';
import ghost1 from '../Character/รูป/GG1.png';
import ghost2 from '../Character/รูป/GG2.png';
import ghost3 from '../Character/รูป/GG3.png';
import ghost4 from '../Character/รูป/GG4.png';
import ghost5 from '../Character/รูป/GG5.png';
import selectTitle from './ตกแต่ง/select_title.png';

export default function Select() {
  const navigate = useNavigate();
  const [characters, setCharacters] = useState([]);
  const [selectedChar, setSelectedChar] = useState(null);
  const [strategy, setStrategy] = useState('');
  const [hp, setHp] = useState('');
  const [def, setDef] = useState('');
  const [formData, setFormData] = useState([]);
  const [lockedChars, setLockedChars] = useState([]);
  const [activeCharId, setActiveCharId] = useState(null);

  const characterData = [
    { id: 1, name: "ผีเวตาล", img: ghost1 },
    { id: 2, name: "ผีกุมาร", img: ghost2 },
    { id: 3, name: "ผีเปรต", img: ghost3 },
    { id: 4, name: "ผีนางรำ", img: ghost4 },
    { id: 5, name: "ผีตายโหง", img: ghost5 },
  ];

  useEffect(() => {
    const selected = JSON.parse(localStorage.getItem('selectedCharacters')) || [];
    const selectedInfo = selected.map(id => characterData.find(c => c.id === id));
    setCharacters(selectedInfo);

    if (selectedInfo.length > 0) {
      setActiveCharId(selectedInfo[0].id);
      setSelectedChar(selectedInfo[0]);
    }
  }, []);

  const handleOK = () => {
    if (!strategy || !hp || !def) {
      alert('กรุณากรอก Strategy, HP, DEF หรือกด Auto');
      return;
    }

    const existing = formData.find(f => f.id === selectedChar.id);
    const updated = existing
      ? formData.map(f => (f.id === selectedChar.id ? { ...f, strategy, hp, def } : f))
      : [...formData, { ...selectedChar, strategy, hp, def }];
    
    setFormData(updated);
    setLockedChars([...lockedChars, selectedChar.id]);
    alert(`บันทึก ${selectedChar.name} สำเร็จ!`);
  };

  const handleAuto = () => {
    setStrategy('Auto-Strategy');
    setHp(100);
    setDef(50);
  };

  const handleCancel = () => {
    setLockedChars(lockedChars.filter(id => id !== selectedChar.id));
    setFormData(formData.filter(f => f.id !== selectedChar.id));
  };

  const handleConfirm = () => {
    localStorage.setItem('finalCharacters', JSON.stringify(formData));
    navigate('/play');
  };

  const isLocked = selectedChar && lockedChars.includes(selectedChar.id);
  const isCompleted = (id) => formData.some(f => f.id === id);

  const handleIconClick = (id) => {
    setSelectedChar(characterData.find(c => c.id === id));
    setActiveCharId(id);
  };

  return (
    <div className="select-container">
      <img src={selectTitle} alt="หัวข้อ" className="select-title" />

      <div className="select-layout">
        {/* แสดงตัวละครฝั่งซ้าย */}
        <div className="select-character-side">
          {characters.filter((_, i) => i % 2 === 0).map((char) => (
            <div className="select-character-card" key={char.id} onClick={() => handleIconClick(char.id)}>
              <img
                src={char.img}
                alt={char.name}
                className={`select-character-icon ${isCompleted(char.id) ? 'completed' : ''}`}
                style={{ filter: selectedChar?.id === char.id ? 'grayscale(100%)' : 'none' }}
              />
            </div>
          ))}
        </div>

        {/* กล่องรายละเอียดที่เลือก */}
        {selectedChar && (
          <div className="center-box">
            <img src={selectedChar.img} alt={selectedChar.name} className="select-selected-char-img" />
            <textarea placeholder="Strategy" value={strategy} onChange={e => setStrategy(e.target.value)} disabled={isLocked} />
            <input type="number" placeholder="HP" value={hp} onChange={e => setHp(e.target.value)} disabled={isLocked} />
            <input type="number" placeholder="DEF" value={def} onChange={e => setDef(e.target.value)} disabled={isLocked} />
            <button className="auto-btn" onClick={handleAuto} disabled={isLocked}>Auto</button>
            <button className="ok-btn" onClick={handleOK} disabled={isLocked}>OK</button>
            {isLocked && <button className="cancel-btn" onClick={handleCancel}>ยกเลิก</button>}
          </div>
        )}

        {/* แสดงตัวละครฝั่งขวา */}
        <div className="select-character-side">
          {characters.filter((_, i) => i % 2 !== 0).map((char) => (
            <div className="select-character-card" key={char.id} onClick={() => handleIconClick(char.id)}>
              <img
                src={char.img}
                alt={char.name}
                className={`select-character-icon ${isCompleted(char.id) ? 'completed' : ''}`}
                style={{ filter: selectedChar?.id === char.id ? 'grayscale(100%)' : 'none' }}
              />
            </div>
          ))}
        </div>
      </div>

      {formData.length === characters.length && (
        <button className="select-confirm-btn" onClick={handleConfirm}>
          ยืนยันทั้งหมด
        </button>
      )}

      <BackBotton />
    </div>
  );
}
