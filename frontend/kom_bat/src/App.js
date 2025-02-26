import React, { useState, useEffect } from "react";
import './App.css';
import logo from './image/ตกแต่ง/logo.png';
import How_to_play from './components/How_to_play.js';
import Start from './components/Start.js';

const H = () => {
  const [currentImage, setCurrentImage] = useState(logo);

  useEffect(() => {
    const intervalId = setInterval(() => {
      setCurrentImage((prevImage) => (prevImage === logo ? logo : logo));
    }, 1000);

    return () => clearInterval(intervalId);
  }, []);

  return (
    <h1>
      <img 
        src={currentImage} 
        width="1000" 
      />
    </h1>
  );
};

function App() {
  return(
    <body>
      <div >
        <H/>
        <Start/>
        <How_to_play/>
      </div>

    </body>
   
  );
    
  
}

export default App;
