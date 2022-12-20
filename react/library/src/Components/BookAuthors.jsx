import React, {useState, useEffect} from 'react';
import BookService from '../API/BookService';
import {Button} from 'react-bootstrap';
import EmailForm from './EmailForm';
import LoginService from '../API/LoginService';
import Loader from '../UI/Loader/Loader';
const BookAuthors = ({tokens, setTokens, id}) => {
    const [authors, setAuthors] = useState([]);
    const [show, setShow] = useState(false);
    const [loading, setLoading] = useState(false);
    useEffect(() => {
        setLoading(true);
        setTimeout(() => {
            fetchAuthors();
            setLoading(false);
        }, 1000);
    }, []);

    async function fetchAuthors() {
        const response = await BookService.getAnotherAuthors(tokens, id);
        if (response.error_message) {
            LoginService.refresh(tokens).then(refresh => {
                if (refresh.hasError) {
                    alert("you must relogin")
                } else {
                    setTokens(refresh, tokens.refresh_token);
                    console.log(refresh);
                    BookService.getAnotherAuthors(refresh, id).then(d => {
                        setAuthors(d);
                    })

                }
            });
        } else {
            setAuthors(response);
        }
    }

    const update = () => {
        fetchAuthors();
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
                        authors.length !== 0 ? (
                            <div>
                                {
                                    authors.map(author =>
                                        <h1> {author.name} </h1>
                                    )
                                }
                            </div>
                        ) : (
                            <h1> this is only your book! </h1>
                        )
                    }
                    <div>
                        {
                            show ? (
                                <EmailForm tokens={tokens} setTokens={setTokens} id={id} update={update}/>
                            ) : (
                                <div>
                                    <Button onClick={() => setShow(true)}> add author</Button>
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
export default BookAuthors;
