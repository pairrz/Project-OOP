import B1 from './ปุ่ม/Button_ 3.png';
import './mode_3.css';
import { useNavigate } from 'react-router-dom';

const Mode_3 = () => {
  const navigate = useNavigate();
  return (
    <img
      className="image3"
      src={B1}
      alt="Hover Image"
      width="1000"
      height="auto"
      style={{ cursor: 'pointer' }}
      onClick={() => navigate('/character')}
    />
  );
};

export default Mode_3;