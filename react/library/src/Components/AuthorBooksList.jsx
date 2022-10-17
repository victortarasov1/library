import React, {useState, useEffect} from 'react';
import {Button, Modal} from 'react-bootstrap';
import AuthorService from '../API/AuthorService';
import BookService from '../API/BookService';
import BookItem from './BookItem';
import BookForm from './BookForm';
const AuthorBooksList = ({id}) => {
  const [books, setBooks] = useState([]);
  const [modal, setModal] = useState(false);
  const update = (book) => {
    setBooks([...books.filter(b => b.id !== book.id), book]);
  };
  const create = (book) => {
    setBooks([...books, book]);
    setModal(false);
  };

  const remove = (book) => {
    BookService.delete(id, book.id);
    setBooks([...books.filter(b => b.id!== book.id)]);
  };

  async function fetchBooks () {
    const response = await AuthorService.getBook(id);
    setBooks(response);
  };
  useEffect( () => {
      fetchBooks();
  }, []);
  return (
    <div>
      <div>
        { books.length ? (
          <div className = "list">
            {books.map( book =>
              <div>
                <BookItem book = {book} update = {update} remove = {remove}/>
              </div>
            )}
          </div>
        ):(
          <div>
            <h1> books not found! </h1>
          </div>
        )}
      </div>
      <div>
        <Button onClick = {()=> setModal(true)}> add </Button>
        <Modal show = {modal} onHide = {setModal}> <BookForm createOrupdate = {create} id = {id} /> </Modal>
      </div>
    </div>
  );
};
export default AuthorBooksList;
