export default class BookService {
    static async getAll() {
        try {
            const response = await fetch('http://localhost:8080/library/books');
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
    static async get(tokens) {
        try {
            const requestOptions = {
                method: 'GET',
                mode: 'cors',
                headers: {
                    'Authorization': 'Bearer ' + tokens.access_token
                }
            };
            const response = await fetch('http://localhost:8080/library/authors/books', requestOptions);
            return await response.json();
        } catch (e) {
            alert(e);
        }
    };

    static async remove(tokens, id) {
        try {
            const requestOptions = {
                method: 'DELETE',
                mode: 'cors',
                headers: {
                    'Authorization': 'Bearer ' + tokens.access_token
                }
            };
            const response = await fetch('http://localhost:8080/library/books/' + id, requestOptions);
            if (response.status !== 200) {
                return {hasError: true}
            }
        } catch (e) {
            alert(e);
        }
    };

    static async change(tokens, id, description, title) {
        try {
            const requestOptions = {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + tokens.access_token
                },
                body: JSON.stringify({id, title, description})
            }
            const response = await fetch('http://localhost:8080/library/books', requestOptions);
            return await response.json();
        } catch (e) {
            alert(e)
        }
    };


    static async save(tokens, description, title) {
        try {
            const requestOptions = {
                method: 'POST',
                mode: 'cors',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + tokens.access_token
                },
                body: JSON.stringify({description, title})
            }
            const response = await fetch('http://localhost:8080/library/books', requestOptions);
            return await response.json();
        } catch (e) {
            alert(e)
        }
    };

    static async getAnotherAuthors(tokens, id) {
        try {
            const response = await fetch('http://localhost:8080/library/books/authors/' + id, {
                method: 'GET',
                mode: 'cors',
                headers: {
                    'Authorization': 'Bearer ' + tokens.access_token
                }
            });
            return await response.json();
        } catch (e) {
            alert(e);
        }
    };

    static async addAuthor(tokens, id, email) {
        try {
            const requestOptions = {
                method: 'POST',
                mode: 'cors',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + tokens.access_token
                },
                body: JSON.stringify({
                    "email": email
                })
            }
            const response = await fetch('http://localhost:8080/library/books/' + id, requestOptions);
            if(response.status !==200) {
                return await response.json();
            } else {
                return response;
            }
        } catch (e) {
            alert(e);
        }
    };

}
