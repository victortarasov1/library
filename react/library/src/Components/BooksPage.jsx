import React, {useState, useEffect} from 'react';
import BookService from '../API/BookService';
import BookList from './BookList';
const BooksPage = () => {
  const [books, setBooks] = useState([]);
  useEffect ( () => {
    fetchBooks();
  },[]);
  async function fetchBooks () {
    const response = await BookService.findAll();
    setBooks(response);
  }
  return (
    <div>
      <BookList books = {books} setBooks = {setBooks}/>
    </div>
  )
};
export default BooksPage;
