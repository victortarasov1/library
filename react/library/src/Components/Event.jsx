import {Button, Modal} from 'react-bootstrap';
import React, {useState} from 'react';
import EventForm from './EventForm';
import EventParticipants from './EventParticipants';

const Event = ({tokens, setTokens, event, change, remove}) => {
    const [modal, setModal] = useState(false);
    const [participants, setParticipants] = useState(false);
    const update = (data) => {
        setModal(false);
        change(data);
    }
    return (
        <div className="participant_item">
            <h1> {event.description} </h1>
            <h1> {event.startTime} </h1>
            <h1> {event.endTime} </h1>

            <Modal show={modal} onHide={setModal}> <EventForm tokens={tokens} setTokens={setTokens} event={event}
                                                              CreateOrUpdate={update}/> </Modal>
            {
                participants ? (
                    <div>
                        <EventParticipants tokens={tokens} setTokens={setTokens} id={event.id}/>
                        <Button variant="dark" onClick={() => setParticipants(false)}> close </Button>
                    </div>
                ) : (
                    <div>
                        <Button onClick={() => setModal(true)}> change </Button>
                        <Button variant="danger" onClick={() => remove(event)}> remove </Button>
                        <Button onClick={() => setParticipants(true)}> participants </Button>
                    </div>
                )
            }
        </div>
    );
};
export default Event;
