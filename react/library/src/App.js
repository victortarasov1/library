import './App.css';

import 'bootstrap/dist/css/bootstrap.min.css';
import {Routes, Route} from 'react-router-dom';
import AuthorsList from './Components/AuthorsList';
import BooksList from './Components/BookList';
function App() {
  return (
    <div>
      <header className = "App-header">
        <a href="/authors"> authors </a>
        <a href="/books"> books </a>
      </header>
      <Routes>
        <Route path = "/authors" element = { <AuthorsList />} />
        <Route path = "/books" element = { <BooksList />} />
        <Route path = "/" element = { <BooksList />} />
      </Routes>
    </div>
  );
}

export default App;
