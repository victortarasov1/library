import {Button, Modal} from 'react-bootstrap';
import React, {useState} from 'react';
import BookService from '../API/BookService';
import FullBookForm from './FullBookForm';
import Loader from '../UI/Loader/Loader';
const BookItem = ({book, remove, update}) =>{
  const[modal, setModal] = useState(false);
  const [open, setOpen] = useState(false);
  const [fullBook, setFullBook] = useState();
  const [fullBookLoading, setFullBookLoading] = useState(false);
  async function fetchBookWithAuthors (){
    setFullBookLoading(true);
    const response = await BookService.getBookById(book.id);
    setFullBook(response);
    setFullBookLoading(false);
    setOpen(true);
  };
  const changeBook = (book) =>{
    update(book);
    setModal(false);
  };
  return(
    <div className = "item">
      <h1> {book.title} </h1>
      {fullBookLoading ? (
        <div style = {{display: 'flex', justifyContent: 'center'}}>
            <Loader />
          </div>
      ):(
      <div>
        {open?(
          <div>
            <div>
              <h3  style = {{textAlign: 'center'}}> {fullBook.description} </h3>
              <h3  style = {{textAlign: 'right'}}> {fullBook.author.name}  {fullBook.author.secondName} </h3>
            </div>
            <div  style = {{textAlign: 'right'}}>
              <Button onClick = {() => setModal(true)}> change </Button>
              <Button variant = "dark" onClick = {() => setOpen(false)}> close </Button>
              <Modal show = { modal} onHide = {setModal}> <FullBookForm book = {fullBook} createOrUpdate = {changeBook}/></Modal>
            </div>
          </div>

        ):(
          <div style = {{textAlign: 'right'}}>
            <Button variant = "danger" onClick = {() => remove(book)}> remove </Button>
            <Button onClick = {() => fetchBookWithAuthors()}> open </Button>
          </div>
        )}
      </div>
      )}
    </div>
  )
};
export default BookItem;
