import './App.css';
import {Routes, Route} from 'react-router-dom';
import {Button, Modal} from 'react-bootstrap';
import React, {useState} from 'react';
import Author from './Components/Author';
import Login from './Components/Login';
import AuthorList from './Components/AuthorList';
import FullBookList from "./Components/FullBookList";
function App() {
    const [login, setLogin] = useState(false);
    const [modal, setModal] = useState(false);
    const [tokens, setTokens] = useState('');
  return (
    <div className="App">
      <header className = "App-header">
        <a href = "/"> authors </a>
          <a href = "/books"> books </a>
          <div style={{textAlign: 'right'}}>
              <Button variant="dark" onClick={() => {
                  tokens ? (window.location.reload(false)) : (setLogin(true))
              }}> {tokens ? ("log out") : ("log in")} </Button>
              {tokens ? (<Button variant="dark" onClick={() => setModal(true)}> home </Button>) : (<></>)}
          </div>
      </header>
      <Routes>
        <Route path = "/" element = {  <AuthorList/>}/>
          <Route path= "/books" element = { <FullBookList/>} />
      </Routes>
        <Modal show={login} onHide={setLogin}>
            <Login setTokens={setTokens} setModal={setLogin}/>
        </Modal>
        <Modal show={modal} onHide={setModal}>
            <Author tokens={tokens} setTokens={setTokens} setModal={setModal}/>
        </Modal>
    </div>
  );
}

export default App;
