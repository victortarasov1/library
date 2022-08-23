import React, {useState, useEffect} from 'react';
import AuthorItem from './AuthorItem';
import FullAuthorForm from './FullAuthorForm';
import AuthorService from '../API/AuthorService';
import {Button, Modal} from 'react-bootstrap';
import Loader from '../UI/Loader/Loader';
import {CSSTransition,TransitionGroup} from 'react-transition-group';
const AuthorsList = () => {
  const [authorLoading, setAuthorLoading] = useState(false);
  const [authors, setAuthors] = useState([]);
  useEffect ( () => {
    setAuthorLoading(true);
    setTimeout ( () => {
        fetchAuthors();
        setAuthorLoading(false);
      }, 1000);
  }, []);
  async function fetchAuthors () {
    const response = await AuthorService.getAll();
    setAuthors(response);
  };
  const addAuthor = (author) =>{
    setAuthors([...authors, author]);
    setModal(false);
  };
  const deleteAuthor = (author) => {
    AuthorService.deleteById(author.id);
    setAuthors(authors.filter(a => a.id !== author.id));
  };
  const changeAuthor = (author) => {
    setAuthors([...authors.filter(a => a.id !== author.id), author]);
  };
  const [modal, setModal] = useState(false);
  return (
    <div className = "list">
      {authorLoading?(
        <div style = {{display: 'flex', justifyContent: 'center'}}>
            <Loader />
          </div>
      ):(
      <div>
      <div>
        {authors.length ?(
            <TransitionGroup>
              {authors.map( author =>
                <CSSTransition key = {author.id} timeout = {1000} classNames = "element" >
                  <AuthorItem author = {author} remove = {deleteAuthor} change = {changeAuthor} />
                </CSSTransition>
              )}
            </TransitionGroup>
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
    )}
    </div>
  )
};
export default AuthorsList;
