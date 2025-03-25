import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
//import BackBotton from '../BackBotton/BackBotton';
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
  const navigate = useNavigate();
  const [grid, setGrid] = useState([]);
  const [minionGrid, setMinionGrid] = useState([]);
  const [currentPlayer, setCurrentPlayer] = useState(1);
  const [buyMode, setBuyMode] = useState(false);
  const [summonMode, setSummonMode] = useState(false);
  const [highlightCells, setHighlightCells] = useState([]);
  const [selectedHex, setSelectedHex] = useState(null);
  const [selectedSummonHex, setSelectedSummonHex] = useState(null);
  const [hasBought, setHasBought] = useState(false);
  const [hasSummoned, setHasSummoned] = useState(false);
  const [selectedMinion, setSelectedMinion] = useState(null);
  const [selectedMinions, setSelectedMinions] = useState([]);
  const [turnCount, setTurnCount] = useState(1);
  const maxTurnLimit = 10;
  const [gameOver, setGameOver] = useState(false);
  const [playerBudget, setPlayerBudget] = useState({ 1: 10000, 2: 10000 });
  const buyCost = 1000;
  const summonCost = 2000;
  const interestRate = 0.05;

  useEffect(() => {
    const initialGrid = Array(HEX_ROWS + 2).fill(0).map(() => Array(HEX_COLS + 2).fill(0));
    const initialMinionGrid = Array(HEX_ROWS + 2).fill(0).map(() => Array(HEX_COLS + 2).fill(null));

    [[8,8], [8,7], [7,8], [7,7], [8,6]].forEach(([r, c]) => initialGrid[r][c] = 1);
    [[1,1], [1,2], [2,1], [2,2], [1,3]].forEach(([r, c]) => initialGrid[r][c] = 2);

    setGrid(initialGrid);
    setMinionGrid(initialMinionGrid);

    const selected = JSON.parse(localStorage.getItem('selectedCharacters')) || [];
    const allCharacters = JSON.parse(localStorage.getItem('finalCharacters')) || [];
    setSelectedMinions(allCharacters.filter(char => selected.includes(char.id)));
  }, []);

  const countMinions = (player) => {
    let count = 0;
    for (let row = 1; row <= HEX_ROWS; row++) {
      for (let col = 1; col <= HEX_COLS; col++) {
        if (minionGrid[row][col] && grid[row][col] === player) count++;
      }
    }
    return count;
  };

  const determineWinner = () => {
    const minionP1 = countMinions(1);
    const minionP2 = countMinions(2);
    if (minionP1 > minionP2) return 'Player 1 ชนะ!';
    if (minionP2 > minionP1) return 'Player 2 ชนะ!';
    if (playerBudget[1] > playerBudget[2]) return 'Player 1 ชนะ!';
    if (playerBudget[2] > playerBudget[1]) return 'Player 2 ชนะ!';
    return 'เสมอ!';
  };

  const findBuyableHexes = () => {
    const buyable = [];
    for (let row = 1; row <= HEX_ROWS; row++) {
      for (let col = 1; col <= HEX_COLS; col++) {
        if (grid[row][col] === currentPlayer) {
          const isEvenCol = col % 2 === 0;
          const directions = isEvenCol
            ? [[-1, 0], [-1, 1], [0, 1], [1, 0], [0, -1], [-1, -1]]
            : [[-1, 0], [0, 1], [1, 1], [1, 0], [1, -1], [0, -1]];
          directions.forEach(([dr, dc]) => {
            const nr = row + dr;
            const nc = col + dc;
            if (nr >= 1 && nr <= HEX_ROWS && nc >= 1 && nc <= HEX_COLS && grid[nr][nc] === 0) {
              buyable.push([nr, nc]);
            }
          });
        }
      }
    }
    setHighlightCells([...new Set(buyable.map(JSON.stringify))].map(JSON.parse));
  };

  const handleBuyMode = () => {
    if (summonMode) {
      setSummonMode(false);
      setHighlightCells([]);
      setSelectedMinion(null);
      setSelectedSummonHex(null);
    }
    setBuyMode(!buyMode);
    if (!buyMode) findBuyableHexes();
    else setHighlightCells([]);
  };

  const handleSummonMode = () => {
    if (buyMode) {
      setBuyMode(false);
      setHighlightCells([]);
      setSelectedHex(null);
    }
    setSummonMode(!summonMode);
    setSelectedMinion(null);
    setHighlightCells([]);
    setSelectedSummonHex(null);
  };

  const handleMinionSelect = (minion) => {
    setSelectedMinion(minion);
    const canPlaceHexes = [];
    for (let row = 1; row <= HEX_ROWS; row++) {
      for (let col = 1; col <= HEX_COLS; col++) {
        if (grid[row][col] === currentPlayer) canPlaceHexes.push([row, col]);
      }
    }
    setHighlightCells(canPlaceHexes);
  };

  const handleHexClick = (row, col) => {
    if (buyMode) {
      if (highlightCells.some(([r, c]) => r === row && c === col)) setSelectedHex([row, col]);
    } else if (summonMode) {
      if (!selectedMinion) return ;
      if (grid[row][col] === currentPlayer && minionGrid[row][col] === null) setSelectedSummonHex([row, col]);
    }
  };

  const confirmBuy = () => {
    if (playerBudget[currentPlayer] >= buyCost) {
      const [row, col] = selectedHex;
      const newGrid = [...grid];
      newGrid[row][col] = currentPlayer;
      setGrid(newGrid);
      setPlayerBudget(prev => ({ ...prev, [currentPlayer]: prev[currentPlayer] - buyCost }));
      setBuyMode(false);
      setHighlightCells([]);
      setSelectedHex(null);
      setHasBought(true); // เปลี่ยนสถานะปุ่มให้เป็นสีเทาเมื่อซื้อแล้ว
    }
  };
  

  const confirmSummon = () => {
    if (playerBudget[currentPlayer] >= summonCost) {
      const [row, col] = selectedSummonHex;
      const newMinionGrid = minionGrid.map(r => [...r]);
      newMinionGrid[row][col] = selectedMinion.id;
      setMinionGrid(newMinionGrid);
      setPlayerBudget(prev => ({ ...prev, [currentPlayer]: prev[currentPlayer] - summonCost }));
      setSummonMode(false);
      setHighlightCells([]);
      setSelectedMinion(null);
      setSelectedSummonHex(null);
      setHasSummoned(true); // เปลี่ยนสถานะปุ่มให้เป็นสีเทาหลังการซัมม่อน
    }
  };
  const handleConfirm = () => {
    if (buyMode && selectedHex) confirmBuy();
    else if (summonMode && selectedSummonHex) confirmSummon();
  };
 
  const handleEndTurn = () => {
    setCurrentPlayer(currentPlayer === 1 ? 2 : 1);
    setBuyMode(false);
    setSummonMode(false);
    setHighlightCells([]);
    setSelectedHex(null);
    setSelectedSummonHex(null);
    setHasBought(false);
    setHasSummoned(false);
    // เปลี่ยนสถานะปุ่มเมื่อขึ้นเทิร์นใหม่
    if (currentPlayer === 2) {
      setPlayerBudget(prev => ({
        1: prev[1] + Math.floor(prev[1] * interestRate),
        2: prev[2] + Math.floor(prev[2] * interestRate),
      }));
      setTurnCount(prev => {
        const next = prev + 1;
        if (next > maxTurnLimit) setGameOver(true);
        return next;
      });
    }
  };

  if (grid.length === 0) return null;

  return (
    <div className="play-container">
      <div className="turn-counter">ภพที่ {turnCount} / {maxTurnLimit}</div>

      <div className={`player-info player1 ${currentPlayer === 1 ? 'active' : ''}`}>
        <div className={`player1-text ${currentPlayer === 1 ? 'active' : ''}`}>Player 1</div>
        <p>วิญญาณ {countMinions(1)} ตัว</p>
        <p>💰 {playerBudget[1]}</p>
      </div>
      <div className={`player-info player2 ${currentPlayer === 2 ? 'active' : ''}`}>
        <div className={`player2-text ${currentPlayer === 2 ? 'active' : ''}`}>Player 2</div>
        <p>วิญญาณ {countMinions(2)} ตัว</p>
        <p>💰 {playerBudget[2]}</p>
      </div>

      {summonMode && (
        <div className="minion-selector">
          <h3>เลือกมินเนี่ยน</h3>
          <div className="minion-images">
            {selectedMinions.map((minion, index) => (
              <img key={index} src={minion.img} alt={minion.name} className="minion-image"
                onClick={() => handleMinionSelect(minion)} />
            ))}
          </div>
        </div>
      )}

      <div className="hex-grid" style={{ position: 'relative', width: HEX_COLS * HEX_HORIZ + 100, height: HEX_ROWS * HEX_VERT + 100, margin: '0 auto', transform: 'scale(0.95)' }}>
        {Array.from({ length: HEX_ROWS }, (_, i) => i + 1).map(row =>
          Array.from({ length: HEX_COLS }, (_, j) => j + 1).map(col => {
            let bgImg = hexDefault;
            if (grid[row][col] === 1) bgImg = hexPlayer1;
            else if (grid[row][col] === 2) bgImg = hexPlayer2;

            const left = (col - 1) * HEX_HORIZ + (row % 2 === 1 ? HEX_OFFSET : 0);
            const extraYOffset = [2, 4, 6, 8].includes(col) ? -32 : 0;
            const top = (row - 1) * HEX_VERT + extraYOffset;
            const isHighlight = highlightCells.some(([r, c]) => r === row && c === col);

            return (
              <div key={`${row}-${col}`} className="hex-cell"
                style={{
                  backgroundImage: `url(${bgImg})`, left, top,
                  border: isHighlight ? '2px solid yellow' : 'none',
                  boxShadow: isHighlight ? '0 0 10px yellow' : 'none', position: 'absolute'
                }}
                onClick={() => handleHexClick(row, col)}>
                {minionGrid[row][col] && (
                  <div className="minion-on-hex">
                    <img src={selectedMinions.find(m => m.id === minionGrid[row][col])?.img} alt="minion" />
                  </div>
                )}
              </div>
            );
          })
        )}
      </div>

      <div className="button-group">
        <button className="buy" onClick={handleBuyMode} disabled={hasBought}></button>
        <button className="summon" onClick={handleSummonMode} disabled={hasSummoned}></button>
        <button className="end-turn" onClick={handleEndTurn}></button>
        {(selectedHex || selectedSummonHex) && 
        <button className="confirm" onClick={handleConfirm}></button>
        }
      </div>

      {/*<div><BackBotton /></div>*/}

      {gameOver && (
        <div className="game-over-popup">
          <div className="popup-content">
            <h2>จบเกม</h2>
            <p>{determineWinner()}</p>
            <button onClick={() => window.location.reload()}>เริ่มใหม่</button>
            <button onClick={() => navigate('/')}>กลับหน้า Home</button>
          </div>
        </div>
      )}
    </div>
  );
}
