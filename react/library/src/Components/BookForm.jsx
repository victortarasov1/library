import React, {useState, useEffect} from 'react';
import {Button, Form} from 'react-bootstrap';
import Input from '../UI/Input/Input';
import BookService from '../API/BookService';
const BookForm = ({createOrupdate, book, id}) => {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const create = (e) => {
    e.preventDefault();
    const data = BookService.add(id, title, description).then(data => {
      validation(data);
    });
  };
  const update = (e) => {
    e.preventDefault();
    const data = BookService.change(book.id, title, description).then(data => {
      validation(data);
    });
  };
  const validation = (data) => {
   data.errors ? alert(data.errors) : createOrupdate(data);
 };
 useEffect ( () => {
    if(book){
      setTitle(book.title);
      setDescription(book.description);
    }
  },[book]);
  return (
    <Form className = "form" onSubmit = {book ? update : create}>
      <Input type = "text" placeHolder = "title" value = {title} onChange = {(e) => setTitle(e.target.value)} />
      <Input type = "text" placeHolder = "description" value = {description} onChange = {(e) => setDescription(e.target.value)} />
      <Button type = "submit" > { book ? "update" : "create"} </Button>
    </Form>
  );
};
export default BookForm;
