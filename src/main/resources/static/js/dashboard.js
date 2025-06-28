
    function renderMovie(movieEntry) {
      const { movie, watchStatus } = movieEntry;
      const genre = Array.isArray(movie.genre) ? movie.genre.join(', ') : movie.genre;

      const container = document.createElement("div");
      container.className = "movie-item";

      container.innerHTML = `
        <a href="/movie/${movie.id}/poster"><img class="movie-poster" src="/movie/${movie.id}/poster" alt="${movie.movieName}" /></a>
        <div class="movie-info">
          <a href="/fullMovieInfo.html?movieId=${movie.id}" style="text-decoration: none; color: inherit;"><div class="movie-name">${movie.movieName}</div></a>
          <div class="movie-genre">${genre}</div>
          <div class="movie-release">${movie.releaseYear}</div>
          <div class="actions">
                    <select onchange="updateWatchlistStatus(${movie.id}, this.value)">
                      <option value="TO_WATCH" ${watchStatus === 'TO_WATCH' ? 'selected': ''}>To Watch</option>
                      <option value="WATCHED" ${watchStatus === 'WATCHED' ? 'selected' : ''}>Watched</option>
                    </select>
                    <br>
                    <button id="remove-btn" onclick="removeFromWatchlist(${movie.id})">Remove</button>
                  </div>
        </div>

      `;
      return container;
    }

    fetch("/dashboard/mydashboard", {
      credentials:"include"
    })
    .then(res => res.json())
    .then(data => {
      const { watchListOverviewDto, watchListMovies } = data;

      document.getElementById("totalMovies").textContent = watchListOverviewDto.totalMovies;
      document.getElementById("watchedMovies").textContent = watchListOverviewDto.totalWatchedMovies;
      document.getElementById("toWatchMovies").textContent = watchListOverviewDto.totalToWatchMovies;

      const container = document.getElementById("watchlistContainer");
      watchListMovies.forEach(entry => {
        container.appendChild(renderMovie(entry));
      });
    })
    .catch(err => {
      console.error("Error loading dashboard:", err);
    });