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

    




document.getElementById('getLogsByEmployeeIdForm').addEventListener('submit', function(event) {
        event.preventDefault();
        var form = event.target;
        var userId = document.getElementById('id-1').value;
        var startDate = document.getElementById('startDate').value;
        var endDate = document.getElementById('endDate').value;

        // Validation
        if (!userId) {
            alert('User ID is required.');
            return;
        }
        if (!startDate) {
            alert('Start date is required.');
            return;
        }
        if (!endDate) {
            alert('End date is required.');
            return;
        }

        var start = new Date(startDate);
        var end = new Date(endDate);
        var today = new Date();

        if (start > end) {
            alert('Start date must be earlier than end date.');
            return;
        }

        if (start > today || end > today) {
            alert('Dates cannot be in the future.');
            return;
        }

        fetch(`http://localhost:8081/reporting/access/${userId}?startDate=${startDate}&endDate=${endDate}`)
        .then(response => response.blob()) // Assuming the response is a CSV file
        .then(blob => {
            var url = window.URL.createObjectURL(blob);
            var a = document.createElement('a');
            a.href = url;
            a.download = `logs_id_${userId}_from_${startDate}_to_${endDate}.xlsx`;
            document.body.appendChild(a);
            a.click();
            a.remove();
            form.reset(); // Reset form after successful submission
        })
        .catch(error => {
            console.error('Error:', error);
            handleErrorResponse(error);
        });
    });

    document.getElementById('getAllUsersByDateRangeForm').addEventListener('submit', function(event) {
        event.preventDefault();
        var form = event.target;
        var startDate = document.getElementById('startDateAll').value;
        var endDate = document.getElementById('endDateAll').value;

        // Validation
        if (!startDate) {
            alert('Start date is required.');
            return;
        }
        if (!endDate) {
            alert('End date is required.');
            return;
        }

        var start = new Date(startDate);
        var end = new Date(endDate);
        var today = new Date();

        if (start > end) {
            alert('Start date must be earlier than end date.');
            return;
        }

        if (start > today || end > today) {
            alert('Dates cannot be in the future.');
            return;
        }

        fetch(`http://localhost:8081/reporting/access/all?startDate=${startDate}&endDate=${endDate}`)
        .then(response => response.blob()) // Assuming the response is a CSV file
        .then(blob => {
            var url = window.URL.createObjectURL(blob);
            var a = document.createElement('a');
            var fileName = `all_logs_from_${startDate}_to_${endDate}.xlsx`;
            a.href = url;
            a.download = fileName;
            document.body.appendChild(a);
            a.click();
            a.remove();
            form.reset(); // Reset form after successful submission
        })
        .catch(error => {
            console.error('Error:', error);
            handleErrorResponse(error);
        });
    });

function getAllUsers() {
    fetch('http://localhost:8081/reporting/users/all')
    .then(response => response.blob()) // Assuming the response is a CSV file
    .then(blob => {
        var url = window.URL.createObjectURL(blob);
        var a = document.createElement('a');
        a.href = url;
        a.download = 'all_employees.xlsx';
        document.body.appendChild(a);
        a.click();
        a.remove();
    })
    .catch(error => {
        console.error('Error:', error);
        handleErrorResponse(error);
    });
}

function getActiveUsers() {
    fetch('http://localhost:8081/reporting/users/active')
    .then(response => response.blob())
    .then(blob => {
        var url = window.URL.createObjectURL(blob);
        var a = document.createElement('a');
        a.href = url;
        a.download = 'all_active_employees.xlsx';
        document.body.appendChild(a);
        a.click();
        a.remove();
    })
    .catch(error => {
        console.error('Error:', error);
        handleErrorResponse(error);
    });
}

    document.getElementById('getListByStatus').addEventListener('submit', function(event) {
        event.preventDefault();
        var form = event.target;
        var userStatus = document.getElementById('userStatus').selectedOptions;
        var values = Array.from(userStatus).map(({ value }) => value);
        console.log(values);
        

        fetch(`http://localhost:8081/reporting/users/status?employeeStatus=` + values)
        .then(response => response.blob()) // Assuming the response is a CSV file
        .then(blob => {
            var url = window.URL.createObjectURL(blob);
            var a = document.createElement('a');
            var fileName = `employees_by_status.xlsx`;
            a.href = url;
            a.download = fileName;
            document.body.appendChild(a);
            a.click();
            a.remove();
            form.reset(); // Reset form after successful submission
        })
        .catch(error => {
            console.error('Error:', error);
            handleErrorResponse(error);
        });
    });



