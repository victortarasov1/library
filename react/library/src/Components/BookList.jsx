import BookItem from './BookItem';
import BookService from '../API/BookService';
import FullBookForm from './FullBookForm';
import Loader from '../UI/Loader/Loader';
import React, {useState, useEffect} from 'react';
import {Modal, Button} from 'react-bootstrap';
const BookList = () => {
  const [books, setBooks] = useState([]);
  const [modal, setModal] = useState(false);
  const [booksLoading, setBooksLoading] = useState(false);
  const addBook = (book) =>{
    setBooks([...books, book]);
    setModal(false);
  }
  useEffect ( () => {
    setBooksLoading(true);
    setTimeout ( () => {
        fetchBooks();
        setBooksLoading(false);
      }, 1000);
  },[]);
  async function fetchBooks () {
    const response = await BookService.findAll();
    setBooks(response);
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
      {booksLoading ?(
        <div style = {{display: 'flex', justifyContent: 'center'}}>
            <Loader />
          </div>
      ):(
      <div>
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
      </div>)
    }
    </div>
  )
};
export default BookList;
