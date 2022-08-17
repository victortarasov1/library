import {Button, Modal} from 'react-bootstrap';
import React, {useState} from 'react';
import TitleForm from './TitleForm';
const TitleList = ({titles,remove, create, authorId}) => {
  const [modal, setModal] = useState(false);
  const createTitle = (title) =>{
    create(title);
    setModal(false);
  }
  return(
    <div>
      {titles.length? (
        <div style = {{textAlign: 'center'}}>
          {titles.map( title =>
            <div>
              <h2> {title.title} </h2>
              <Button variant = "danger" onClick ={() => remove(title)}> remove </Button>
            </div>
          )}
        </div>
      ):(
        <h1> Books not found! </h1>
      )}
      <div>
        <Button onClick ={() => setModal(true)}> add </Button>
        <Modal className = "subModal" show = {modal} onHide = {setModal} > <TitleForm create = {createTitle} authorId = {authorId} /> </Modal>
      </div>
    </div>
  )
};
export default TitleList;
