function setupStarRating(currentRating) {
    const container = document.getElementById('star-rating-container');
    container.innerHTML = '';  // Clear any previous stars

    // Create 5 stars
    for (let i = 1; i <= 5; i++) {
        const star = document.createElement('span');
        star.innerHTML = '★';
        star.style.fontSize = '30px';
        star.style.cursor = 'pointer';
        star.style.marginRight = '5px';
        star.dataset.value = i;

        // Set initial color based on rating
        star.style.color = i <= currentRating ? 'gold' : 'lightgray';

        // Add event listeners for click (to rate) and right-click (to set half-rating)
        star.addEventListener('click', () => setRating(i));
        star.addEventListener('contextmenu', (e) => {
            e.preventDefault();
            setRating(i - 0.5);  // Right-click to set half-star rating
        });

        // Hover effects for stars
        star.addEventListener('mouseover', () => {
            const hoverValue = parseFloat(star.dataset.value);
            container.querySelectorAll('span').forEach(s => {
                const value = parseFloat(s.dataset.value);
                s.style.color = value <= hoverValue ? 'gold' : 'lightgray';
            });
        });

        star.addEventListener('mouseout', () => {
            container.querySelectorAll('span').forEach(s => {
                const value = parseFloat(s.dataset.value);
                s.style.color = value <= currentRating ? 'gold' : 'lightgray';
            });
        });

        container.appendChild(star);  // Append each star
    }

    // Update the current rating value text
    const ratingTextEl = document.getElementById('current-rating-value');
    if (ratingTextEl) {
        ratingTextEl.textContent = currentRating > 0 ? `${currentRating}/5` : 'Not rated';
    }

    // Add or remove the "Remove Rating" button based on currentRating
    const removeBtn = document.getElementById('remove-rating-btn');
    if (removeBtn) {
        removeBtn.style.display = currentRating > 0 ? 'inline-block' : 'none';
    }

    // Attach event listener for removing rating
    if (removeBtn) {
        removeBtn.addEventListener('click', removeRating);
    }
}

// Function to update the rating (via PUT request to server)
function setRating(newRating) {
    fetch(`/watchlist/rate/${movieId}`, {
        method: 'PUT',
        headers: getAuthHeaders(),
        body: JSON.stringify({ rating: newRating })
    })
    .then(async res => {
        if (!res.ok) throw new Error(await res.text());

        setupStarRating(newRating);  // Update UI with new rating
        fetchMovieDetails();  // Refresh overall rating (define fetchMovieDetails() elsewhere)
    })
    .catch(err => {
        console.error('Error updating rating:', err);
        alert('Failed to update rating: ' + err.message);
    });
}

// Function to remove the rating (via DELETE request to server)
function removeRating() {
    fetch(`/watchlist/rate/${movieId}`, {
        method: 'DELETE',
        headers: getAuthHeaders()
    })
    .then(async res => {
        if (!res.ok) throw new Error(await res.text());

        setupStarRating(0);  // Reset rating to 0 and update UI
        fetchMovieDetails();  // Refresh overall rating
    })
    .catch(err => {
        console.error('Error removing rating:', err);
        alert('Failed to remove rating: ' + err.message);
    });
}