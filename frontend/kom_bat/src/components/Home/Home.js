import './Home.css'
import How_to_play from './How_to_play.js';
import Start from './Start.js';
import H from './H.js';
const Home = () => {  
    return(
      <div >
        <div className="Home">
        <H/>
        <Start/>
        <How_to_play/>

        </div>
        
      </div>
    );
  };

  export default Home;