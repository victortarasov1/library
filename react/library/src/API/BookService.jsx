export default class BookService {
  static async deleteById (id){
    const requestOptions = {
        method: 'DELETE'
    };
    const response = await fetch('http://localhost:8080/library/books/' + id, requestOptions);
    const data = await response.json();
    return data;
  }
  static async getBookById(id){
    try{
      const response = await fetch ('http://localhost:8080/library/books/' + id);
      const data = await response.json();
      return data;

    } catch (e) {
      alert(e);
    }
  }
  static async addNewBook(title, description, author){
    const requestOptions = {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({title, description, author})
    };

    const response = await fetch('http://localhost:8080/library/books', requestOptions);
    const data = await response.json();
    return data;

  }
  static async changeBook (id, title, description, author){
    const requestOptions = {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({id, title, description, author})
    };
    const response = await fetch('http://localhost:8080/library/books', requestOptions);
    const data = await response.json();
    return data;
  }

  static async findAll(){
    try{
      const response = await fetch ('http://localhost:8080/library/books/');
      const data = await response.json();
      console.log(data);
      return data;

    } catch (e) {
      alert(e);
    }
  }
}
