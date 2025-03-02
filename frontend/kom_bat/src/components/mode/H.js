import { useState, useEffect } from 'react';
import logo from './ตกแต่ง/mlogo.png';

const H2= () => {
  const [currentImage, setCurrentImage] = useState(logo);

  useEffect(() => {
    const intervalId = setInterval(() => {
      setCurrentImage((prevImage) => (prevImage === logo ? logo : logo));
    }, 1000);

    return () => clearInterval(intervalId);
  }, []);
console.log('test')
  return (
    <h1>
      <img 
        src={currentImage} 
        width="200" 
      />
    </h1>
  );
};

export default H2;