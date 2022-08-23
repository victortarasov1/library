import React, {useState} from 'react';
import AuthorService from '../API/AuthorService';
import BookService from '../API/BookService';
import {Button,Modal} from 'react-bootstrap';
import FullAuthorForm from './FullAuthorForm';
import Loader from '../UI/Loader/Loader';
import TitleList from './TitleList';
const AuthorItem = ({author, remove, change}) => {
  const [open, setOpen] = useState(false);
  const [age, setAge] = useState();
  const [modal, setModal] = useState(false);
  const [fullAuthor, setFullAuthor] = useState();
  const [fullAuthorLoading, setFullAuthoroading] = useState(false);
  async function fetchAuthorWithBooks (){
    setFullAuthoroading(true);
    const response = await AuthorService.getAuthorById(author.id);
    setAge(response.age);
    //setBooks(response.books);
    setFullAuthor(response);
    setOpen(true);
    setFullAuthoroading(false);
  };
  const deleteBook = (book) => {
    BookService.deleteById(book.id);
    const books = fullAuthor.books.filter(b => b.id !== book.id);
    setFullAuthor({name: author.name,secondName: author.secondName,age: age, books: books});


  }
  const changeAuthor = (newAuthor) => {
    change(newAuthor);
    setModal(false);
    setOpen(false);
  }
  const addBook = (newAuthor) => {
    setFullAuthor(newAuthor);
  }
  return (
    <div className = "item">
      <h1 style = {{textAlign: 'center'}}> {author.name} </h1>
      <h3 style = {{textAlign: 'center'}}> {author.secondName} </h3>
      {fullAuthorLoading? (
        <div style = {{display: 'flex', justifyContent: 'center'}}>
            <Loader />
          </div>
      ):(
        <div>
        {open ?(
          <div>
            <h3 style = {{textAlign: 'right'}}> {age} </h3>
            {/*<BookList books = {books} remove = {deleteBook} change = {change}/>*/}
                <TitleList titles = {fullAuthor.books} remove = {deleteBook} create = {addBook} authorId = {author.id} />
            <div style = {{textAlign: 'right'}}>
              <Button onClick = {() => setModal(true)}> change </Button>
              <Modal className = "modal" show = {modal} onHide = {setModal}> <FullAuthorForm createOrUpdate = {changeAuthor} author = {fullAuthor} /> </Modal>
              <Button variant="dark" onClick = {() => setOpen(false)}> close </Button>
            </div>
          </div>
        ):(
          <div style = {{textAlign: 'center'}}>
            <Button variant="danger" onClick = {() => remove(author)}> remove </Button>
            <Button variant="primary" onClick = {() => fetchAuthorWithBooks()}> open </Button>
          </div>
        )
        }
        </div>
    )}
    </div>
  )
};
export default AuthorItem;
