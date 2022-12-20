import React, {useState, useEffect} from 'react';
import {Form, Button} from 'react-bootstrap';
import Input from '../UI/Input/Input';
import BookService from '../API/BookService';
import LoginService from '../API/LoginService';

const BookForm = ({tokens, setTokens, book, CreateOrUpdate}) => {
    const [id, setId] = useState('');
    const[title, setTitle] = useState('');
    const [description, setDescription] = useState('');
    const update = (e) => {
        e.preventDefault();
        BookService.change(tokens, id, description, title).then(data => {
            if (data.error_message) {
                LoginService.refresh(tokens).then(refresh => {
                    if (refresh.hasError) {
                        alert("you must relogin")
                    } else {
                        setTokens(refresh, tokens.refresh_token);
                        console.log(refresh);
                        BookService.change(refresh, id, description, title).then(d => {
                            alert("D");
                            validation(d);
                        })
                    }
                });
            } else {
                validation(data);
            }
        })
    };

    const add = (e) => {
        e.preventDefault();
        BookService.save(tokens, description,title).then(data => {
            if (data.error_message) {
                LoginService.refresh(tokens).then(refresh => {
                    if (refresh.hasError) {
                        alert("you must relogin")
                    } else {
                        setTokens(refresh, tokens.refresh_token);
                        console.log(refresh);
                        BookService.save(refresh, description, title).then(d => {
                            validation(d);
                        })
                    }
                    ;
                });
            } else {
                validation(data);
            }
        })
    };

    const validation = (data) => {
        data.errors ? alert(data.errors) : CreateOrUpdate(data);
    };

    useEffect(() => {
        if (book) {
            setId(book.id)
            setDescription(book.description)
            setTitle(book.title)
        }
    }, []);

    return (
        <Form className="form" onSubmit={book ? update : add}>
            <Input type="text" placeholder="title" value={title}
                   onChange={e => setTitle(e.target.value)}/>
            <Input type="text" placeholder="description" value={description}
                   onChange={e => setDescription(e.target.value)}/>
            <Button type="submit"> {book ? "update" : "create"} </Button>
        </Form>
    );
};
export default BookForm;
