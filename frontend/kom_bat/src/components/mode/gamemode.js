import React, { useEffect, useState, useRef } from 'react';
import { useLocation } from 'react-router-dom';
import H2 from "./H";
import Mode_1 from "./mode_1";
import Mode_2 from "./mode_2";
import Mode_3 from "./mode_3";
import BackBotton from "../BackBotton/BackBotton";
import './gamemode.css';
import modeMusic from './ตกแต่ง/เสียง/M_sound.mp3';

function Gamemode() {
  const [isPlaying, setIsPlaying] = useState(false);
  const audioRef = useRef(null);
  const location = useLocation();

  useEffect(() => {
    if (location.pathname === '/gamemode') {
      audioRef.current = new Audio(modeMusic);
      audioRef.current.loop = true;
      audioRef.current.play();
      setIsPlaying(true);
    }

    return () => {
      if (audioRef.current) {
        audioRef.current.pause();
        audioRef.current.currentTime = 0;
        setIsPlaying(false);
      }
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
