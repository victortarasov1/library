export default class LoginService {
    static async login(email, password) {
        try {
            const response = await fetch('http://localhost:8080/login', {
                method: 'POST',
                mode: 'cors',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: new URLSearchParams({
                    'email': email,
                    'password': password,
                })
            });
            return await response.json();
        } catch (e) {
            alert(e);
        }

    };

    static async refresh(tokens) {
        try {
            const response = await fetch('http://localhost:8080/library/refresh', {
                method: 'GET',
                mode: 'cors',
                headers: {
                    'Authorization': 'Bearer ' + tokens.refresh_token
                }
            });
            if (response.ok) {
                return await response.json();
            } else {
                return {hasError: true}
            }
        } catch (e) {
            alert(e);
        }
    };
}
