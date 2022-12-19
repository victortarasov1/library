import React, {useState, useEffect} from 'react';
import BookService from "../API/BookService";
import {Button} from "react-bootstrap";
import Loader from "../UI/Loader/Loader";

const FullBook = ({book}) => {
    const [authors, setAuthors] = useState([]);
    const [loading, setLoading] = useState(false);
    const [open, setOpen] = useState(false);
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
            {loading ? (
                <div style={{display: 'flex', justifyContent: 'center'}}>
                    <Loader/>
                </div>
            ) : (
                <div className="item">
                    <h1> {book.title} </h1>
                    {open ? (
                        <>
                            <h1> {book.description}</h1>
                            <div className="list">

                                {authors.map(author =>
                                    <div className="item">
                                        <h1> {author.name} </h1>
                                        <h1> {author.secondName} </h1>
                                        <hr/>
                                    </div>
                                )}
                                <Button variant="danger" onClick={() => setOpen(false)}> close </Button>
                            </div>
                        </>
                    ) : (
                        <Button onClick={() => setOpen(true)}> open </Button>
                    )}
                </div>
            )
            }

        </div>
    );
};
export default FullBook;