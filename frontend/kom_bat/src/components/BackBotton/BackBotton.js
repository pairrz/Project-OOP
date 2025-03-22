import React from 'react';
import { useNavigate } from 'react-router-dom';
import './BackBotton.css';

const BackBotton = () => {
  const navigate = useNavigate();

  const handleBackClick = () => {
    navigate('/');
  };

  return (
    <img
  src={process.env.PUBLIC_URL + '/BB2.png'} 
  alt="Back Button"
  className="back-image-button"
  onClick={handleBackClick}
/>
  );
};

export default BackBotton;
