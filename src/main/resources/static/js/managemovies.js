

// === Auth Header Helper ===
function getAuthHeaders() {
    const token = localStorage.getItem("jwt");
    const headers = {};
    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }
    return headers;
}

// === Pagination State ===
let adminCurrentPage = 0;
const adminPageSize = 15;
let lastAdminKeyword = "";

// === Load Movies (with Search & Pagination) ===
function loadMovies() {
    const keyword = document.getElementById("searchInput").value.trim();

    if (keyword !== lastAdminKeyword) {
        adminCurrentPage = 0;
        lastAdminKeyword = keyword;
    }

    fetch(`/movie/search?keyword=${encodeURIComponent(keyword)}&page=${adminCurrentPage}&size=${adminPageSize}`, {
        method: 'GET',
        headers: getAuthHeaders()
    })
    .then(res => res.json())
    .then(data => {
        const list = document.getElementById("movieList");
        list.innerHTML = "";

        if (!data.content || data.content.length === 0) {
            list.innerHTML = "<p>No movies found.</p>";
            return;
        }

        data.content.forEach(item => {
            const m = item.movie;
            const div = document.createElement("div");
            div.className = "movie";
            div.setAttribute("data-id", m.id);
            div.setAttribute("data-name", m.movieName);
            div.setAttribute("data-release", m.releaseYear);
            div.setAttribute("data-genre", JSON.stringify(m.genre));
            div.setAttribute("data-description", encodeURIComponent(m.description || ""));

            div.innerHTML = `
                <img src="/movie/${m.id}/poster" alt="Poster" style="width:100px;height:150px;">
                <h4>${m.movieName} (${m.releaseYear})</h4>
                <p>${m.genre.join(", ")}</p>
                <button id="update-btn" onclick="populateForm(${m.id})">Update</button>
                <button id="delete-btn" onclick="deleteMovie(${m.id})">Delete</button>
            `;
            list.appendChild(div);
        });

        // === Pagination Controls =

        const paginationDiv = document.createElement("div");
        paginationDiv.className = "pagination-controls";
        paginationDiv.style.marginTop = "20px";

        const prevBtn = document.createElement("button");
        prevBtn.className="prev-btn";
        prevBtn.textContent = "Previous";
        prevBtn.disabled = adminCurrentPage === 0;
        prevBtn.onclick = () => {
            adminCurrentPage--;
            loadMovies();
        };

        const nextBtn = document.createElement("button");
        nextBtn.className="nxt-btn";
        nextBtn.textContent = "Next";
        nextBtn.disabled = adminCurrentPage + 1 >= data.totalPages;
        nextBtn.onclick = () => {
            adminCurrentPage++;
            loadMovies();
        };


        paginationDiv.appendChild(prevBtn);
        paginationDiv.appendChild(document.createTextNode(` Page ${adminCurrentPage + 1} of ${data.totalPages} `));
        paginationDiv.appendChild(nextBtn);
        const paginationControls = document.getElementById("pagination-controls");
        paginationControls.innerHTML = ''; // Clear old controls
        paginationControls.appendChild(paginationDiv);


    })
    .catch(err => {
        console.error("Error loading movies:", err);
        alert("Failed to load movies.");
    });
}

// === Populate Form for Update ===
function populateForm(id) {
    const card = document.querySelector(`.movie[data-id='${id}']`);
    if (!card) return alert("Movie not found");

    const movie = {
        id: card.getAttribute("data-id"),
        movieName: card.getAttribute("data-name"),
        releaseYear: card.getAttribute("data-release"),
        genre: JSON.parse(card.getAttribute("data-genre")),
        description: decodeURIComponent(card.getAttribute("data-description"))
    };

    showForm('update', movie);
}

// === Show Add/Update Form ===
function showForm(action, movie = null) {
    const form = document.getElementById("movieForm");
    form.style.display = 'block';
    document.getElementById("movieId").value = movie ? movie.id : '';
    document.getElementById("movieName").value = movie ? movie.movieName : '';
    document.getElementById("releaseYear").value = movie ? movie.releaseYear : '';
    document.getElementById("description").value = movie ? movie.description : '';

    const genreSelect = document.getElementById("genre");
    if (movie) {
        for (let i = 0; i < genreSelect.options.length; i++) {
            genreSelect.options[i].selected = movie.genre.includes(genreSelect.options[i].value);
        }
    } else {
        for (let i = 0; i < genreSelect.options.length; i++) {
            genreSelect.options[i].selected = false;
        }
    }

    document.getElementById("formTitle").textContent = action === 'add' ? 'Add Movie' : 'Update Movie';
    document.getElementById("submitBtn").textContent = action === 'add' ? 'Save' : 'Update';
}

// === Reset Form ===
function resetForm() {
    document.getElementById("movieForm").reset();
    document.getElementById("movieId").value = "";
    document.getElementById("formTitle").textContent = "Add Movie";
    document.getElementById("submitBtn").textContent = "Save";
    document.getElementById("movieForm").style.display = 'none';
}

// === Submit Form (Add/Update) ===
function handleFormSubmit(e) {
    e.preventDefault();

    const movieId = document.getElementById("movieId").value;
    const isUpdate = movieId !== "";
    const url = isUpdate ? `/movie/update/${movieId}` : `/movie/add`;

    const movieJson = {
        movieName: document.getElementById("movieName").value,
        genre: Array.from(document.getElementById("genre").selectedOptions).map(opt => opt.value),
        releaseYear: document.getElementById("releaseYear").value,
        description: document.getElementById("description").value
    };

    const formData = new FormData();
    formData.append("movieJson", new Blob([JSON.stringify(movieJson)], { type: "application/json" }));
    const posterFile = document.getElementById("poster").files[0];
    if (posterFile) formData.append("moviePoster", posterFile);

    fetch(url, {
        method: isUpdate ? "PUT" : "POST",
        headers: getAuthHeaders(),
        body: formData
    })
    .then(res => {
        if (!res.ok) throw new Error("Request failed");
        return res.text();
    })
    .then(() => {
        alert(isUpdate ? "Movie updated!" : "Movie added!");
        resetForm();
        if (isUpdate) {
            window.location.reload();
        } else {
            loadMovies();
        }
    })
    .catch(err => {
        console.error(err);
        alert(isUpdate ? "Update failed" : "Save failed");
    });
}

// === Delete Movie ===
function deleteMovie(id) {
    if (!confirm("Are you sure you want to delete this movie?")) return;

    fetch(`/movie/delete/${id}`, {
        method: "DELETE",
        headers: getAuthHeaders()
    })
    .then(res => {
        if (!res.ok) throw new Error("Delete failed");
        return res.text();
    })
    .then(() => {
        alert("Movie deleted!");
        loadMovies();
    })
    .catch(err => {
        console.error(err);
        alert("Delete failed");
    });
}

// === Events ===
document.getElementById("movieForm").addEventListener("submit", handleFormSubmit);
document.getElementById("searchInput").addEventListener("keydown", function (event) {
    if (event.key === "Enter") {
        loadMovies();
    }
});
window.onload = loadMovies;
