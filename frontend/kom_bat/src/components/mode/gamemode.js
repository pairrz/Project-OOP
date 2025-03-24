import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import H2 from "./H";
import Mode_1 from "./mode_1";
import Mode_2 from "./mode_2";
import Mode_3 from "./mode_3";
import BackBotton from "../BackBotton/BackBotton"; // เพิ่มปุ่มย้อนกลับเข้ามา
import './gamemode.css';

import modeMusic from './ตกแต่ง/เสียง/M_sound.mp3';  // Import ไฟล์เสียง

function Gamemode() {
  const [isPlaying, setIsPlaying] = useState(false);  // สถานะการเล่นเสียง
  const location = useLocation(); // ใช้เพื่อรู้ว่าอยู่ที่หน้าไหน

  useEffect(() => {
    if (location.pathname === '/gamemode' && !isPlaying) {
      const audio = new Audio(modeMusic);
      audio.loop = true;
      audio.play();
      setIsPlaying(true);
    }

    return () => {
      if (isPlaying) {
        const audio = new Audio(modeMusic);
        audio.pause();
        audio.currentTime = 0;
        setIsPlaying(false); // หยุดเสียงเมื่อออกจากหน้า
      }
    };
  }, [location.pathname, isPlaying]); // รันเมื่อเส้นทางเปลี่ยน

  return (
    <div className="mode">
      <BackBotton />
      <H2 />          
      <div className="mode-group">
        <Mode_1 />
        <Mode_2 />
        <Mode_3 />
      </div>
    </div>
  );
}

export default Gamemode;
