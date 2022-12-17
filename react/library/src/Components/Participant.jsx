import React, {useState, useEffect} from 'react';
import AuthorService from '../API/AuthorService';
import {Button, Modal} from 'react-bootstrap';
import AuthorForm from './AuthorForm';
import LoginService from '../API/LoginService';
import EventList from './EventList';
import Loader from "../UI/Loader/Loader";

const Participant = ({tokens, setTokens, setModal}) => {
    const [participant, setParticipant] = useState('');
    const [show, setShow] = useState(false);
    const [events, setEvents] = useState(false);
    const [loading, setLoading] = useState(false);
    useEffect(() => {
        setLoading(true);
        setTimeout(() => {
            fetchParticipant();
            setLoading(false);
        }, 1000);
    }, []);

    const change = (data) => {
        if (data) {
            window.location.reload(false);
        }
    };
    const remove = () => {
        AuthorService.delete(tokens).then(data => {
            console.log(data);
            if (data.hasError) {
                LoginService.refresh(tokens).then(data => {
                    if (data.hasError) {
                        alert("you must relogin")
                    } else {
                        setTokens(data, tokens.refresh_token);
                        AuthorService.delete(data);
                    }
                });
            }
        });
    };

    async function fetchParticipant() {
        const response = await AuthorService.getParticipant(tokens.access_token);
        if (response.hasError) {
            setModal(false);
        }
        setParticipant(response);
    };
    return (
        <div>
            {loading ? (
                <div style={{display: 'flex', justifyContent: 'center'}}>
                    <Loader/>
                </div>
            ) : (
                <div className="participant_item">
                    <div style={{textAlign: 'left'}}>
                        <h1> {participant.firstName} </h1>
                        <h1> {participant.lastName} </h1>
                    </div>
                    <h3> {participant.email} </h3>
                    <div style={{textAlign: 'center'}}>
                        <Modal show={show} onHide={setShow}> <AuthorForm CreateOrUpdate={change}
                                                                         participant={participant}
                                                                         tokens={tokens}
                                                                         setTokens={setTokens}/></Modal>
                        {
                            events ?
                                (
                                    <>
                                        <EventList tokens={tokens} setTokens={setTokens}/>
                                        <Button variant="dark" onClick={() => setEvents(false)}> close </Button>
                                    </>
                                ) : (
                                    <>
                                        <Button onClick={() => setShow(true)}> change </Button>
                                        <Button variant="danger" onClick={() => remove()}> delete </Button>
                                        <Button onClick={() => setEvents(true)}> events </Button>
                                        <br/>
                                    </>
                                )
                        }
                    </div>
                </div>
            )}
        </div>
    );
};
export default Participant;