document.getElementById('getUserByIdForm').addEventListener('submit', function(event) {
        event.preventDefault();
        var userId = document.getElementById('id').value;
        fetch('http://localhost:8081/id?id=' + userId)
        .then(response => response.json())
        .then(data => {
            console.log(data);
            handleServerResponse(data);
            event.target.reset(); // Reset form after successful submission
        })
        .catch(error => {
            console.error('Error:', error);
            handleErrorResponse(error);
        });
    });

    document.getElementById('getUserByRfidForm').addEventListener('submit', function(event) {
        event.preventDefault();
        var rfid = document.getElementById('rfidSearch').value;
        fetch('http://localhost:8081/rfid?rfid=' + rfid)
        .then(response => response.json())
        .then(data => {
            console.log(data);
            handleServerResponse(data);
            event.target.reset(); // Reset form after successful submission
        })
        .catch(error => {
            console.error('Error:', error);
            handleErrorResponse(error);
        });
    });

    // Add similar event listeners for the other forms

    document.getElementById('getUserBySurnameForm').addEventListener('submit', function(event) {
        event.preventDefault();
        var lastName = document.getElementById('lastNameSearch').value;
        fetch('http://localhost:8081/lastName?lastName=' + lastName)
        .then(response => response.json())
        .then(data => {
            console.log(data);
            handleServerResponse(data);
            event.target.reset(); // Reset form after successful submission
        })
        .catch(error => {
            console.error('Error:', error);
            handleErrorResponse(error);
        });
    });

    document.getElementById('getUserByNameForm').addEventListener('submit', function(event) {
        event.preventDefault();
        var firstName = document.getElementById('firstNameSearch').value;
        fetch('http://localhost:8081/firstName?firstName=' + firstName)
        .then(response => response.json())
        .then(data => {
            console.log(data);
            handleServerResponse(data);
            event.target.reset(); // Reset form after successful submission
        })
        .catch(error => {
            console.error('Error:', error);
            handleErrorResponse(error);
        });
    });

    document.getElementById('deactivateEmployeeById').addEventListener('submit', function(event) {
        event.preventDefault();
        var deactivateId = document.getElementById('deactivateById').value;
        fetch('http://localhost:8081/deactivate?id=' + deactivateId, {
            method: 'PATCH'
        })
        .then(response => {
            if (response.ok) {
                console.log('User deactivated successfully');
                handleServerResponse('User deactivated successfully');
                event.target.reset(); // Reset form after successful submission
            } else {
                console.error('Error:', response.statusText);
                handleErrorResponse(response.statusText);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            handleErrorResponse(error);
        });
    });



    document.getElementById('activateById').addEventListener('submit', function(event) {
        event.preventDefault();
        var activateId = document.getElementById('activateId').value;
        fetch('http://localhost:8081/activate?id=' + activateId, {
            method: 'PATCH'
        })
        .then(response => {
            if (response.ok) {
                console.log('User activated successfully');
                handleServerResponse('User activated successfully');
                event.target.reset(); // Reset form after successful submission
            } else {
                console.error('Error:', response.statusText);
                handleErrorResponse(response.statusText);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            handleErrorResponse(error);
        });
    });

document.getElementById('getUserByIdForm').addEventListener('submit', function(event) {
        event.preventDefault();
        var userId = document.getElementById('id').value;
        fetch('http://localhost:8081/id?id=' + userId)
        .then(response => response.json())
        .then(data => {
            console.log(data);
            handleServerResponse(data);
            event.target.reset(); // Reset form after successful submission
        })
        .catch(error => {
            console.error('Error:', error);
            handleErrorResponse(error);
        });
    });

    document.getElementById('getUserByRfidForm').addEventListener('submit', function(event) {
        event.preventDefault();
        var rfid = document.getElementById('rfidSearch').value;
        fetch('http://localhost:8081/rfid?rfid=' + rfid)
        .then(response => response.json())
        .then(data => {
            console.log(data);
            handleServerResponse(data);
            event.target.reset(); // Reset form after successful submission
        })
        .catch(error => {
            console.error('Error:', error);
            handleErrorResponse(error);
        });
    });

    // Add similar event listeners for the other forms

    document.getElementById('getUserBySurnameForm').addEventListener('submit', function(event) {
        event.preventDefault();
        var lastName = document.getElementById('lastNameSearch').value;
        fetch('http://localhost:8081/lastName?lastName=' + lastName)
        .then(response => response.json())
        .then(data => {
            console.log(data);
            handleServerResponse(data);
            event.target.reset(); // Reset form after successful submission
        })
        .catch(error => {
            console.error('Error:', error);
            handleErrorResponse(error);
        });
    });

    document.getElementById('getUserByNameForm').addEventListener('submit', function(event) {
        event.preventDefault();
        var firstName = document.getElementById('firstNameSearch').value;
        fetch('http://localhost:8081/firstName?firstName=' + firstName)
        .then(response => response.json())
        .then(data => {
            console.log(data);
            handleServerResponse(data);
            event.target.reset(); // Reset form after successful submission
        })
        .catch(error => {
            console.error('Error:', error);
            handleErrorResponse(error);
        });
    });

    document.getElementById('deactivateEmployeeById').addEventListener('submit', function(event) {
        event.preventDefault();
        var deactivateId = document.getElementById('deactivateById').value;
        fetch('http://localhost:8081/deactivate?id=' + deactivateId, {
            method: 'PATCH'
        })
        .then(response => {
            if (response.ok) {
                console.log('User deactivated successfully');
                handleServerResponse('User deactivated successfully');
                event.target.reset(); // Reset form after successful submission
            } else {
                console.error('Error:', response.statusText);
                handleErrorResponse(response.statusText);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            handleErrorResponse(error);
        });
    });



    document.getElementById('activateById').addEventListener('submit', function(event) {
        event.preventDefault();
        var activateId = document.getElementById('activateId').value;
        fetch('http://localhost:8081/activate?id=' + activateId, {
            method: 'PATCH'
        })
        .then(response => {
            if (response.ok) {
                console.log('User activated successfully');
                handleServerResponse('User activated successfully');
                event.target.reset(); // Reset form after successful submission
            } else {
                console.error('Error:', response.statusText);
                handleErrorResponse(response.statusText);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            handleErrorResponse(error);
        });
    });

