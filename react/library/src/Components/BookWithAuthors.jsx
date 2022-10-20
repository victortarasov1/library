import React, {useState} from 'react';
import {Button} from 'react-bootstrap';
import BookService from '../API/BookService';
import Loader from '../UI/Loader/Loader';
const BookWithAuthors = ({book}) => {
  const [show, setShow] = useState('');
  const[authors, setAuthors] = useState([]);
  const [loading, setLoading] = useState(false);
  async function fetchAuthors () {
    const response = await BookService.getAuthors(book.id);
    setAuthors(response);
  };
 const getAuthors = () => {
   setLoading(true);
   setTimeout ( () => {
     fetchAuthors();
     setLoading(false);
   }, 1000);
 }
  return (
    <div className = "item">
      <h1> {book.title} </h1>
      <div>
        {loading ? (
            <div style = {{display: 'flex', justifyContent: 'center'}}>
              <Loader />
            </div>
        ):(
            <div>
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

                    <Button onClick = {() => {setShow(true); getAuthors()}}> open </Button>
                    <hr/>
                  </div>
                )}
              </div>
          )}
      </div>
    </div>
  );
};
export default BookWithAuthors;
