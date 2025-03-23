import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import BackBotton from '../BackBotton/BackBotton';
import './Select.css';
import ghost1 from '../Character/รูป/G1.png';
import ghost2 from '../Character/รูป/G2.png';
import ghost3 from '../Character/รูป/G3.png';
import ghost4 from '../Character/รูป/G4.png';
import ghost5 from '../Character/รูป/G5.png';
import selectTitle from './ตกแต่ง/select_title.png';

export default function Select() {
  const navigate = useNavigate();
  const [characters, setCharacters] = useState([]);
  const [selectedChar, setSelectedChar] = useState(null);
  const [strategy, setStrategy] = useState('');
  const [hp, setHp] = useState('');
  const [def, setDef] = useState('');
  const [formData, setFormData] = useState([]);
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

    alert(`บันทึก ${selectedChar.name} สำเร็จ!`);
  };

  const handleAuto = () => {
    setStrategy('Auto-Strategy');
    setHp(100);
    setDef(50);
  };

  const handleConfirm = () => {
    localStorage.setItem('finalCharacters', JSON.stringify(formData));
    navigate('/play');
  };

  return (
    <div className="select-container">
      <img src={selectTitle} alt="หัวข้อ" className="select-title" />

      <div className="select-layout">
        <div className="character-side">
          {characters.filter((_, i) => i % 2 === 0).map((char) =>
            activeCharId === char.id ? null : (
              <img
                key={char.id}
                src={char.img}
                alt={char.name}
                className="character-icon"
                onClick={() => {
                  setActiveCharId(char.id);
                  setSelectedChar(char);
                  const data = formData.find(f => f.id === char.id);
                  setStrategy(data?.strategy || '');
                  setHp(data?.hp || '');
                  setDef(data?.def || '');
                }}
              />
            )
          )}
        </div>

        {selectedChar && (
          <div className="center-box">
            <img src={selectedChar.img} alt={selectedChar.name} className="selected-char-img" />
            <textarea placeholder="Strategy" value={strategy} onChange={e => setStrategy(e.target.value)} />
            <input type="number" placeholder="HP" value={hp} onChange={e => setHp(e.target.value)} />
            <input type="number" placeholder="DEF" value={def} onChange={e => setDef(e.target.value)} />
            <button className="auto-btn" onClick={handleAuto}>Auto</button>
            <button className="ok-btn" onClick={handleOK}>OK</button>
          </div>
        )}

        <div className="character-side">
          {characters.filter((_, i) => i % 2 !== 0).map((char) =>
            activeCharId === char.id ? null : (
              <img
                key={char.id}
                src={char.img}
                alt={char.name}
                className="character-icon"
                onClick={() => {
                  setActiveCharId(char.id);
                  setSelectedChar(char);
                  const data = formData.find(f => f.id === char.id);
                  setStrategy(data?.strategy || '');
                  setHp(data?.hp || '');
                  setDef(data?.def || '');
                }}
              />
            )
          )}
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
