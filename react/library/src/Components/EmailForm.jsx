import {Button, Form} from 'react-bootstrap';
import React, {useState} from 'react';
import Input from '../UI/Input/Input';
import EventService from '../API/EventService';
import LoginService from '../API/LoginService';

const EmailForm = ({tokens, setTokens, id, update}) => {
    const [email, setEmail] = useState('');
    const add = (e) => {
        e.preventDefault();
        EventService.addParticipant(tokens, id, email).then(data => {
            if (data.error_message) {
                LoginService.refresh(tokens).then(refresh => {
                    if (refresh.hasError) {
                        alert("you must relogin")
                    } else {
                        setTokens(refresh, tokens.refresh_token);
                        console.log(refresh);
                        EventService.addParticipant(refresh, id, email).then(d => {
                            validation(d);
                        })
                    }

                });
            } else {
                validation(data);
            }
        });
    };

    const validation = (data) => {
        data.errors ? alert(data.errors) : update();
    }
    return (
        <Form className="form" onSubmit={add}>
            <Input type="text" placeholder="email" value={email} onChange={e => setEmail(e.target.value)}/>
            <Button type="submit"> {"add"} </Button>
        </Form>
    );
};
export default EmailForm;
