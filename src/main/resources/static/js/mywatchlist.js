
    function renderMovie(movie, watchStatus) {
      const container = document.createElement("div");
      container.className = "movie-item";

      const genre = Array.isArray(movie.genre) ? movie.genre.join(', ') : movie.genre || '';
      const releaseYear=movie.releaseYear;

      container.innerHTML = `
        <a href="/movie/${movie.id}/poster"><img src="/movie/${movie.id}/poster" alt="${movie.movieName}"></a>
        <div class="movie-info">
          <div class="movie-name"><a href="/fullMovieInfo.html?movieId=${movie.id}" style="text-decoration: none; color: inherit;">${movie.movieName}</a></div>
          <div class="movie-genre">${genre}</div>
          <div class="movie-release">${releaseYear}</div>
          <div class="actions">
            <select onchange="updateWatchlistStatus(${movie.id}, this.value)">
              <option value="TO_WATCH" ${watchStatus === 'TO_WATCH' ? 'selected' : ''}>To Watch</option>
              <option value="WATCHED" ${watchStatus === 'WATCHED' ? 'selected' : ''}>Watched</option>
            </select>
            <button onclick="removeFromWatchlist(${movie.id})">Remove</button>
          </div>
        </div>
      `;
      return container;
    }

    fetch('/watchlist/mywatchlist', {
      method: 'GET',
      credentials:"include"
    })
    .then(async res => {
      if (!res.ok) throw new Error(await res.text());
      return res.json();
    })
    .then(data => {
      const { allMovies = [], watched = [], toWatch = [] } = data;

      const allContainer = document.getElementById('all-movies');
      const watchedContainer = document.getElementById('watched-movies');
      const toWatchContainer = document.getElementById('to-watch-movies');

      allMovies.forEach(entry => {
        allContainer.appendChild(renderMovie(entry.movie, entry.watchStatus));
      });

      watched.forEach(entry => {
        watchedContainer.appendChild(renderMovie(entry.movie, entry.watchStatus));
      });

      toWatch.forEach(entry => {
        toWatchContainer.appendChild(renderMovie(entry.movie, entry.watchStatus));
      });
    })
    .catch(err => {
      console.error(err);
      alert("Failed to load watchlist: " + err.message);
    });


