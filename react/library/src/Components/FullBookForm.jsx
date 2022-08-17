import Input from '../UI/Input/Input';
import React, {useState, useEffect} from 'react';
import BookService from '../API/BookService';
import {Button, Modal} from 'react-bootstrap';
import AuthorForm from './AuthorForm';
const FullBookForm = ({book, createOrUpdate}) => {
  const [title, setTitle] = useState();
  const [description, setDescription] = useState();
  const [name, setName] = useState();
  const [secondName, setSecondName] = useState();
  const [modal, setModal] = useState(false);
  const validation = (data) =>{
    if(data.errors){
      alert(data.errors);
    } else {
      const newId = data.id;
      createOrUpdate({id: newId, title, description});
    }
  }
  const setAuthor = (author) =>{
    setName(author.name);
    setSecondName(author.secondName);
    setModal(false);
  }
  const addOrChangeBook = () =>{
    if(!book){
      const author = {name, secondName};
      BookService.addNewBook(title, description, author).then(data => {validation(data)});
    } else {
      const authorId = book.author.id;
      const author = {authorId, name, secondName};
      BookService.changeBook(book.id, title, description,author).then(data => {validation(data)});
    }
  }
  useEffect ( () => {
    if(book){
      setTitle(book.title);
      setDescription(book.description);
      setName(book.author.name);
      setSecondName(book.author.secondName);
    }
  },[book])
  return (
    <div className = "form">
      <Input type = "text" placeholder = "title" value = {title} onChange = {(e) => setTitle(e.target.value)} />
      <Input type = "text" placeholder = "description" value = {description} onChange = {(e) => setDescription(e.target.value)} />
      <h2> {name} </h2>
      <h2> {secondName} </h2>
      <div style = {{textAlign: 'right'}}>
        <Button onClick = {() => setModal(true)}> add author </Button>
        <Button  onClick = {() => addOrChangeBook()}> {book ? ("change") : ("create")} </Button>
      </div>
      <Modal className = "modal" show = {modal} onHide = {setModal} > <AuthorForm setAuthor = {setAuthor}/></Modal>
    </div>
  )
};
export default FullBookForm;
