export default class AuthorService {
  static async getAll(){
      try {
        const response = await fetch ('http://localhost:8080/library/authors');
        const data = await response.json();
        return data;
      } catch (e) {
        alert(e);
      }
  }
  static async getAuthorById(id){
    try{
      const response = await fetch ('http://localhost:8080/library/authors/' + id);
      const data = await response.json();
      return data;
    } catch (e) {
      alert(e);
    }
  }

  static async addNewAuthor(name, secondName, age, books){
    const requestOptions = {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({name, secondName, age, books})
    };

    const response = await fetch('http://localhost:8080/library/authors', requestOptions);
    const data = await response.json();
    return data;

  }

  static async changeAuthor (id, name, secondName, age, books){
    const requestOptions = {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({id, name, secondName, age, books})
    };

    const response = await fetch('http://localhost:8080/library/authors', requestOptions);
    const data = await response.json();
    return data;
  }
  static async deleteById (id){
    const requestOptions = {
        method: 'DELETE'
    };
    const response = await fetch('http://localhost:8080/library/authors/' + id, requestOptions);
    const data = await response.json();
    return data;
  }


  static async addNewTitle(authorId, title){
    const requestOptions = {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({title})
    };
    const response = await fetch('http://localhost:8080/library/authors/'+authorId, requestOptions);
    const data = await response.json();
    return data;

  }
}
//export default AuthorService;
