import React, {useState, useEffect} from 'react';
import {Button, Form} from 'react-bootstrap';
import Input from '../UI/Input/Input';
import AuthorService from '../API/AuthorService';
const AuthorForm = ({createOrupdate, author}) => {
  const[name, setName] = useState('');
  const[secondName, setSecondName] = useState('');
  const[age, setAge] = useState('');
  const create = (e) => {
    e.preventDefault();
    const data = AuthorService.add(name, secondName, age).then(data => {
      validation(data);
    });
  };
  const update = (e) => {
    e.preventDefault();
    const data = AuthorService.change(author.id, name, secondName, age).then(data => {
      validation(data);
    });
  };
  const validation = (data) => {
   data.errors ? alert(data.errors) : createOrupdate(data);
 };
  useEffect ( () => {
     if(author){
       setName(author.name);
       setSecondName(author.secondName);
       setAge(author.age);
     }
   },[author]);
  return (
    <Form className = "form" onSubmit = {author ? update : create}>
      <Input type= "text" placeHolder = "name" value = {name} onChange = {(e) => setName(e.target.value)} />
      <Input type= "text" placeHolder = "secondName" value = {secondName} onChange = {(e) => setSecondName(e.target.value)} />
      <Input type= "number" placeHolder = "age" value = {age} onChange = {(e) => setAge(e.target.value)} />
      <Button type = "submit" > { author ? "update" : "create"} </Button>
    </Form>
  );
};
export default AuthorForm;
