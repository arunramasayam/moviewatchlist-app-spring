document.getElementById("login-form").addEventListener("submit", function(e) {
    e.preventDefault();

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    fetch("/user/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ email, password })
    })
    .then(response => {
        if (response.ok) {
            return response.text(); // Success
        } else {
            return response.text().then(errorMessage => {
                throw new Error(errorMessage); // Handle errors
            });
        }
    })
    .then(data => {
        if (data.includes("jwt:")) {
            const token = data.split("jwt:")[1].trim();
            localStorage.setItem("jwt", token);
            window.location.href = "index.html"; // redirect to home page
        } else {
            document.getElementById("login-error").textContent = "Invalid login"; // If JWT not found in response
        }
    })
    .catch(error => {
        // Handle errors (invalid login, etc.)
        document.getElementById("login-error").textContent = "Error: " + error.message;
    });
});
