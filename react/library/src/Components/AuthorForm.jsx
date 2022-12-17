import React, {useState, useEffect} from 'react';
import AuthorService from '../API/AuthorService';
import Input from '../UI/Input/Input';
import {Button, Form} from 'react-bootstrap';
import LoginService from '../API/LoginService';

const AuthorForm = ({author, CreateOrUpdate, tokens, setTokens}) => {
    const [name, setName] = useState('');
    const [secondName, setSecondName] = useState('');
    const [age, setAge] = useState(0);
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    useEffect(() => {
        if (author) {
            setName(author.name);
            setSecondName(author.secondName);
            setEmail(author.email);
            setAge(author.age);
        }
    }, [author]);

    const update = (e) => {
        e.preventDefault();
        AuthorService.change(name, secondName, email, password, age, tokens).then(data => {
            if (data.error_message) {
                LoginService.refresh(tokens).then(refresh => {
                    if (refresh.hasError) {
                        alert("you must relogin")
                    } else {
                        setTokens(refresh, tokens.refresh_token);
                        console.log(refresh);
                        AuthorService.change(name, secondName, email, password, refresh).then(d => {
                            validation(d);
                        })
                    }
                });
            } else {
                validation(data);
            }
        });
    };

    const add = (e) => {
        e.preventDefault();
        AuthorService.save(name, secondName, email, password, age).then(data => {
            validation(data);
        });
    };

    const validation = (data) => {
        data.errors ? alert(data.errors) : CreateOrUpdate(data);
    }

    return (
        <Form className="form" onSubmit={author ? update : add}>
            <Input type="text" placeholder="firstName" value={name} onChange={e => setName(e.target.value)}/>
            <Input type="text" placeholder="lastName" value={secondName} onChange={e => setSecondName(e.target.value)}/>
            <Input type="number" placeholder="age" value={age} onChange={e => setAge(e.target.value)}/>
            <Input type="text" placeholder="email" value={email} onChange={e => setEmail(e.target.value)}/>
            <Input type="password" placeholder="password" value={password} onChange={e => setPassword(e.target.value)}/>
            <Button type="submit"> {author ? "update" : "create"} </Button>
        </Form>
    );
};
export default AuthorForm;
