let csrfToken = null;

async function getCsrfToken() {
    if (csrfToken) return csrfToken; // Return cached token
    
    try {
        const response = await fetch("/csrf-token", {
            method: "GET",
            credentials: "include"
        });
        if (response.ok) {
            const data = await response.json();
            csrfToken = data.token;
        }
    } catch (error) {
        console.error("Failed to get CSRF token:", error);
    }
    return csrfToken;
}

//document.addEventListener('DOMContentLoaded', getCsrfToken);