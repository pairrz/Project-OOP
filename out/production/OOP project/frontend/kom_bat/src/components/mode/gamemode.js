import React, { useEffect, useRef } from 'react';
import { useLocation } from 'react-router-dom';
import H2 from "./H";
import Mode_1 from "./mode_1";
import Mode_2 from "./mode_2";
import Mode_3 from "./mode_3";
import BackBotton from "../BackBotton/BackBotton";
import './gamemode.css';
import modeMusic from './ตกแต่ง/เสียง/M_sound.mp3';

const audio = new Audio(modeMusic);  // ประกาศ Audio เป็น global-level

function Gamemode() {
  const location = useLocation();

  useEffect(() => {
    if (location.pathname === '/gamemode') {
      audio.loop = true;
      audio.play().catch(err => {
        console.error("เล่นเสียงไม่ได้:", err);
      });
    } else {
      audio.pause();
      audio.currentTime = 0;
      console.log('เสียงหยุด');
    }

    return () => {
      audio.pause();
      audio.currentTime = 0;
      console.log('เสียงหยุด');
    };
  }, [location.pathname]);

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
