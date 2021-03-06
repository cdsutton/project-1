"use strict";

document.querySelector('#login').addEventListener('click', login);

function login() {
    let un = document.querySelector('#username').value;
    let pw = document.querySelector('#password').value;

    let data = {
        username: un,
        password: pw
    };

    fetch('http://localhost:7009/login', {
        method: 'POST',
        credentials: 'include', // this specifies that when you receive cookies, you should include them in future requests. So in our case, it's important so that the backend can identify if we are logged in or not.
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }).then((response) => {
        if (response.status === 200) {
            sessionStorage.setItem("currentlyLoggedInUser", JSON.stringify(response));
            window.location.href = '/landing.html';
		} else if (response.status === 400) {
            displayInvalidLogin();
        }
    })
}

function displayInvalidLogin() {
    let bodyElement = document.querySelector('body');

    let pElement = document.createElement('p');
    pElement.style.color = 'red';
    pElement.innerHTML = 'Invalid login!';

    bodyElement.appendChild(pElement);
}