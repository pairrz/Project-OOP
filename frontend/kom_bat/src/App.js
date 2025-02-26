import React, { useState, useEffect } from "react";
import './App.css';
import B1 from './image/ปุ่ม/1.png';
import B2 from './image/ปุ่ม/2.png';
import logo from './image/ตกแต่ง/logo.png';
import logo2 from "./image/ตกแต่ง/logo2.png";

const Start = () => (
  <h1>
    <img className="hover-image" src={B1} alt="Hover Image" width="200" />
  </h1>
);


const  How_to_play= () => (
  <h1>
    <img className="hover-image2" src={B2} alt="Hover Image2" width="200" />
  </h1>
);

const H = () => {
  const [currentImage, setCurrentImage] = useState(logo); // กำหนดรูปเริ่มต้นเป็น logo

  useEffect(() => {
    const intervalId = setInterval(() => {
      setCurrentImage((prevImage) => (prevImage === logo ? logo : logo)); // สลับระหว่าง logo และ logo2
    }, 1000); // เปลี่ยนทุกๆ 1 วินาที

    // ล้างการตั้งเวลาเมื่อคอมโพเนนต์ถูกทำลาย
    return () => clearInterval(intervalId);
  }, []); // การใช้ empty array [] จะทำให้ useEffect ทำงานครั้งเดียวเมื่อคอมโพเนนต์ถูก mount

  return (
    <h1>
      <img src={currentImage} width="500" alt="Flashing Image" />
    </h1>
  );
};

function App() {
  return(
    <body>
      <div >
        <H/>
        <br/>
        <Start/>
        <How_to_play/>
      </div>

    </body>
   
  );
    
  
}

export default App;
