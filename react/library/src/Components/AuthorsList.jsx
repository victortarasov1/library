import React, {useState} from 'react';
import AuthorItem from './AuthorItem';
import FullAuthorForm from './FullAuthorForm';
import AuthorService from '../API/AuthorService';
import {Button, Modal} from 'react-bootstrap';
const AuthorsList = ({authors,setAuthors}) => {
  const addAuthor = (author) =>{
    setAuthors([...authors, author]);
    setModal(false);
  }
  const deleteAuthor = (author) => {
    AuthorService.deleteById(author.id);
    setAuthors(authors.filter(a => a.id !== author.id));
  }
  const changeAuthor = (author) => {
    setAuthors([...authors.filter(a => a.id !== author.id), author]);
  }
  const [modal, setModal] = useState(false)
  return (
    <div className = "list">
      <div>
        {authors.length ?(
            <div>
              {authors.map( author =>
                <div>
                  <AuthorItem author = {author} remove = {deleteAuthor} change = {changeAuthor} />
                </div>
              )}
            </div>
          ): (
            <h1> Authors not found! </h1>
          )
        }

      </div>
      <div>
        <Button onClick = {() => setModal(true)}> add new Author </Button>
        <Modal className = "modal" show = {modal} onHide = {setModal}> <FullAuthorForm createOrUpdate = {addAuthor}/> </Modal>
      </div>
    </div>
  )
};
export default AuthorsList;
