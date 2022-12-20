import React, {useState, useEffect} from 'react';
import BookService from '../API/BookService';
import {Button, Modal} from 'react-bootstrap';
import Book from './Book';
import LoginService from '../API/LoginService';
import BookForm from './BookForm';
import Loader from '../UI/Loader/Loader';

const EventList = ({tokens, setTokens}) => {
    const [books, setBooks] = useState([]);
    const [modal, setModal] = useState(false);
    const [loading, setLoading] = useState(false);
    useEffect(() => {
        setLoading(true);
        setTimeout(() => {
            fetchBooks();
            setLoading(false);
        }, 1000);
    }, []);

    async function fetchBooks() {
        const response = await BookService.get(tokens);
        if (response.error_message) {
            LoginService.refresh(tokens).then(refresh => {
                if (refresh.hasError) {
                    alert("you must relogin")
                } else {
                    setTokens(refresh, tokens.refresh_token);
                    console.log(refresh);
                    BookService.get(refresh).then(d => {
                        setBooks(d)
                    });
                }
            });
        } else {
            setBooks(response);
        }
    }

    const add = (book) => {
        setBooks([...books, book]);
        setModal(false);
    };

    const change = (book) => {
        setBooks([...books.filter(e => e.id !== book.id), book])
    };

    const remove = (book) => {
        BookService.remove(tokens, book.id).then(data => {
            if (data.hasError) {
                alert("YARRR!")
                LoginService.refresh(tokens).then(refresh => {
                    if (refresh.hasError) {
                        alert("you must relogin")
                    } else {
                        setTokens(refresh, tokens.refresh_token);
                        console.log(refresh);
                        BookService.remove(refresh, book.id);
                    }
                    ;
                });
            }
        })
        setBooks([...books.filter(e => e.id !== book.id)])
    }
    return (
        <div>
            {loading ? (
                <div style={{display: 'flex', justifyContent: 'center'}}>
                    <Loader/>
                </div>
            ) : (
                <div className="list">
                    {
                        books.length ? (
                            <div>
                                {
                                    books.map(book =>
                                        <div>
                                            <Book tokens={tokens} setTokens={setTokens} book={book} change={change}
                                                  remove={remove}/>
                                        </div>
                                    )
                                }
                            </div>
                        ) : (
                            <div>
                                <h1> events not found! </h1>
                            </div>
                        )
                    }
                    <Button onClick={() => setModal(true)}> add book</Button>
                    <Modal show={modal} onHide={setModal}> <BookForm tokens={tokens} setTokens={setTokens}
                                                                     CreateOrUpdate={add}/> </Modal>
                </div>
            )}
        </div>
    );
};
export default EventList;
