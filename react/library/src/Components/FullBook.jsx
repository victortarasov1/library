import React, {useState, useEffect} from 'react';
import BookService from "../API/BookService";
import {Button} from "react-bootstrap";
import Loader from "../UI/Loader/Loader";
const FullBook = ({book, setOpen}) => {
    const [authors, setAuthors] = useState([]);
    const [loading, setLoading] = useState(false);
    useEffect(() => {
        setLoading(true);
        setTimeout(() => {
            fetchAuthors();
            setLoading(false);
        }, 1000);
    }, []);

    async function fetchAuthors() {
        const response = await BookService.getAuthors(book.id);
        console.log(response)
        setAuthors(response);
        console.log(authors)
    }
    return (
      <div className="item">
          <h1> book.description</h1>
          {loading ? (
              <div style={{display: 'flex', justifyContent: 'center'}}>
                  <Loader/>
              </div>
          ) : (
              <div className= "list">
                  {authors.map(author =>
                          <div>
                            <h1> {author.name} </h1>
                            <h1> {author.secondName} </h1>
                          </div>
                  )}

              </div>
          )
          }
          <Button variant = "danger"  onClick={() => setOpen(false)}> close </Button>
      </div>
    );
};
export default FullBook;