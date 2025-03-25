import React, { useState, useEffect, useRef } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import BackBotton from '../BackBotton/BackBotton';
import './Select.css';

import ghost1 from '../Character/รูป/GG1.png';
import ghost2 from '../Character/รูป/GG2.png';
import ghost3 from '../Character/รูป/GG3.png';
import ghost4 from '../Character/รูป/GG4.png';
import ghost5 from '../Character/รูป/GG5.png';
import selectTitle from './ตกแต่ง/select_title.png';
import selectMusic from './ตกแต่ง/เสียง/Select_sound.mp3';

import autoBtn from './ปุ่ม/auto.png';
import okBtn from './ปุ่ม/ok.png';
import confirmAllBtn from './ปุ่ม/confirm_all.png';
import cancelBtn from './ปุ่ม/cancel.png';

export default function Select() {
  const navigate = useNavigate();
  const location = useLocation();
  const audioRef = useRef(new Audio(selectMusic));

  const [characters, setCharacters] = useState([]);
  const [selectedChar, setSelectedChar] = useState(null);
  const [strategy, setStrategy] = useState('');
  const [hp, setHp] = useState('');
  const [def, setDef] = useState('');
  const [formData, setFormData] = useState([]);
  const [activeCharId, setActiveCharId] = useState(null);
  const [lockedChars, setLockedChars] = useState([]);

  const characterData = [
    { id: 1, name: "ผีเวตาล", img: ghost1 },
    { id: 2, name: "ผีกุมาร", img: ghost2 },
    { id: 3, name: "ผีเปรต", img: ghost3 },
    { id: 4, name: "ผีนางรำ", img: ghost4 },
    { id: 5, name: "ผีตายโหง", img: ghost5 },
  ];

  useEffect(() => {
    if (location.pathname === '/select') {
      const audio = audioRef.current;
      audio.loop = true;
      audio.play().catch(err => console.error('เล่นเสียง Select ไม่ได้:', err));
    }
    return () => {
      audioRef.current.pause();
      audioRef.current.currentTime = 0;
    };
  }, [location.pathname]);

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
      /*alert('กรุณากรอก Strategy, HP, DEF หรือกด Auto');*/
      return;
    }
    const existing = formData.find(f => f.id === selectedChar.id);
    const updated = existing
      ? formData.map(f => (f.id === selectedChar.id ? { ...f, strategy, hp, def } : f))
      : [...formData, { ...selectedChar, strategy, hp, def }];
    setFormData(updated);
    setLockedChars([...lockedChars, selectedChar.id]);
    /*alert(`บันทึก ${selectedChar.name} สำเร็จ!`);*/
  };

  const handleAuto = () => {
    setStrategy(`t = t + 1
m = 0
while (3 - m) {
  if (budget - 100) then {} else done
  opponentLoc = opponent
  cost = 30
  if (budget - cost) then {
    if ((opponentLoc - nearby upleft) * (opponentLoc - nearby upleft)) then shoot upleft cost
    else if ((opponentLoc - nearby upright) * (opponentLoc - nearby upright)) then shoot upright cost
    else if ((opponentLoc - nearby up) * (opponentLoc - nearby up)) then shoot up cost
    else if ((opponentLoc - nearby downleft) * (opponentLoc - nearby downleft)) then shoot downleft cost
    else if ((opponentLoc - nearby downright) * (opponentLoc - nearby downright)) then shoot downright cost
    else if ((opponentLoc - nearby down) * (opponentLoc - nearby down)) then shoot down cost
  } else done
  m = m + 1
}`);
    setHp(100);
    setDef(50);
  };

  const handleCancel = () => {
    setFormData(formData.filter(f => f.id !== selectedChar.id));
    setLockedChars(lockedChars.filter(id => id !== selectedChar.id));
  };

  const handleConfirm = () => {
    localStorage.setItem('finalCharacters', JSON.stringify(formData));
    navigate('/play');
  };

  const handleIconClick = (id) => {
    const charData = characterData.find(c => c.id === id);
    setSelectedChar(charData);
    setActiveCharId(id);
    const existing = formData.find(f => f.id === id);
    if (existing) {
      setStrategy(existing.strategy);
      setHp(existing.hp);
      setDef(existing.def);
    } else {
      setStrategy('');
      setHp('');
      setDef('');
    }
  };

  const isCompleted = (id) => formData.some(f => f.id === id);
  const isLocked = selectedChar && lockedChars.includes(selectedChar.id);

  return (
    <div className="select-container">
      <img src={selectTitle} alt="หัวข้อ" className="select-title" />

      <div className="select-layout">
        {characters.length > 1 && (
          <div className="select-character-side">
            {characters.filter((_, i) => i % 2 === 0).map((char) => (
              <div
                className={`select-character-card ${activeCharId === char.id ? 'active' : ''} ${isCompleted(char.id) ? 'locked' : ''}`}
                key={char.id}
                onClick={() => handleIconClick(char.id)}
              >
                <img src={char.img} alt={char.name} className="select-character-icon" />
              </div>
            ))}
          </div>
        )}

        {selectedChar && (
          <div className="center-box">
            {/* ซ้าย */}
            <div className="left-input">
              <img src={selectedChar.img} alt={selectedChar.name} className="select-selected-char-img" />
              <textarea
                placeholder="Strategy"
                value={strategy}
                onChange={e => setStrategy(e.target.value)}
                spellCheck={false}
                disabled={isLocked}
              />
              <div className="input-group">
                <input type="number" placeholder="HP" value={hp} onChange={e => setHp(e.target.value)} disabled={isLocked} />
                <input type="number" placeholder="DEF" value={def} onChange={e => setDef(e.target.value)} disabled={isLocked} />
              </div>
            </div>

            {/* ขวา */}
            <div className="right-button">
              <img src={autoBtn} alt="Auto" className="auto-btn" onClick={handleAuto} style={{ cursor: isLocked ? 'not-allowed' : 'pointer', opacity: isLocked ? 0.5 : 1 }} />
              <img src={okBtn} alt="OK" className="ok-btn" onClick={handleOK} style={{ cursor: isLocked ? 'not-allowed' : 'pointer', opacity: isLocked ? 0.5 : 1 }} />
              {isLocked && <img src={cancelBtn} alt="Cancel" className="cancel-btn" onClick={handleCancel} />}
            </div>
          </div>
        )}

        {characters.length > 1 && (
          <div className="select-character-side">
            {characters.filter((_, i) => i % 2 !== 0).map((char) => (
              <div
                className={`select-character-card ${activeCharId === char.id ? 'active' : ''} ${isCompleted(char.id) ? 'locked' : ''}`}
                key={char.id}
                onClick={() => handleIconClick(char.id)}
              >
                <img src={char.img} alt={char.name} className="select-character-icon" />
              </div>
            ))}
          </div>
        )}
      </div>

      {formData.length === characters.length && (
        <img
          src={confirmAllBtn}
          alt="ตกลงทั้งหมด"
          className="select-confirm-btn"
          onClick={handleConfirm}
        />
      )}

      <BackBotton />
    </div>
  );
}
