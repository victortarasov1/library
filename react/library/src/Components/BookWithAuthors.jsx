import React, {useState, useEffect} from 'react';
import {Button} from 'react-bootstrap';
import BookService from '../API/BookService';
const BookWithAuthors = ({book}) => {
  const [show, setShow] = useState('');
  const[authors, setAuthors] = useState([]);
  async function fetchAuthors () {
    const response = await BookService.getAuthors(book.id);
    setAuthors(response);
  };

  return (
    <div className = "item">
      <h1> {book.title} </h1>
      {show ? (
        <div>
          <h1> {book.description} </h1>
          <Button variant = "dark" onClick = {() => setShow(false)}> close </Button>
          <div className = "list">
            {authors.map(author =>
              <div className = "item">
                <h1> {author.name} </h1>
                <h1> {author.secondName} </h1>
                <hr/>
              </div>
            )}
          </div>
        </div>
      ):(
        <div>

          <Button onClick = {() => {setShow(true); fetchAuthors()}}> open </Button>
          <hr/>
        </div>
      )}

    </div>
  );
};
export default BookWithAuthors;
