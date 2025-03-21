import H2 from "./H";
import Mode_1 from "./mode_1";
import Mode_2 from "./mode_2";
import Mode_3 from "./mode_3";
import './gamemode.css'
function gamemode() {
  return(
    
      <div className="mode" >
        <H2/>
        <Mode_1/>
        <Mode_2/>    
        <Mode_3/>
        <h2>ออก</h2>

      </div>
   
  );
    
  
}

export default gamemode;