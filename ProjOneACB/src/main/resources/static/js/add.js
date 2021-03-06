"use strict";
let URL = "http://localhost:7009/reimbursements";
	

window.onload = function() {
    let currUser = JSON.parse(sessionStorage.getItem("currentlyLoggedInUser"));
    if (currUser) {
        let button = document.querySelector('#submitReimb');
        button.addEventListener('click', function(event) {
            event.preventDefault();
            createReimb();
        });
    } else {
        // If no one is logged in, create sessionStorage value for message and redirect
        let message = "That page cannot be accessed without being logged in";
        sessionStorage.setItem("notLoggedIn", message);
        window.location.href = "index.html";
    }
}

function createReimb() {
    let fd = new FormData();

    fd.append("amount", document.getElementById("amountInput").value);
    fd.append("description", document.getElementById("descriptionInput").value);
    let file = document.getElementById("receiptInput").files[0];
    if (file) {
        fd.append("receipt", file, file.name);
    } else {
        fd.append("receipt", "");
    }    
    fd.append("type", document.getElementById("typeInput").value);

    fetch(URL, {
        method: 'POST',
        credentials: 'include',
        body: fd
    }).then((data) => {
        return data.json();
    }).then((response) => {
        if (response.message) {
            alert(response.message);
        } else {
            window.location.href = "view.html";
        }
    }).catch((error) => {
        console.log(error);
    });
}