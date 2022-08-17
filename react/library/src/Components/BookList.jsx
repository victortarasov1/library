import BookItem from './BookItem';
import BookService from '../API/BookService';
import FullBookForm from './FullBookForm';
import React, {useState} from 'react';
import {Modal, Button} from 'react-bootstrap';
const BookList = ({books, setBooks}) => {
  const [modal, setModal] = useState(false);
  const addBook = (book) =>{
    setBooks([...books, book]);
    setModal(false);
  }
  const deleteBook = (book) => {
    BookService.deleteById(book.id);
    setBooks(books.filter(b => b.id !== book.id));
  }
  const changeBook = (book) =>{
    setBooks([...books.filter(b => b.id !== book.id), book]);
  }
  return (
    <div className = "list">
      {books.length? (
        <div>
          { books.map( book =>
            <div>
              <BookItem book = {book} remove = {deleteBook} update = {changeBook}/>
            </div>
          )}
        </div>
      ):(
        <h1> Books not found! </h1>
      )}
      <div>
        <Button onClick = {() => setModal(true)}> add </Button>
        <Modal show = {modal} onHide = {setModal}> <FullBookForm createOrUpdate = {addBook}/> </Modal>
      </div>
    </div>
  )
};
export default BookList;
