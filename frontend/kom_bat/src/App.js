import './App.css';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './components/Home/Home';
import Gamemode from './components/mode/gamemode';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/gamemode" element={<Gamemode />} />
      </Routes>
    </Router>
  );
}

export default App;
