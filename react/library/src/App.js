import './App.css';
import {Routes, Route} from 'react-router-dom';
import AuthorList from './Components/AuthorList';
function App() {
  return (
    <div className="App">
      <header className = "App-header">
        <a href = "/"> authors </a>
      </header>
      <Routes>
        <Route path = "/" element = {  <AuthorList/>}/>
      </Routes>
    </div>
  );
}

export default App;
