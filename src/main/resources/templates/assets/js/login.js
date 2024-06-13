document.addEventListener("DOMContentLoaded", function() {
    var form = document.querySelector("form");

    form.addEventListener("submit", function(event) {
        event.preventDefault();

        var email = document.querySelector("input[name='email']").value;
        var password = document.querySelector("input[name='password']").value;

        if (email === "admin@company.com" && password === "admin") {
            window.location.href = window.location.href = "user-form.html"
        } else {
            alert("Nieprawidłowy login lub hasło!");
        }
    });
});
