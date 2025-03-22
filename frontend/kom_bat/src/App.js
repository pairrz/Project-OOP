import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Home from './components/Home/Home.js';
import Gamemode from './components/mode/gamemode.js';
import Character from './components/Character/Character.js';
import Select from './components/Select/Select.js';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/gamemode" element={<Gamemode />} />
        <Route path="/character" element={<Character />} />
        <Route path="/select" element={<Select />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
