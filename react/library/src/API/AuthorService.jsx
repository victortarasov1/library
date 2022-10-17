export default class AuthorService {
  static async getAll () {
    try {
      const response = await fetch('http://localhost:8080/library/authors');
      const data = await response.json();
      return data;
    } catch (e) {
      alert(e);
    }
  };
  static async getBook (id) {
    try {
      const response = await fetch('http://localhost:8080/library/authors/' + id);
      const data = await response.json();
      return data;
    } catch (e) {
      alert(e);
    }
  };
  static async change(id, name, secondName, age) {
    try {
      const requestOptions = {
        method: 'PATCH',
        headers: { 'Content-Type' : 'application/json'},
        body: JSON.stringify({id, name, secondName, age})
      };
      const response = await fetch ('http://localhost:8080/library/authors', requestOptions);
      const data = await response.json();
      return data;
    } catch (e){
      alert(e);
    }
  };

  static async add(name, secondName, age){
    try{
      const requestOptions = {
        method: 'POST',
        headers: { 'Content-Type' : 'application/json'},
        body: JSON.stringify({ name, secondName, age})
      };
      const response = await fetch ('http://localhost:8080/library/authors', requestOptions);
      const data = await response.json();
      return data;
    } catch(e){
      alert(e);
    }
  };

  static async delete(id){
    try{
         const requestOptions = {
             method: 'DELETE',
         };

         const response = await fetch('http://localhost:8080/library/authors/' + id, requestOptions);

       } catch(e) {
         alert(e);
       }
  };
}
