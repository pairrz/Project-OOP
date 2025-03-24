import React, { useEffect, useState } from 'react';
import BackBotton from '../BackBotton/BackBotton';
import './Play.css';
import hexDefault from './รูป/h1.png';
import hexPlayer1 from './รูป/h3.png';
import hexPlayer2 from './รูป/h2.png';

const HEX_ROWS = 8;
const HEX_COLS = 8;
const HEX_HORIZ = 46;
const HEX_VERT = 62;
const HEX_OFFSET = HEX_HORIZ / 100;

export default function Play() {
  const [grid, setGrid] = useState([]);
  const [currentPlayer, setCurrentPlayer] = useState(1);
  const [buyMode, setBuyMode] = useState(false);
  const [summonMode, setSummonMode] = useState(false);  // เพิ่ม state สำหรับ SummonMode
  const [highlightCells, setHighlightCells] = useState([]);
  const [selectedHex, setSelectedHex] = useState(null);
  const [hasBought, setHasBought] = useState(false);
  const [selectedMinions, setSelectedMinions] = useState([]);  // เก็บมินเนี่ยนที่เลือกจาก Select.js

  useEffect(() => {
    const initialGrid = Array(HEX_ROWS + 2).fill(0).map(() => Array(HEX_COLS + 2).fill(0));

    // Player 1 เริ่มขวาล่าง
    [[8,8], [8,7], [7,8], [7,7], [8,6]].forEach(([r, c]) => initialGrid[r][c] = 1);

    // Player 2 เริ่มซ้ายบน
    [[1,1], [1,2], [2,1], [2,2], [1,3]].forEach(([r, c]) => initialGrid[r][c] = 2);

    setGrid(initialGrid);

        // ดึงข้อมูลตัวละครที่เลือกจาก localStorage
        const selected = JSON.parse(localStorage.getItem('selectedCharacters')) || [];
        const allCharacters = JSON.parse(localStorage.getItem('finalCharacters')) || [];  // เพิ่มการดึงข้อมูลจาก finalCharacters
        setSelectedMinions(allCharacters.filter(char => selected.includes(char.id)));  // ดึงตัวละครจาก finalCharacters ที่มี id ตรงกับ selected
  }, []);

  const findBuyableHexes = () => {
    const buyable = [];
    for (let row = 1; row <= HEX_ROWS; row++) {
      for (let col = 1; col <= HEX_COLS; col++) {
        if (grid[row]?.[col] === currentPlayer) {
          const isEvenCol = col % 2 === 0;
          const directions = isEvenCol
            ? [[-1, 0], [-1, 1], [0, 1], [1, 0], [0, -1], [-1, -1]]
            : [[-1, 0], [0, 1], [1, 1], [1, 0], [1, -1], [0, -1]];

          directions.forEach(([dr, dc]) => {
            const nr = row + dr;
            const nc = col + dc;
            if (nr >= 1 && nr <= HEX_ROWS && nc >= 1 && nc <= HEX_COLS && grid[nr]?.[nc] === 0) {
              buyable.push([nr, nc]);
            }
          });
        }
      }
    }
    const unique = Array.from(new Set(buyable.map(JSON.stringify)), JSON.parse);
    setHighlightCells(unique);
  };

  // ฟังก์ชันสำหรับการเปิด/ปิด BuyMode
  const handleBuyMode = () => {
    if (buyMode) {
      setBuyMode(false);
      setHighlightCells([]);
      setSelectedHex(null);
    } else {
      setBuyMode(true);
      findBuyableHexes();
    }
  };
   // ฟังก์ชันสำหรับการเปิด/ปิด SummonMode
   const handleSummonMode = () => {
    setSummonMode(!summonMode);  // Toggle SummonMode
  };

  const handleHexClick = (row, col) => {
    if (buyMode) {
      const isBuyable = highlightCells.some(([r, c]) => r === row && c === col);
      if (isBuyable) setSelectedHex([row, col]);
    } else if (summonMode) {
      if (grid[row]?.[col] === currentPlayer) {
        alert(`อัญเชิญสำเร็จที่ (${row}, ${col})`);
        setSummonMode(false);
      } else {
        alert('อัญเชิญได้เฉพาะช่องตัวเอง');
      }
    }
  };

  const confirmBuy = () => {
    const [row, col] = selectedHex;
    const newGrid = [...grid];
    newGrid[row][col] = currentPlayer;
    setGrid(newGrid);
    setBuyMode(false);
    setHighlightCells([]);
    setSelectedHex(null);
    setHasBought(true);
  };

 

  const handleEndTurn = () => {
    setCurrentPlayer(currentPlayer === 1 ? 2 : 1);
    setBuyMode(false);
    setSummonMode(false);
    setHighlightCells([]);
    setSelectedHex(null);
    setHasBought(false);
  };

  if (grid.length === 0) return null;

  return (
    <div className="play-container">
      <div className={`player-info player1 ${currentPlayer === 1 ? 'active' : ''}`}>
  <div className={`player1-text ${currentPlayer === 1 ? 'active' : ''}`}>Player 1</div>
  
  <p>วิญญาณ X/X</p>
  <p>💰 XX</p>
  <p>ภพที่ 1/XX</p>
</div>

<div className={`player-info player2 ${currentPlayer === 2 ? 'active' : ''}`}>
  <div className={`player2-text ${currentPlayer === 2 ? 'active' : ''}`}>Player 2</div>
  
  <p>วิญญาณ X/X</p>
  <p>💰 XX</p>
  <p>ภพที่ 1/XX</p>
</div><div className={`player-info player1 ${currentPlayer === 1 ? 'active' : ''}`}>
  <div className={`player1-text ${currentPlayer === 1 ? 'active' : ''}`}>Player 1</div>
  
  <p>วิญญาณ X/X</p>
  <p>💰 XX</p>
  <p>ภพที่ 1/XX</p>
</div>

<div className={`player-info player2 ${currentPlayer === 2 ? 'active' : ''}`}>
  <div className={`player2-text ${currentPlayer === 2 ? 'active' : ''}`}>Player 2</div>
  
  <p>วิญญาณ X/X</p>
  <p>💰 XX</p>
  <p>ภพที่ 1/XX</p>
</div>
  {/* แสดงตัวละครที่เลือกจากหน้า Select.js */}
  {summonMode && (
        <div className="minion-selector">
          <h3>เลือกมินเนี่ยน</h3>
          <div className="minion-images">
            {selectedMinions.map((minion, index) => (
              <img key={index} src={minion.img} alt={minion.name} className="minion-image" />
            ))}
          </div>
        </div>
      )}

      


      <div className="hex-grid" style={{ position: 'relative', width: HEX_COLS * HEX_HORIZ + 100, height: HEX_ROWS * HEX_VERT + 100, margin: '0 auto', transform: 'scale(0.95)' }}>
        {Array.from({ length: HEX_ROWS }, (_, i) => i + 1).map(row =>
          Array.from({ length: HEX_COLS }, (_, j) => j + 1).map(col => {
            let bgImg = hexDefault;
            if (grid[row]?.[col] === 1) bgImg = hexPlayer1;
            else if (grid[row]?.[col] === 2) bgImg = hexPlayer2;

            const left = (col - 1) * HEX_HORIZ + (row % 2 === 1 ? HEX_OFFSET : 0);

            let extraYOffset = 0;
            if (col === 2 || col === 4 || col === 6 || col === 8) extraYOffset = -32;

            const top = (row - 1) * HEX_VERT + extraYOffset;

            const isHighlight = highlightCells.some(([r, c]) => r === row && c === col);

            return (
              <div
                key={`${row}-${col}`}
                className="hex-cell"
                style={{
                  backgroundImage: `url(${bgImg})`,
                  left,
                  top,
                  border: isHighlight ? '2px solid yellow' : 'none',
                  boxShadow: isHighlight ? '0 0 10px yellow' : 'none'
                }}
                onClick={() => handleHexClick(row, col)}
              />
            );
          })
        )}
      </div>

      <div className="button-group">
        <button onClick={handleBuyMode} disabled={hasBought}>Buy Mode</button>
        <button onClick={handleSummonMode}>Summon Mode</button>
        <button onClick={handleEndTurn}>End Turn</button>
        {selectedHex && <button onClick={confirmBuy}>ยืนยันซื้อ</button>}
      </div>
      <BackBotton />
    </div>
  );
}
