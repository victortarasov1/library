import {Button, Modal} from 'react-bootstrap';
import React, {useState} from 'react';
import BookForm from './BookForm';
import BookAuthors from './BookAuthors';

const Book = ({tokens, setTokens, book, change, remove}) => {
    const [modal, setModal] = useState(false);
    const [authors, setAuthors] = useState(false);
    const update = (data) => {
        setModal(false);
        change(data);
    }
    return (
        <div className="item">
            <h1> {book.description} </h1>
            <h1> {book.title} </h1>

            <Modal show={modal} onHide={setModal}> <BookForm tokens={tokens} setTokens={setTokens}  book={book}
                                                             CreateOrUpdate={update}/> </Modal>
            {
                authors ? (
                    <div>
                        <BookAuthors tokens={tokens} setTokens={setTokens} id={book.id}/>
                        <Button variant="dark" onClick={() => setAuthors(false)}> close </Button>
                    </div>
                ) : (
                    <div>
                        <Button onClick={() => setModal(true)}> change </Button>
                        <Button variant="danger" onClick={() => remove(book)}> remove </Button>
                        <Button onClick={() => setAuthors(true)}> authors </Button>
                    </div>
                )
            }
        </div>
    );
};
export default Book;
