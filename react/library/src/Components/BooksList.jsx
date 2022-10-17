import React, {useState, useEffect} from 'react';
import BookService from '../API/BookService';
import BookWithAuthors from './BookWithAuthors';
const BooksList = () => {
  const [books, setBooks] = useState([]);
  async function fetchBooks () {
    const response = await BookService.getAll();
    setBooks(response);
  };
  useEffect( () => {
      fetchBooks();
  }, []);
  return (
    <div>
      {books.length ? (
        <div className ="list">
          {books.map(book =>
            <div>
              <BookWithAuthors book = {book} />
            </div>
          )}
        </div>
      ): (
        <h1> books not found! </h1>
      )}
    </div>
  );
};
export default BooksList;
