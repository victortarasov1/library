import React, {useEffect, useState} from 'react';
import AuthorService from '../API/AuthorService';
import {Button, Modal} from 'react-bootstrap';
import AuthorItem from './AuthorItem';
import AuthorForm from './AuthorForm';
import Loader from '../UI/Loader/Loader';
const AuthorsList = () => {
  const [authors, setAuthors] = useState([]);
  const [modal, setModal] = useState(false);
  const [loading, setLoading] = useState(false);
  const update = (author) => {
    setAuthors([...authors.filter(a => a.id !== author.id), author]);
  };
  const create = (author) => {
    setAuthors([...authors, author]);
    setModal(false);
  };

  const remove = (author) => {
    AuthorService.delete(author.id);
    setAuthors([...authors.filter(a => a.id !== author.id)]);
  };
  async function fetchAuthors () {
    const response = await AuthorService.getAll();
    setAuthors(response);
    console.log("authors");
    console.log(authors);

  };
  useEffect( () => {
    setLoading(true);
    setTimeout ( () => {
      fetchAuthors();
      setLoading(false);
    }, 1000);
  }, []);
  return (
      <div>
      {loading ? (
            <div style = {{display: 'flex', justifyContent: 'center'}}>
              <Loader />
            </div>
        ) : (
            <div>
              <div>
                {authors.length ? (
                    <div className="list">
                      {authors.map(author =>
                          <AuthorItem author={author} update={update} remove={remove}/>
                      )}
                    </div>
                ) : (
                    <div>
                      <h1> authors not found! </h1>
                    </div>
                )
                }
              </div>
              <div>
                <Button variant="primary" onClick={() => setModal(true)}> add </Button>
                <Modal show={modal} onHide={setModal}> <AuthorForm createOrupdate={create}/> </Modal>
              </div>
            </div>
        )
      }
      </div>

  );
};
export default AuthorsList;
