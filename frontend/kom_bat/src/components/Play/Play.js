import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
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
  const location = useLocation();
  const selectedMinions = JSON.parse(localStorage.getItem('selectedMinions')) || []; // รับมินเนี่ยนจาก Select.js
  
  const [grid, setGrid] = useState([]);
  const [currentPlayer, setCurrentPlayer] = useState(1);
  const [buyMode, setBuyMode] = useState(false);
  const [summonMode, setSummonMode] = useState(false);
  const [highlightCells, setHighlightCells] = useState([]);
  const [selectedHex, setSelectedHex] = useState(null);
  const [hasBought, setHasBought] = useState(false);
  const [selectedMinion, setSelectedMinion] = useState(null);

  useEffect(() => {
    const initialGrid = Array(HEX_ROWS + 2).fill(0).map(() => Array(HEX_COLS + 2).fill(0));
    [[8,8], [8,7], [7,8], [7,7], [8,6]].forEach(([r, c]) => initialGrid[r][c] = 1);
    [[1,1], [1,2], [2,1], [2,2], [1,3]].forEach(([r, c]) => initialGrid[r][c] = 2);
    setGrid(initialGrid);
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

  const handleBuyMode = () => {
    if (hasBought) {
      alert('ซื้อได้ 1 ช่องต่อเทิร์น ต้อง End Turn ก่อน');
      return;
    }
    setBuyMode(!buyMode);
    setSummonMode(false);
    setSelectedMinion(null);
    if (!buyMode) findBuyableHexes();
    else setHighlightCells([]);
  };

  const handleSummonMode = () => {
    setSummonMode(true);
    setBuyMode(false);
    setSelectedHex(null);
    setHighlightCells([]);
  };

  const handleSelectMinion = (minion) => {
    setSelectedMinion(minion);
    findSummonableHexes();
  };

  const findSummonableHexes = () => {
    const summonable = [];
    for (let row = 1; row <= HEX_ROWS; row++) {
      for (let col = 1; col <= HEX_COLS; col++) {
        if (grid[row]?.[col] === currentPlayer) {
          summonable.push([row, col]);
        }
      }
    }
    setHighlightCells(summonable);
  };

  const handleHexClick = (row, col) => {
    if (buyMode) {
      const isBuyable = highlightCells.some(([r, c]) => r === row && c === col);
      if (isBuyable) setSelectedHex([row, col]);
    } else if (summonMode && selectedMinion) {
      const isSummonable = highlightCells.some(([r, c]) => r === row && c === col);
      if (!isSummonable) return;
      const newGrid = [...grid];
      newGrid[row][col] = { ...selectedMinion, owner: currentPlayer };
      setGrid(newGrid);
      alert(`${selectedMinion.name} ลงที่ (${row}, ${col}) สำเร็จ`);
      setSummonMode(false);
      setSelectedMinion(null);
      setHighlightCells([]);
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
      <h1>
        <span style={{ color: currentPlayer === 1 ? 'gold' : 'white' }}>Player 1</span> vs
        <span style={{ color: currentPlayer === 2 ? 'gold' : 'white' }}> Player 2</span>
      </h1>

      <div className="hex-grid">
        {Array.from({ length: HEX_ROWS }, (_, i) => i + 1).map(row =>
          Array.from({ length: HEX_COLS }, (_, j) => j + 1).map(col => {
            let bgImg = hexDefault;
            if (grid[row]?.[col] === 1) bgImg = hexPlayer1;
            else if (grid[row]?.[col] === 2) bgImg = hexPlayer2;

            const left = (col - 1) * HEX_HORIZ + (row % 2 === 0 ? HEX_HORIZ / 2 : 0);
            const top = (row - 1) * HEX_VERT;

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

      {/* ✅ กรอบ Player 1 ขวาล่าง */}
      <div className="player-info player1">
        <h3>หมอผี..1..</h3>
        <img src="/img/player1.png" alt="Player 1" />
        <p>วิญญาณ X/X</p>
        <p>💰 XX</p>
        <p>ภพที่ 1/XX</p>
      </div>

      {/* ✅ แสดงมินเนี่ยนจาก Select.js */}
      {summonMode && (
        <div className="summon-panel">
          {selectedMinions.map((minion, index) => (
            <div
              key={index}
              className={`minion-card ${selectedMinion?.name === minion.name ? 'selected' : ''}`}
              onClick={() => handleSelectMinion(minion)}
            >
              <img src={minion.img} alt={minion.name} width="50" />
              <p>{minion.name}</p>
            </div>
          ))}
          <button>OK</button>
        </div>
      )}

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
