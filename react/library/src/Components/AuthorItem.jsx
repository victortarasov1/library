import React, {useState} from 'react';
import {Button, Modal} from 'react-bootstrap';
import AuthorForm from './AuthorForm';
import AuthorBooksList from './AuthorBooksList';
const AuthorItem = ({author, update, remove}) => {
  const[show, setShow] = useState(false);
  const[modal, setModal] = useState(false);
  const change = (data) => {
    setModal(false);
    update(data);
  };
  return (
      <div className="item">
        <h1> {author.name}</h1>
        <h1> {author.secondName}</h1>
        <div>
          {show ? (
            <div>
              <div>
                <h1> {author.age} </h1>
                <AuthorBooksList id = {author.id} />
              </div>
              <div>
                <Button variant = "dark" onClick = {() => setShow(false)}> close </Button>
                <Button onClick = {() => setModal(true)}> change </Button>
                <Modal show = {modal} onHide = {setModal}> <AuthorForm author = {author} createOrupdate = {change}/> </Modal>
              </div>
            </div>
            ) :(
              <div>
                <hr/>
                <Button onClick = {() => setShow(true)}> open </Button>
                <Button variant = "danger" onClick = {() => remove(author)}> remove </Button>
              </div>
            )
          }
        </div>
      </div>
  );
};
export default AuthorItem;
