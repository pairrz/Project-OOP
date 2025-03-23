import React, { useEffect, useState } from 'react';
import BackBotton from '../BackBotton/BackBotton';
import './Play.css';
import hexDefault from './รูป/h1.png';
import hexPlayer1 from './รูป/h2.png';
import hexPlayer2 from './รูป/h3.png';

const HEX_ROWS = 8;
const HEX_COLS = 8;

export default function Play() {
  const [grid, setGrid] = useState([]);
  const [currentPlayer, setCurrentPlayer] = useState(1);
  const [buyMode, setBuyMode] = useState(false);
  const [summonMode, setSummonMode] = useState(false);

  useEffect(() => {
    const initialGrid = Array(HEX_ROWS).fill(0).map(() => Array(HEX_COLS).fill(0));
    // ✅ Player 1 มุมบนซ้าย 5 ช่อง
    [[0,0], [0,1], [1,0], [1,1], [2,0]].forEach(([r,c]) => initialGrid[r][c] = 1);
    // ✅ Player 2 มุมล่างขวา 5 ช่อง
    [[7,7], [7,6], [6,7], [6,6], [5,7]].forEach(([r,c]) => initialGrid[r][c] = 2);
    setGrid(initialGrid);
  }, []);

  const handleBuyMode = () => { setBuyMode(true); setSummonMode(false); };
  const handleSummonMode = () => { setSummonMode(true); setBuyMode(false); };
  const handleEndTurn = () => {
    alert('จบเทิร์น Player ' + currentPlayer);
    setCurrentPlayer(currentPlayer === 1 ? 2 : 1);
    setBuyMode(false);
    setSummonMode(false);
  };

  const handleHexClick = (row, col) => {
    if (buyMode) {
      const newGrid = [...grid];
      newGrid[row][col] = currentPlayer;
      setGrid(newGrid);
      setBuyMode(false);
    } else if (summonMode) {
      if (grid[row][col] === currentPlayer) {
        alert('อัญเชิญตัวละครสำเร็จที่ช่อง (' + row + ',' + col + ')');
        setSummonMode(false);
      } else {
        alert('อัญเชิญได้เฉพาะช่องฝั่งตัวเอง');
      }
    }
  };

  return (
    <div className="play-container">
      <h1>หน้าสำหรับเล่นเกม (Player {currentPlayer})</h1>

      <div className="hex-grid">
        {grid.map((rowData, row) => (
          <div className={`hex-row ${row % 2 === 0 ? 'even' : ''}`} key={row}>
            {rowData.map((cell, col) => {
              let bgImg = hexDefault;
              if (cell === 1) bgImg = hexPlayer1;
              else if (cell === 2) bgImg = hexPlayer2;
              return (
                <div
                  key={col}
                  className="hex-cell"
                  style={{ backgroundImage: `url(${bgImg})` }}
                  onClick={() => handleHexClick(row, col)}
                ></div>
              );
            })}
          </div>
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
