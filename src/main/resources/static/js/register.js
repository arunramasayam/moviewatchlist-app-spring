document.getElementById("signup-form").addEventListener("submit", function (e) {
    e.preventDefault();

    const name = document.getElementById("name").value.trim();
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value;

    fetch("/user/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ name, email, password })
    })
    .then(res => {
        if (!res.ok) {
            return res.text().then(text => {
                try {
                    const errorData = JSON.parse(text); // Try parsing JSON
                    throw new Error(errorData.error || "Signup failed");
                } catch (e) {
                    // If it's not valid JSON, throw a generic error
                    throw new Error("Signup failed: " + text);
                }
            });
        }
        return res.text(); // If successful, continue with registration
    })
    .then(msg => {
        document.getElementById("signup-message").innerText = "Registered successfully";
        document.getElementById("signup-error").innerText = "";
    })
    .catch(err => {
        document.getElementById("signup-error").innerText = err.message;
        document.getElementById("signup-message").innerText = "";
    });
});
