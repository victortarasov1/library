import React, {useState, useEffect} from 'react';
import BookService from '../API/BookService';
import Loader from '../UI/Loader/Loader';
import FullBook from "./FullBook";

const FullBookList = () => {
    const [books, setBooks] = useState([]);
    const [loading, setLoading] = useState(false);
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
                                <FullBook book={book} />
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