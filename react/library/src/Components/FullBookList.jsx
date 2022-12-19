import React, {useState, useEffect} from 'react';
import BookService from '../API/BookService';
import {Button, Modal} from 'react-bootstrap';
import Loader from '../UI/Loader/Loader';
import FullBook from "./FullBook";

const FullBookList = () => {
    const [books, setBooks] = useState([]);
    const [loading, setLoading] = useState(false);
    const [open, setOpen] = useState(false);
    useEffect(() => {
        setLoading(true);
        setTimeout(() => {
            fetchBooks();
            setLoading(false);
        }, 1000);
    }, []);

    async function fetchBooks() {
        const response = await BookService.getAll();
        setBooks(response);
    }
    return (
        <div>
            {loading ? (
                <div style={{display: 'flex', justifyContent: 'center'}}>
                    <Loader/>
                </div>
            ) : (
                <div className="list">
                    {books.length ? (
                        <div>
                            {books.map(book =>
                                <div className="item">
                                    <h1> {book.title} </h1>
                                    <Button onClick={() => setOpen(true)}> open </Button>
                                    {
                                        open ? (
                                            <FullBook book={book} setOpen={setOpen} />
                                        ) : (
                                            <br/>
                                        )
                                    }
                                </div>
                            )}
                        </div>
                    ) : (
                        <h1> authors not found! </h1>
                    )}

                </div>
            )}
        </div>
    );
};
export default FullBookList;