import { useState, useEffect } from 'react';
import logo from ''

const H = () => {
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
        width="1000" 
      />
    </h1>
  );
};

export default H;