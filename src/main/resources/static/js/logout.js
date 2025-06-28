function logout() {

    getCsrfToken().then(csrfToken=>{
    fetch("/api/v1/auth/logout", {
        method: "POST", // or POST if your backend expects POST
        headers: {
            "X-XSRF-TOKEN":csrfToken
        },
        credentials:"include"
    })
    .then(response => {
        if (response.ok) {
            window.location.href = "/index.html"; // Redirect to home page
        } else {
            console.error("Logout failed with status:", response.status);
        }
    })
    .catch(error => {
        console.error("Logout error:", error);
    });
    });
}
