import { useEffect, useState } from 'react';
import backgroundMusic from './ตกแต่ง/เสียง/H_sound.mp3';  // Import ไฟล์เสียง

import './Home.css';
import How_to_play from './How_to_play.js';
import Start from './Start.js';
import H from './H.js';

const Home = () => {
  const [isPlaying, setIsPlaying] = useState(false);

  useEffect(() => {
    const audio = new Audio(backgroundMusic);
    audio.loop = true;  // ทำให้เสียงเล่นต่อเนื่อง

    // พยายามเล่นเสียงเมื่อเข้าหน้า
    audio.play().then(() => {
      setIsPlaying(true);
    }).catch((err) => {
      console.error("ไม่สามารถเล่นเสียงได้", err);
    });

    // จัดการเมื่อหน้า unmount
    return () => {
      audio.pause();
      audio.currentTime = 0;
    };
  }, []);

  return (
    <div>
      <div className="Home">
        <div className="logo-container">
          <H />
        </div>
        <Start />
        <How_to_play />
      </div>
    </div>
  );
};

export default Home;
