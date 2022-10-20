import React, {useState, useEffect} from 'react';
import {Button, Modal} from 'react-bootstrap';
import AuthorService from '../API/AuthorService';
import BookService from '../API/BookService';
import BookItem from './BookItem';
import BookForm from './BookForm';
import Loader from '../UI/Loader/Loader';
const AuthorBooksList = ({id}) => {
  const [books, setBooks] = useState([]);
  const [modal, setModal] = useState(false);
  const [loading, setLoading] = useState(false);
  const update = (book) => {
    fetchBooks();
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
        ):(
          <div>
            <div>
              { books.length ? (
                <div className = "list">
                  {books.map( book =>
                    <div>
                      <BookItem id =  {id} book = {book} update = {update} remove = {remove}/>
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
        )}
      </div>
  );
};
export default AuthorBooksList;
