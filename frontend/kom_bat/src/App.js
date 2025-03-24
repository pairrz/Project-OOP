import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Home from './components/Home/Home.js';
import Gamemode from './components/mode/gamemode.js';
import Character from './components/Character/Character.js';
import Select from './components/Select/Select.js';
import Play from './components/Play/Play';
/*import WaitingRoom from './components/WaitingRoom/WaitingRoom';
import RoomSelection from './components/RoomSelection/RoomSelection';
import JoinRoom from './components/JoinRoom/JoinRoom';*/



function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/gamemode" element={<Gamemode />} />
        
        
        <Route path="/character" element={<Character />} />
        <Route path="/select" element={<Select />} />
        <Route path="/play" element={<Play />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
/*<Route path="/waitingroom" element={<WaitingRoom />} />
<Route path="/roomselection" element={<RoomSelection />} />
<Route path="/joinroom" element={<JoinRoom />} />*/
