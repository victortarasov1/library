export default class EventService {

    static async get(tokens) {
        try {
            const requestOptions = {
                method: 'GET',
                mode: 'cors',
                headers: {
                    'Authorization': 'Bearer ' + tokens.access_token
                }
            };
            const response = await fetch('http://localhost:8080/calendar/events', requestOptions);
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
            const response = await fetch('http://localhost:8080/calendar/events/' + id, requestOptions);
            if (response.status !== 200) {
                return {hasError: true}
            }
        } catch (e) {
            alert(e);
        }
    };

    static async change(tokens, id, description, startTime, endTime) {
        try {
            const requestOptions = {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + tokens.access_token
                },
                body: JSON.stringify({id, startTime, endTime, description})
            }
            const response = await fetch('http://localhost:8080/calendar/events', requestOptions);
            return await response.json();
        } catch (e) {
            alert(e)
        }
    };


    static async save(tokens, description, startTime, endTime) {
        try {
            const requestOptions = {
                method: 'POST',
                mode: 'cors',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + tokens.access_token
                },
                body: JSON.stringify({description, startTime, endTime})
            }
            const response = await fetch('http://localhost:8080/calendar/events', requestOptions);
            return await response.json();
        } catch (e) {
            alert(e)
        }
    };

    static async getParticipants(tokens, id) {
        try {
            const response = await fetch('http://localhost:8080/calendar/events/users/' + id, {
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

    static async addParticipant(tokens, id, email) {
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
            const response = await fetch('http://localhost:8080/calendar/events/' + id, requestOptions);
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
