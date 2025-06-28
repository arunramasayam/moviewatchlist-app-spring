function loadUserInfo() {
    fetch("/user/userinfo", {
        headers: {
            credentials:"include"
        }
    })
    .then(response => {
        if (!response.ok) {
            document.getElementById("user-info").innerText = "Guest";
            const loginBtn=document.createElement("button");
            loginBtn.className="login-btn";
            loginBtn.innerText="Login";
            loginBtn.onclick = () => window.location.href = "login.html";
            document.getElementById("user-section").appendChild(loginBtn);


            return;
        }
        return response.json();
    })
    .then(data => {
        if (data) {
            document.getElementById("user-info").innerText = `Logged in as: ${data.name} (${data.email})`;
            const logoutBtn = document.getElementById("logout-btn");
            if (logoutBtn) {
                logoutBtn.style.display = "inline";
                logoutBtn.onclick = logout;
            }
        }
    })
    .catch(err => {
        console.error("Error loading user info:", err);

    });
}
  window.addEventListener("load", function () {
      fetch("user.html")
          .then(res => res.text())
          .then(html => {
              document.getElementById("user-section").innerHTML = html;
              loadUserInfo();
          })
          .catch(err => {
              console.error("Error loading user info HTML:", err);
          });
  });

