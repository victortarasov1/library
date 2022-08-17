import './App.css';

import 'bootstrap/dist/css/bootstrap.min.css';
import {Routes, Route} from 'react-router-dom';
import AuthorsPage from './Components/AuthorsPage';
import BooksPage from './Components/BooksPage';
function App() {
  return (
    <div>
      <header className = "App-header">
        <a href="/authors"> authors </a>
        <a href="/books"> books </a>
      </header>
      <Routes>
        <Route path = "/authors" element = { <AuthorsPage />} />
        <Route path = "/books" element = { <BooksPage />} />
        <Route path = "/" element = { <BooksPage />} />
      </Routes>
    </div>
  );
}

export default App;
