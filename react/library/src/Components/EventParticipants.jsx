import React, {useState, useEffect} from 'react';
import EventService from '../API/EventService';
import {Button} from 'react-bootstrap';
import EmailForm from './EmailForm';
import LoginService from '../API/LoginService';
import Loader from '../UI/Loader/Loader';
const EventParticipants = ({tokens, setTokens, id}) => {
    const [participants, setParticipants] = useState([]);
    const [show, setShow] = useState(false);
    const [loading, setLoading] = useState(false);
    useEffect(() => {
        setLoading(true);
        setTimeout(() => {
            fetchParticipants();
            setLoading(false);
        }, 1000);
    }, []);

    async function fetchParticipants() {
        const response = await EventService.getParticipants(tokens, id);
        if (response.error_message) {
            LoginService.refresh(tokens).then(refresh => {
                if (refresh.hasError) {
                    alert("you must relogin")
                } else {
                    setTokens(refresh, tokens.refresh_token);
                    console.log(refresh);
                    EventService.getParticipants(refresh, id).then(d => {
                        setParticipants(d);
                    })

                }
            });
        } else {
            setParticipants(response);
        }
    }

    const update = () => {
        fetchParticipants();
        setShow(false);
    }
    return (
        <div>
            {loading ? (
                <div style={{display: 'flex', justifyContent: 'center'}}>
                    <Loader/>
                </div>
            ) : (
                <div>
                    {
                        participants.length !== 0 ? (
                            <div>
                                {
                                    participants.map(participant =>
                                        <h1> {participant.firstName} </h1>
                                    )
                                }
                            </div>
                        ) : (
                            <h1> this is only your event! </h1>
                        )
                    }
                    <div>
                        {
                            show ? (
                                <EmailForm tokens={tokens} setTokens={setTokens} id={id} update={update}/>
                            ) : (
                                <div>
                                    <Button onClick={() => setShow(true)}> add participant</Button>
                                    <br/>
                                </div>
                            )
                        }
                    </div>
                </div>
            )}
        </div>
    );
};
export default EventParticipants;
