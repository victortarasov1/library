import Input from '../UI/Input/Input';
import React, {useState, useEffect} from 'react';
import {Button} from 'react-bootstrap';
const AuthorForm = ({setAuthor, author}) => {
  const [name, setName] = useState();
  const [secondName, setSecondName] = useState();
  const createOrUpdate = () =>{
    if(author){

    } else {
      setAuthor({name, secondName})
    }
  }
  useEffect (() =>{
    if(author){
      setName(author.name);
      setSecondName(author.secondName);
    }
  },[author])
  return (
    <div className = "form">
      <Input type = "text" placeholder = "name" value = {name} onChange = {(e) => setName(e.target.value)} />
      <Input type = "text" placeholder = "secondName" value = {secondName} onChange = {(e) => setSecondName(e.target.value)} />
      <Button onClick = {()=> createOrUpdate()}> {author?("change"):("add")} </Button>
    </div>
  )
};
export default AuthorForm;
