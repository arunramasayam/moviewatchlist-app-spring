    document.getElementById("load-movies-btn").addEventListener("click", function () {

        getCsrfToken().then(csrfToken=>{
        fetch("/movie/load-movies", {
            method: "POST",
            headers:{
            "X-XSRF-TOKEN":csrfToken
            },
            credentials:"include"
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
    });