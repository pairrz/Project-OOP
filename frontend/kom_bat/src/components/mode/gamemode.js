import React from 'react';
import H2 from "./H";
import Mode_1 from "./mode_1";
import Mode_2 from "./mode_2";
import Mode_3 from "./mode_3";
import BackBotton from "../BackBotton/BackBotton"; // เพิ่มปุ่มย้อนกลับเข้ามา
import './gamemode.css'

function gamemode() {
  return(
    <div className="mode">
      <BackBotton /> 
      <H2/>          
      <div className="mode-group">
        <Mode_1/>    
        <Mode_2/>    
        <Mode_3/>    
      </div>
    </div>
  );
}

export default gamemode;
