import React, { useEffect, useState } from 'react';
import BackBotton from '../BackBotton/BackBotton';
import './Play.css';
import hexDefault from './รูป/h1.png';
import hexPlayer1 from './รูป/h2.png';
import hexPlayer2 from './รูป/h3.png';

const HEX_ROWS = 8;
const HEX_COLS = 8;
const HEX_HORIZ = 46;  // ✅ ย่อระยะห่างแนวนอน
const HEX_VERT = 62;   // ✅ ย่อระยะห่างแนวตั้ง
const HEX_OFFSET = HEX_HORIZ / 100;

export default function Play() {
  const [grid, setGrid] = useState([]);
  const [currentPlayer, setCurrentPlayer] = useState(1);
  const [buyMode, setBuyMode] = useState(false);
  const [summonMode, setSummonMode] = useState(false);

  useEffect(() => {
    const initialGrid = Array(HEX_ROWS).fill(0).map(() => Array(HEX_COLS).fill(0));
    // ✅ Player 1 start (1,1) (1,2) (2,1) (2,2) (1,3)
    [[0,0], [0,1], [1,0], [1,1], [0,2]].forEach(([r,c]) => initialGrid[r][c] = 1);
    // ✅ Player 2 start (8,6) (7,7) (7,8) (8,7) (8,8)
    [[7,5], [6,6], [6,7], [7,6], [7,7]].forEach(([r,c]) => initialGrid[r][c] = 2);
    setGrid(initialGrid);
  }, []);

  const handleHexClick = (row, col) => {
    if (buyMode) {
      const newGrid = [...grid];
      newGrid[row][col] = currentPlayer;
      setGrid(newGrid);
      setBuyMode(false);
    } else if (summonMode) {
      if (grid[row][col] === currentPlayer) {
        alert('อัญเชิญสำเร็จที่ (' + (row + 1) + ',' + (col + 1) + ')');
        setSummonMode(false);
      } else {
        alert('อัญเชิญได้เฉพาะช่องตัวเอง');
      }
    }
  };

  const handleBuyMode = () => { setBuyMode(true); setSummonMode(false); };
  const handleSummonMode = () => { setSummonMode(true); setBuyMode(false); };
  const handleEndTurn = () => {
    alert('จบเทิร์น Player ' + currentPlayer);
    setCurrentPlayer(currentPlayer === 1 ? 2 : 1);
    setBuyMode(false);
    setSummonMode(false);
  };

  return (
    <div className="play-container">
      <h1>หน้าสำหรับเล่นเกม (Player {currentPlayer})</h1>

      {/* ✅ ย้าย grid มาตรงกลางด้วย transform */}
      <div className="hex-grid" style={{ position: 'relative', width: HEX_COLS * HEX_HORIZ + 100, height: HEX_ROWS * HEX_VERT + 100, margin: '0 auto', transform: 'scale(0.95)' }}>
        {grid.map((rowData, row) => (
          rowData.map((cell, col) => {
            let bgImg = hexDefault;
            if (cell === 1) bgImg = hexPlayer1;
            else if (cell === 2) bgImg = hexPlayer2;

            const left = col * HEX_HORIZ + (row % 2 === 1 ? HEX_OFFSET : 0);

            // ✅ Offset Y ลดลงให้แน่นขึ้น
            let extraYOffset = 0;
            if (col === 1) extraYOffset = -32;   // col 2
            if (col === 3) extraYOffset = -32;   // col 4
            if (col === 5) extraYOffset = -32;   // col 6
            if (col === 7) extraYOffset = -32;   // col 8

            const top = row * HEX_VERT + extraYOffset;

            return (
              <div
                key={`${row}-${col}`}
                className="hex-cell"
                style={{ backgroundImage: `url(${bgImg})`, left, top }}
                onClick={() => handleHexClick(row, col)}
              >
                <div className="hex-coord">({row + 1},{col + 1})</div>
              </div>
            );
          })
        ))}
      </div>

      <div className="control-panel">
        <button onClick={handleBuyMode}>ซื้อ Hex</button>
        <button onClick={handleSummonMode}>อัญเชิญ</button>
        <button onClick={handleEndTurn}>จบเทิร์น</button>
      </div>

      <BackBotton />
    </div>
  );
}