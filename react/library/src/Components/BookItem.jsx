import React, {useState, useEffect} from 'react';
import {Button, Modal} from 'react-bootstrap';
import AuthorsList from './AuthorsList';
import BookForm from './BookForm';
const BookItem = ({book, update, remove}) => {
  const [show, setShow] = useState(false);
  const [modal, setModal] = useState(false);
  const change = (book) => {
    setModal(false);
    update(book);
  }
  return(
    <div className = "item">
      <h1> {book.title}</h1>
      {show ? (
        <div>
          <h2> {book.description} </h2>
          <div>
            <Button onClick = {() =>setModal(true)}> change </Button>
            <Button variant = "dark" onClick = {() => setShow(false)}> close </Button>
            <Modal show = {modal} onHide = {setModal}> <BookForm book = {book}  createOrupdate = {change}/> </Modal>
          </div>
        </div>
      ) :(
        <div>
          <Button onClick = {() => setShow(true)}> open </Button>
          <Button variant = "danger" onClick = {() => remove(book)}> remove </Button>
        </div>
      )
      }
    </div>
  );
};
export default BookItem;
