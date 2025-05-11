    document.getElementById("load-movies-btn").addEventListener("click", function () {
        const token = localStorage.getItem("jwt");
        if (!token) {
            document.getElementById("message").textContent = "Error: You must be logged in as admin.";
            document.getElementById("message").style.color = "red";
            return;
        }

        fetch("/movie/load-movies", {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token
            }
        })
        .then(response => {
            if (!response.ok) {
                throw new Error("Failed to load movies");
            }
            return response.text();
        })
        .then(message => {
            document.getElementById("message").textContent = "Success: " + message;
            document.getElementById("message").style.color = "green";
        })
        .catch(err => {
            document.getElementById("message").textContent = "Error: " + err.message;
            document.getElementById("message").style.color = "red";
        });
    });