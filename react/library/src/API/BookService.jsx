export default class BookService {
  static async getAll(){
      try {
        const response = await fetch('http://localhost:8080/library/books/');
        const data = await response.json();
        return data;
      } catch (e) {
        alert(e);
      }
  };

  static async getAuthors(id) {
    try {
      const response = await fetch('http://localhost:8080/library/books/' + id);
      const data = await response.json();
      return data;
    } catch (e) {
      alert(e);
    }
  };
  static async change(id, title, description) {
    try {
      const requestOptions = {
        method: 'PATCH',
        headers: { 'Content-Type' : 'application/json'},
        body: JSON.stringify({id,title, description})
      };
      const response = await fetch ('http://localhost:8080/library/books', requestOptions);
      const data = await response.json();
      return data;
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
      const data = await response.json();
      return data;
    } catch(e){
      alert(e);
    }
  };

  static async delete(authorId, id){
    try{
         const requestOptions = {
             method: 'DELETE',
         };

         const response = await fetch('http://localhost:8080/library/authors/' + authorId + '/books/' + id, requestOptions);

       } catch(e) {
         alert(e);
       }
  };
  
};
