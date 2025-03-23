import React, { useEffect, useState, useRef } from 'react';
import BackBotton from '../BackBotton/BackBotton';
import './Play.css';
import axios from 'axios';
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
  const socketRef = useRef(null);

  // ✅ ดึงข้อมูลเริ่มเกมจาก backend
  useEffect(() => {
    axios.get('http://localhost:8080/api/init')
        .then(response => {
          console.log('Initial State:', response.data);
          // สามารถ map response มาเติม grid ได้
        })
        .catch(error => {
          console.error('Error fetching initial state:', error);
        });
  }, []);

  // ✅ WebSocket
  useEffect(() => {
    socketRef.current = new WebSocket('ws://localhost:8080/ws/game');
    socketRef.current.onopen = () => console.log('WebSocket Connected');
    socketRef.current.onmessage = (event) => {
      const updatedState = JSON.parse(event.data);
      console.log('WebSocket Update:', updatedState);
      // อาจ update grid หรือ player ตาม backend
    };
    socketRef.current.onclose = () => console.log('WebSocket Disconnected');
    return () => socketRef.current.close();
  }, []);

  // ✅ สร้าง Hex Grid เริ่มต้น
  useEffect(() => {
    const initialGrid = Array(HEX_ROWS).fill(0).map(() => Array(HEX_COLS).fill(0));
    [[0,0], [0,1], [1,0], [1,1], [2,0]].forEach(([r,c]) => initialGrid[r][c] = 1);
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

      // ✅ ส่ง WebSocket แจ้งฝั่ง backend ด้วย
      const action = { type: 'BUY_HEX', playerId: currentPlayer, targetHex: { x: row, y: col } };
      if (socketRef.current && socketRef.current.readyState === WebSocket.OPEN) {
        socketRef.current.send(JSON.stringify(action));
      }
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
