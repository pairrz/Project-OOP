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
      <BackBotton /> {/* ปุ่มย้อนกลับใหม่ */}
      <H2/>          {/* ของเก่า */}
      <Mode_1/>      {/* ของเก่า */}
      <Mode_2/>      {/* ของเก่า */}
      <Mode_3/>      {/* ของเก่า */}
    </div>
  );
}

export default gamemode;
