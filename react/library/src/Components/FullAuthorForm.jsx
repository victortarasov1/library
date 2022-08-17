import {Button} from 'react-bootstrap';
import Input from '../UI/Input/Input';
import React, {useState, useEffect} from 'react';
import AuthorService from '../API/AuthorService';
/*import TitleForm from './TitleForm';*/
import TitleList from './TitleList';
const FullAuthorForm = ({createOrUpdate, author}) => {
  const [name, setName] = useState();
  const [secondName, setSecondName] = useState();
  const [age, setAge] = useState();
  const [books, setBooks] = useState([]);
  const addTitle = (title) => {
    //e.preventDefault();

    setBooks([...books, {title: title}])
  }
  const removeTitle = (book) => {
      setBooks(books.filter(b => b.title !== book.title));
  }
  const addOrChangeAuthor = (e) => {
    //e.preventDefault();
    if(!author){
      AuthorService.addNewAuthor(name, secondName, age, books).then(data => {validation(data)});
    } else {
      const id = author.id;
      AuthorService.changeAuthor(id, name, secondName, age, books).then(data => {validation(data)});
    }
  }
  const validation = (data) =>{
    if(data.errors){
      alert(data.errors);
    } else {
      const newId = data.id;
      createOrUpdate({id: newId, name, secondName});
    }
  }

useEffect (() => {
  if(author){
    setName(author.name);
    setSecondName(author.secondName);
    setAge(author.age);
    setBooks(author.books);
  }
},[author])
  return (
      <div className = "form">
        <Input type = "text" placeholder = "first name" value = {name} onChange = { (e) => setName(e.target.value)} />
        <Input type = "text" placeholder = "second name" value = {secondName} onChange = { (e) => setSecondName(e.target.value)} />
        <Input type = "number" placeholder = "age" value = {age} onChange = { (e) => setAge(e.target.value)} />
        <TitleList titles = {books} create = {addTitle} remove = {removeTitle}/>
        <div style = {{textAlign: 'right'}}>
          <Button onClick = {() => addOrChangeAuthor()}> {author ? ("change") : ("create")} </Button>
        </div>
      </div>
  )
};
export default FullAuthorForm;
