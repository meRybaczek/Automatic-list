

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





    document.getElementById('addUserForm').addEventListener('submit', function(event) {
        event.preventDefault();
        //var form = event.target;
        var formData = {
            firstName: document.getElementById('firstName').value,
            lastName: document.getElementById('lastName').value,
            rfid: document.getElementById('rfid').value,
            status: document.getElementById('status').value
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
            event.target.reset();
            //form.reset(); // Reset form after successful submission
        })
        .catch(error => {
            console.error('Error:', error);
            handleErrorResponse(error);
        });
    });

    