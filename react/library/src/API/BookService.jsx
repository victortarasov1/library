export default class BookService {
  static async getAll(){
      try {
        const response = await fetch('http://localhost:8080/library/books/');
        return await response.json();
      } catch (e) {
        alert(e);
      }
  };

  static async getAuthors(id) {
    try {
      const response = await fetch('http://localhost:8080/library/books/' + id);
      return await response.json();
    } catch (e) {
      alert(e);
    }
  };
  static async change(authorId, id, title, description) {
    try {
      const requestOptions = {
        method: 'PATCH',
        headers: { 'Content-Type' : 'application/json'},
        body: JSON.stringify({id,title, description})
      };
      const response = await fetch ('http://localhost:8080/library/authors/' + authorId + '/books', requestOptions);
      return await response.json();
    } catch (e){
      alert(e);
    }
  };

  static async add(authorId, title, description){
    try{
      const requestOptions = {
        method: 'POST',
        headers: { 'Content-Type' : 'application/json'},
        body: JSON.stringify({title, description})
      };
      const response = await fetch ('http://localhost:8080/library/authors/' + authorId, requestOptions);
      return await response.json();
    } catch(e){
      alert(e);
    }
  };

  static async delete(authorId, id){
    try{
         const requestOptions = {
             method: 'DELETE',
         };
         await fetch('http://localhost:8080/library/authors/' + authorId + '/books/' + id, requestOptions);
       } catch(e) {
         alert(e);
       }
  };
  
};
