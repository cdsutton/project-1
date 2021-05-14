"use strict";

let URL = "http://localhost:7009/reimbursements";

document.querySelector('#add').addEventListener('click', add);

function add() {
    window.location.href = "add.html";
}

window.onload = function() {
    let currUser = JSON.parse(sessionStorage.getItem("currentlyLoggedInUser"));
    if (currUser) {
        // Populate table with all reimbursements by default
        populateTable("All");
        let filterByStatus = document.querySelector("#filterByStatus");
        filterByStatus.addEventListener("change", function() {
            populateTable(filterByStatus.value);
        });
    } else {
        // If no one is logged in, create sessionStorage value for message and redirect
        let message = "That page cannot be accessed without being logged in";
        sessionStorage.setItem("notLoggedIn", message);
        window.location.href = "index.html";
    }
}

async function getTableData() {
    return await fetch(URL, {
        method: 'GET',
        credentials: 'include'
    }).then((data) => {
        return data.json();
    }).then((response) => {
        if (response.message) {
            alert(response.message);
        } else {
            return response;
        }
    }).catch((error) => {
        console.log(error);
    });
}

async function populateTable(status) {
    let response = await getTableData();
    let tbody = document.querySelector("table tbody");
    tbody.innerHTML = "";
    for (let i = 0; i < response.length; i++) {
        if (status == 'All' || response[i].status == status) {
            let row = document.createElement("tr");
            for (let val in response[i]) {
                let td = document.createElement("td");
                let currUser = JSON.parse(sessionStorage.getItem("currentlyLoggedInUser"));
                // Don't add id to table row
                if (val == 'id') continue;
                // In order for approve/deny buttons to make sense, only add them when the user is a manager and the status is pending
                if (val == 'resolved' && response[i]["status"] == "pending" && currUser.role.roleName == "manager") {
                    td.innerHTML = `<button id="approveReimb" class="btn btn-success" onClick="approveReimb(${response[i]['id']}, '${status}')">Approve</button>` +
                                   `<button id="denyReimb" class="btn btn-danger" onClick="denyReimb(${response[i]['id']}, '${status}')">Deny</button>`;
                // Stops totally blank fields in table for nicer look
                } else if (response[i][val] == null) {
                    td.innerHTML = "N/A";
                // Convert date in milliseconds to human-readable date string
                } else if (val == 'submitted' || val == 'resolved') {
                    const dateString = new Date(response[i][val]).toLocaleString();
                    td.innerHTML = dateString;
                // If receipt isn't null, add button to open modal with receipt image
                } else if (val == 'receipt') {
                    let btn = `<button class="btn btn-primary" onClick="populateModal('data:image/png;base64,${response[i][val]}')">Show</button>`
                    td.innerHTML = btn;
                // Add dollar sign and commas to amount field for nicer look
                } else if (val == 'amount') {
                    td.innerHTML = `$${response[i][val].toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")}`;
                } else {
                    td.innerHTML = response[i][val];
                }
                row.appendChild(td);
            }
            tbody.appendChild(row);
        }
    }
}

function approveReimb(id, status) {

    fetch(URL + `/${id}/approve`, {
        method: 'POST',
        credentials: 'include'
    }).then((data) => {
        return data.json();
    }).then((response) => {
        if (response.message != "Reimbursement approved") {
            alert(response.message);
        } else {
            populateTable(status);
        }
    }).catch((error) => {
        console.log(error);
    });

}
function denyReimb(id, status) {

    fetch(URL + `/${id}/deny`, {
        method: 'POST',
        credentials: 'include'
    }).then((data) => {
        return data.json();
    }).then((response) => {
        if (response.message != "Reimbursement denied") {
            alert(response.message);
        } else {
            populateTable(status);
        }
    }).catch((error) => {
        console.log(error);
    });
}