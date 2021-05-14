"use strict";

window.onload = function() {
    renderCurrentUser();
}

function renderCurrentUser() {
    fetch('http://localhost:7009/current_user', {
        method: 'GET',
        credentials: 'include'
    }).then((response) => {
        if (response.status === 400) {
            window.location.href = '/landing.html';
        }

        return response.json();
    }).then((data) => {
        let firstname = data.firstName;
        let lastname = data.lastName;
        let role = data.role.roleName;

        let userInfoElement = document.querySelector('#user');
        userInfoElement.innerHTML = `Welcome, ${firstname} ${lastname}!`
        
        document.cookie = "userRole=" + role;
        alert(document.cookie);

        console.log("userRole" + role);
    })
}