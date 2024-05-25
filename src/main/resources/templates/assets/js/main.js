// Functions for handling server response
function handleServerResponse(response) {
    var serverResponseDiv = document.getElementById('serverResponse');
    serverResponseDiv.innerHTML = ''; // Clear previous content

    if (typeof response === 'object') {
        for (var key in response) {
            var paragraph = document.createElement('p');
            paragraph.textContent = key + ': ' + response[key];
            serverResponseDiv.appendChild(paragraph);
        }
    } else {
        serverResponseDiv.innerText = response;
    }
}

function handleErrorResponse(error) {
    document.getElementById('serverResponse').innerText = 'Error: ' + error;
}

// Event listeners for form submissions
document.getElementById('addEmployeeForm').addEventListener('submit', function(event) {
    event.preventDefault();
    var formData = {
        firstName: document.getElementById('firstName').value,
        lastName: document.getElementById('lastName').value,
        rfid: document.getElementById('rfid').value,
        hasPermission: document.getElementById('hasPermission').checked
    };

    fetch('http://localhost:8081/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
    .then(response => response.json())
    .then(data => {
        console.log(data);
        handleServerResponse(data);
    })
    .catch(error => {
        console.error('Error:', error);
        handleErrorResponse(error);
    });
});

function getEmployeeById() {
    var employeeId = document.getElementById('id').value;
    fetch('http://localhost:8081/id?id=' + employeeId)
    .then(response => response.json())
    .then(data => {
        console.log(data);
        handleServerResponse(data);
    })
    .catch(error => {
        console.error('Error:', error);
        handleErrorResponse(error);
    });
}

function getEmployeeByRfid() {
    var rfid = document.getElementById('rfidSearch').value;
    fetch('http://localhost:8081/rfid?rfid=' + rfid)
    .then(response => response.json())
    .then(data => {
        console.log(data);
        handleServerResponse(data);
    })
    .catch(error => {
        console.error('Error:', error);
        handleErrorResponse(error);
    });
}

function getEmployeeBySurname() {
    var lastName = document.getElementById('lastNameSearch').value;
    fetch('http://localhost:8081/lastName?lastName=' + lastName)
    .then(response => response.json())
    .then(data => {
        console.log(data);
        handleServerResponse(data);
    })
    .catch(error => {
        console.error('Error:', error);
        handleErrorResponse(error);
    });
}

function getEmployeeByName() {
    var firstName = document.getElementById('firstNameSearch').value;
    fetch('http://localhost:8081/firstName?firstName=' + firstName)
    .then(response => response.json())
    .then(data => {
        console.log(data);
        handleServerResponse(data);
    })
    .catch(error => {
        console.error('Error:', error);
        handleErrorResponse(error);
    });
}

function deleteEmployeeById() {
    var deleteId = document.getElementById('deleteId').value;
    fetch('http://localhost:8081/?id=' + deleteId, {
        method: 'DELETE'
    })
    .then(response => {
        if (response.ok) {
            console.log('Employee deleted successfully');
            handleServerResponse('Employee deleted successfully');
        } else {
            console.error('Error:', response.statusText);
            handleErrorResponse(response.statusText);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        handleErrorResponse(error);
    });
}
