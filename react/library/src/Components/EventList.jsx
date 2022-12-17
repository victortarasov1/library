import React, {useState, useEffect} from 'react';
import EventService from '../API/EventService';
import {Button, Modal} from 'react-bootstrap';
import Event from './Event';
import LoginService from '../API/LoginService';
import EventForm from './EventForm';
import Loader from '../UI/Loader/Loader';

const EventList = ({tokens, setTokens}) => {
    const [events, setEvents] = useState([]);
    const [modal, setModal] = useState(false);
    const [loading, setLoading] = useState(false);
    useEffect(() => {
        setLoading(true);
        setTimeout(() => {
            fetchEvents();
            setLoading(false);
        }, 1000);
    }, []);

    async function fetchEvents() {
        const response = await EventService.get(tokens);
        if (response.error_message) {
            LoginService.refresh(tokens).then(refresh => {
                if (refresh.hasError) {
                    alert("you must relogin")
                } else {
                    setTokens(refresh, tokens.refresh_token);
                    console.log(refresh);
                    EventService.get(refresh).then(d => {
                        setEvents(d)
                    });
                }
            });
        } else {
            setEvents(response);
        }
    }

    const add = (event) => {
        setEvents([...events, event]);
        setModal(false);
    };

    const change = (event) => {
        setEvents([...events.filter(e => e.id !== event.id), event])
    };

    const remove = (event) => {
        EventService.remove(tokens, event.id).then(data => {
            if (data.hasError) {
                alert("YARRR!")
                LoginService.refresh(tokens).then(refresh => {
                    if (refresh.hasError) {
                        alert("you must relogin")
                    } else {
                        setTokens(refresh, tokens.refresh_token);
                        console.log(refresh);
                        EventService.remove(refresh, event.id);
                    }
                    ;
                });
            }
        })
        setEvents([...events.filter(e => e.id !== event.id)])
    }
    return (
        <div>
            {loading ? (
                <div style={{display: 'flex', justifyContent: 'center'}}>
                    <Loader/>
                </div>
            ) : (
                <div className="participant_list">
                    {
                        events.length ? (
                            <div>
                                {
                                    events.map(event =>
                                        <div>
                                            <Event tokens={tokens} setTokens={setTokens} event={event} change={change}
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
                    <Button onClick={() => setModal(true)}> add event</Button>
                    <Modal show={modal} onHide={setModal}> <EventForm tokens={tokens} setTokens={setTokens}
                                                                      CreateOrUpdate={add}/> </Modal>
                </div>
            )}
        </div>
    );
};
export default EventList;
