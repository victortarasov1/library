import Input from '../UI/Input/Input';
import {Button} from 'react-bootstrap';
import React, {useState} from 'react';
import AuthorService from '../API/AuthorService';
const TitleForm = ({create, authorId}) => {
  const[title,setTitle] = useState();
  const validation = (data) =>{
    if(data.errors){
      alert(data.errors);
    } else {
      //const newId = data.id;
      create(data);
    }
  }
  const addTitle = () => {
    //e.preventDefault();
    if(authorId){

        AuthorService.addNewTitle(authorId,title).then(data => {validation(data)});
    } else {
        create(title)

    }
  }
  return (
    <div className = "form">

          <Input type = "text" placeholder = "title" value = {title} onChange = { (e) => setTitle(e.target.value)} />
          <div style = {{textAlign: 'left'}}>
            <Button onClick = {() => addTitle()}> add </Button>
          </div>
    </div>
  )
};
export default TitleForm;
