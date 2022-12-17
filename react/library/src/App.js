import './App.css';
import {Routes, Route} from 'react-router-dom';
import AuthorsList from './Components/AuthorsList';
import BooksList from './Components/BooksList';
function App() {
  return (
    <div className="App">
      <header className = "App-header">
        <a href = "/"> authors </a>
        <a href = "/books"> books </a>
      </header>
      <Routes>
        <Route path = "/" element = {  <AuthorsList/>}/>
        <Route path = "/books" element = {  <BooksList/>}/>
      </Routes>
    </div>
  );
}

export default App;
