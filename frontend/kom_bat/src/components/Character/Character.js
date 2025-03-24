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

import logo from './ตกแต่ง/logo2.png';
import fire from './ตกแต่ง/fire_animation.gif';
import confirmBtn from './ปุ่ม/button.png';

import name1 from './ตกแต่ง/Cr_เวตาล.png'; 
import name2 from './ตกแต่ง/Cr_กุมารทอง.png';
import name3 from './ตกแต่ง/Cr_เปรต.png';
import name4 from './ตกแต่ง/Cr_ผีนางรำ.png';
import name5 from './ตกแต่ง/Cr_ผีตายโหง.png';

const characters = [
    { id: 1, name: "ผีเวตาล", img: "path_to_ghost1_image", nameImg: "path_to_name1_image" },
    { id: 2, name: "ผีกุมาร", img: "path_to_ghost2_image", nameImg: "path_to_name2_image" },
    { id: 3, name: "ผีเปรต", img: "path_to_ghost3_image", nameImg: "path_to_name3_image" },
    { id: 4, name: "ผีนางรำ", img: "path_to_ghost4_image", nameImg: "path_to_name4_image" },
    { id: 5, name: "ผีตายโหง", img: "path_to_ghost5_image", nameImg: "path_to_name5_image" },
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

    const handleConfirm = async () => {
        // ส่งข้อมูลมินเนียนที่เลือกไปที่ API
        try {
            const response = await fetch('http://localhost:8080/api/game/selectMinions', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    selectedMinions: selected,  // ส่ง ID ของมินเนียนที่เลือก
                })
            });

            if (response.ok) {
                // หากส่งข้อมูลสำเร็จ, ไปที่หน้า Select
                navigate('/select');
            } else {
                console.error("Failed to assign minions");
            }
        } catch (error) {
            console.error("Error:", error);
        }
    };

    return (
        <div
            className="character-container"
            style={{

                backgroundSize: 'cover',
                backgroundPosition: 'center'
            }}
        >
            <img src={logo} alt="หัวข้อ" className="character-logo" />
            <div className="character-list">
                {characters.map((char) => (
                    <div
                        key={char.id}
                        className={character-card ${selected.includes(char.id) ? 'selected' : ''}}
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
                <img
                    src={confirmBtn}
                    alt="ยืนยัน"
                    className="confirm-btn"
                    onClick={handleConfirm}
                />
            )}
            <BackBotton />
        </div>
    );
}