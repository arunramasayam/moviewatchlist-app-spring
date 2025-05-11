const urlParams = new URLSearchParams(window.location.search);
const movieId = urlParams.get('movieId');

// Fetch movie details when page loads
fetchMovieDetails();

function getAuthHeaders() {
    const token = localStorage.getItem("jwt");
    const headers = { 'Content-Type': 'application/json' };
    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    } else {
//        alert("You are not logged in. Please log in to use the watchlist feature.");
    }
    return headers;
}

function fetchMovieDetails() {
    fetch(`/movie/${movieId}`, {
        method: 'GET',
        headers: getAuthHeaders()
    })
    .then(async res => {
        if (!res.ok) throw new Error(await res.text());
        return res.json();
    })
    .then(data => {
        const m = data.movie;
        const genres = Array.isArray(m.genre) ? m.genre.join('/') : m.genre || '';
        const { userRating = 0, userReview = "", reviewsList: reviews = [], overallRating = 0, totalRatedUsers = 0, movieStatus } = data;

        // Show "Add Review" if no review, and hide review box
        const reviewSection = userReview
            ? `
                <label><strong>Your Review:</strong></label><br>
                <textarea id="user-review" cols="40" rows="5">${userReview}</textarea><br>
                <button id="save-review-btn">Save Review</button>
                <button id="delete-review-btn">Delete Review</button>
            `
            : `
                <label><strong>Your Review:</strong></label><br>
                <button id="add-review-btn">Add Review</button>
            `;

        document.getElementById('movie-details').innerHTML = `
            <img src="/movie/${m.id}/poster" alt="${m.movieName}" style="max-width:200px">
            <h2 class="movie-info">${m.movieName}</h2>
            <p class="movie-info">${genres}</p>
            <p class="movie-info">${m.releaseYear}</p>
            <p class="movie-info">${m.description}</p>
            <div id="watchlist-placeholder"></div>
            <div id="user-rating">
                <p>Your Rating:</p>
                <div style="display: flex; align-items: center;">
                    <div id="star-rating-container" style="user-select: none;"></div>
                    <span id="current-rating-value" style="margin-left: 10px;"></span>
                    <button id="remove-rating-btn" style="margin-left: 10px; display: none;">Remove Rating</button>
                </div>
            </div>

            <p>Overall Rating: <span style="color:gold">★</span> ${overallRating.toFixed(1)}/5 (${totalRatedUsers})</p>
            ${reviewSection}
            <h3>Reviews:</h3>
            <ul>
                ${reviews.map(r => `
                    <li><strong>${r.username}</strong>: <span style="color:gold">★</span> ${r.userRating}
                    <p id="user-reviews">${r.userReview}</p></li>
                `).join('')}
            </ul>
        `;

        // Setup the star rating interface
        setupStarRating(userRating);

        // Attach event listeners
        if (userReview) {
            document.getElementById('save-review-btn').addEventListener('click', saveReview);
            document.getElementById('delete-review-btn').addEventListener('click', deleteReview);
        } else {
            document.getElementById('add-review-btn').addEventListener('click', showReviewBox);
        }

        // Load watchlist functionality
        fetch('watchlistselect.html')
            .then(res => res.text())
            .then(html => {
                document.getElementById('watchlist-placeholder').innerHTML = html;

                if (!window.setupWatchlistUI) {
                    const script = document.createElement('script');
                    script.src = '/js/watchlistselect.js';
                    script.onload = () => {
                        setupWatchlistUI(m.id, movieStatus);
                    };
                    document.body.appendChild(script);
                } else {
                    setupWatchlistUI(m.id, movieStatus);
                }
            });
    })
    .catch(err => {
        console.error(err);
        //document.getElementById('movie-details').innerHTML = `<p>Error loading movie details: ${err.message}</p>`;
    });
}

