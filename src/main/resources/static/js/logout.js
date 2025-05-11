function logout() {
    const token = localStorage.getItem("jwt");

    fetch("/api/v1/auth/logout", {
        method: "POST", // or POST if your backend expects POST
        headers: {
            "Authorization": "Bearer " + token
        }
    })
    .then(response => {
        if (response.ok) {
            // Clear the token and update UI
            localStorage.removeItem("jwt");
            window.location.href = "/index.html"; // Redirect to home page
        } else {
            console.error("Logout failed with status:", response.status);
        }
    })
    .catch(error => {
        console.error("Logout error:", error);
    });
}
