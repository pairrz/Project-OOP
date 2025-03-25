import B1 from './ปุ่ม/Button_ 1.png';
import './mode_1.css';
import { useNavigate } from 'react-router-dom';

const Mode_1 = () => {
    const navigate = useNavigate();

    const handleClick = () => {
        navigate('/waitingroom');
    };

    return (
        <img
            className="image1"
            src={B1}
            alt="Hover Image"
            onClick={handleClick}  // คลิกแล้วไปหน้า Character ทันที
            style={{ cursor: 'pointer' }}
        />
    );
};

export default Mode_1;