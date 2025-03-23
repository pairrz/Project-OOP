import B1 from './ปุ่ม/Button_ 2.png';
import './mode_2.css';
import { useNavigate } from 'react-router-dom';

const Mode_2 = () => {
  const navigate = useNavigate();
  return (
    <img
      className="image2"
      src={B1}
      alt="Hover Image"
      width="1000"
      style={{ cursor: 'pointer' }}
      onClick={() => navigate('/character')}
    />
  );
};

export default Mode_2;