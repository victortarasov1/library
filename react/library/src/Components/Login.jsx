import React, {useState} from 'react';
import {Form, Button} from 'react-bootstrap';
import Input from '../UI/Input/Input';
import LoginService from '../API/LoginService';

const Login = ({setTokens, setModal}) => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const login = (e) => {
        e.preventDefault();
        LoginService.login(email, password).then(data => validation(data));
    };
    const validation = (data) => {
        data.debugMessage ? alert(data.debugMessage) : set(data);
    };
    const set = (data) => {
        setTokens(data);
        setModal(false)
    }
    return (
        <Form className="form" onSubmit={login}>
            <Input type="text" placeholder="email" value={email} onChange={e => setEmail(e.target.value)}/>
            <Input type="password" placeholder="password" value={password} onChange={e => setPassword(e.target.value)}/>
            <Button type="submit"> login </Button>
        </Form>
    );
};
export default Login;
