import React, { useState } from 'react';
import BackBotton from '../BackBotton/BackBotton';
import './Character.css';
import { useNavigate } from 'react-router-dom';
import bgImage from './รูป/BG1.webp';
import ghost1 from './รูป/G1.png';
import ghost2 from './รูป/G2.png';
import ghost3 from './รูป/G3.png';
import ghost4 from './รูป/G4.png';
import ghost5 from './รูป/G5.png';

import logo from './ตกแต่ง/logo.png';
import fire from './ตกแต่ง/fire.png';

import name1 from './ตกแต่ง/ผี (1).png';
import name2 from './ตกแต่ง/ผี (2).png';
import name3 from './ตกแต่ง/ผี (3).png';
import name4 from './ตกแต่ง/ผี (4).png';
import name5 from './ตกแต่ง/ผี (5).png';


const characters = [
  { id: 1, name: "ผีเวตาล", img: ghost1, nameImg: name1 },
  { id: 2, name: "ผีกุมาร", img: ghost2, nameImg: name2 },
  { id: 3, name: "ผีเปรต", img: ghost3, nameImg: name3 },
  { id: 4, name: "ผีนางรำ", img: ghost4, nameImg: name4 },
  { id: 5, name: "ผีตายโหง", img: ghost5, nameImg: name5 },
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
    <div
      className="character-container"
      style={{
        backgroundImage: `url(${bgImage})`,
        backgroundSize: 'cover',
        backgroundPosition: 'center'
      }}
    >
      <img src={logo} alt="หัวข้อ" className="character-logo" />
      <div className="character-list">
  {characters.map((char) => (
    <div 
      key={char.id} 
      className={`character-card ${selected.includes(char.id) ? 'selected' : ''}`} 
      onClick={() => toggleSelect(char.id)}
    >
      <img src={char.img} alt={char.name} />
      <img src={char.nameImg} alt={char.name} className="name-img" />
    </div>
  ))}
</div>
      {selected.length > 0 && (
        <div className="fire-container">
          {selected.map((char, index) => (
            <img key={index} src={fire} alt="fire" className="fire-img" />
          ))}
        </div>
      )}
       
      {selected.length > 0 && (
        <button className="confirm-btn" onClick={handleConfirm}>ยืนยัน</button>
      )}
      <BackBotton />
    </div>
  );
}
