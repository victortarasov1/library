import React, {useState, useEffect} from 'react';
import BookService from '../API/BookService';
import BookWithAuthors from './BookWithAuthors';
import Loader from '../UI/Loader/Loader';
const BooksList = () => {
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(false);
  async function fetchBooks () {
    const response = await BookService.getAll();
    setBooks(response);
  };
  useEffect( () => {
    setLoading(true);
    setTimeout ( () => {
      fetchBooks();
      setLoading(false);
    }, 1000);
  }, []);
  return (
      <div>
        {loading ? (
            <div style = {{display: 'flex', justifyContent: 'center'}}>
              <Loader />
            </div>
            ) :(
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
            )}
      </div>
  );
};
export default BooksList;
