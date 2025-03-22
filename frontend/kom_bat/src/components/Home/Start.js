import React from "react";
import { useNavigate } from "react-router-dom";
import B1 from './ปุ่ม/B1.png';
import './Start.css';

const Start = () => {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate('/gamemode');
  };

  return (
    <h1> 
      <img 
        className="hover-image" 
        src={B1} 
        alt="Hover Image" 
        width="200" 
        onClick={handleClick}
        style={{cursor: 'pointer'}}
      />
    </h1>
  );
};

export default Start;
