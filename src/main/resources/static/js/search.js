let currentPage = 0;
const pageSize = 21;
let lastKeyword = '';

function searchMovies() {
    const keyword = document.getElementById('search-input').value.trim();
    const resultsDiv = document.getElementById('movie-results');
    resultsDiv.innerHTML = ''; // Clear old results

    // Reset page if keyword has changed
    if (keyword !== lastKeyword) {
        currentPage = 0;
        lastKeyword = keyword;
    }

    fetch(`/movie/search?keyword=${encodeURIComponent(keyword)}&page=${currentPage}&size=${pageSize}`)
        .then(response => {
            if (!response.ok) {
                throw new Error("Network response was not ok");
            }
            return response.json();
        })
        .then(data => {
            if (!data.content || data.content.length === 0) {
                resultsDiv.innerHTML = '<p>No movies found.</p>';
                return;
            }

            data.content.forEach(item => {
                const movie = item.movie;
                const overallRating = item.overallRating;

                const formattedGenres = Array.isArray(movie.genre) ? movie.genre.join('/') : movie.genre;

                const movieDiv = document.createElement('div');
                movieDiv.style.border = "none";
                movieDiv.style.padding = "10px";
                movieDiv.style.margin = "10px 0";

                const ratingHTML = `<span style="color: gold; font-size: 1.2em;">★</span> ${overallRating}`;

                movieDiv.innerHTML = `
                    <br>
                    <span id="movie-rating" style="font-weight: bold;">${ratingHTML}</span>
                    <h3 class="movie-info">
                        <a href="/fullMovieInfo.html?movieId=${movie.id}" style="text-decoration: none; color: inherit;">
                            ${movie.movieName}
                        </a>
                    </h3>
                    <p class="movie-info">${formattedGenres}</p>
                    <p class="movie-info">${movie.releaseYear}</p>
                `;

                const img = document.createElement("img");
                img.src = `/movie/${movie.id}/poster`;
                img.alt = movie.movieName;
                img.style.width = "150px";
                img.style.height = "250px";
                const imgLink = document.createElement("a");
                imgLink.href = `/movie/${movie.id}/poster`;
                imgLink.appendChild(img);

                movieDiv.insertBefore(imgLink, movieDiv.firstChild);

                resultsDiv.appendChild(movieDiv);
            });

            // Pagination controls
            const paginationDiv = document.createElement('div');
            paginationDiv.style.marginTop = '20px';

            const prevBtn = document.createElement('button');
            prevBtn.className="prev-btn";
            prevBtn.textContent = 'Previous';
            prevBtn.disabled = currentPage === 0;
            prevBtn.onclick = () => {
                currentPage--;
                searchMovies();
            };

            const nextBtn = document.createElement('button');
            nextBtn.className="nxt-btn";
            nextBtn.textContent = 'Next';
            nextBtn.disabled = currentPage + 1 >= data.totalPages;
            nextBtn.onclick = () => {
                currentPage++;
                searchMovies();
            };

            paginationDiv.appendChild(prevBtn);
            paginationDiv.appendChild(document.createTextNode(` Page ${currentPage + 1} of ${data.totalPages} `));
            paginationDiv.appendChild(nextBtn);
            const paginationControlDiv=document.getElementById("pagination-controls");
            paginationControlDiv.innerHTML= '';
            paginationControlDiv.appendChild(paginationDiv);
        })
        .catch(error => {
            resultsDiv.innerHTML = `<p>Error fetching movies: ${error.message}</p>`;
        });
}

window.addEventListener("load", function () {
    const params = new URLSearchParams(window.location.search);
    const keywordFromURL = params.get("keyword");
    if (keywordFromURL) {
        document.getElementById('search-input').value = keywordFromURL;
    }

    searchMovies();

    const inputField = document.getElementById('search-input');
    inputField.addEventListener("keydown", function (event) {
        if (event.key === "Enter") {
            searchMovies();
        }
    });
});
