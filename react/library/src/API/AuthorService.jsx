export default class AuthorService {
    static async getAll() {
        try {
            const response = await fetch('http://localhost:8080/library');
            return await response.json();
        } catch (e) {
            alert(e);
        }
    };

    static async getParticipant(access_token) {
        try {
            const response = await fetch('http://localhost:8080/calendar/user', {
                method: 'GET',
                mode: 'cors',
                headers: {
                    'Authorization': 'Bearer ' + access_token
                }
            });
            if (response.ok) {
                return await response.json();
            }
            alert("you must authorize at first!")
            return {hasError: true}
        } catch (e) {
            alert(e);
        }
    };

    static async delete(tokens) {
        try {
            const requestOptions = {
                method: 'DELETE',
                mode: 'cors',
                headers: {
                    'Authorization': 'Bearer ' + tokens.access_token
                }
            };
            const response = await fetch('http://localhost:8080/calendar/user', requestOptions);
            if (response.status !== 200) {
                return {hasError: true}
            }
        } catch (e) {
            alert(e);
        }
        window.location.reload(false);
    };

    static async save(firstName, lastName, email, password, age) {
        try {
            const requestOptions = {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({firstName, lastName, email, password, age})
            };
            const response = await fetch('http://localhost:8080/library', requestOptions);
            return await response.json();
        } catch (e) {
            alert(e);
        }
    };

    static async change(firstName, lastName, email, password, age, tokens) {
        try {
            const requestOptions = {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + tokens.access_token
                },
                body: JSON.stringify({firstName, lastName, email, password, age})
            };
            const response = await fetch('http://localhost:8080/library/authors', requestOptions);
            return await response.json();
        } catch (e) {
            alert(e);
        }
    };
}
