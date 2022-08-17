import React, {useState, useEffect} from 'react';
import AuthorService from '../API/AuthorService';
import AuthorsList from './AuthorsList';
const  AuthorsPage = () => {
  const [authors, setAuthors] = useState([]);
  useEffect ( () => {
    fetchAuthors();
  }, [])
  async function fetchAuthors () {
    const response = await AuthorService.getAll();
    setAuthors(response);
  }

  return (
      <AuthorsList authors = {authors} setAuthors = {setAuthors} />
  )
};
export default AuthorsPage;
