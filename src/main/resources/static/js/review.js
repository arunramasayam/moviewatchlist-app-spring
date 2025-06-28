function saveReview() {
    const reviewText = document.getElementById('user-review').value;

    if (!reviewText) {
        alert('Please enter a review.');
        return;
    }
    getCsrfToken().then(csrfToken=>{
    fetch(`/watchlist/review/${movieId}`, {
        method: 'PUT',
        headers: {
        "Content-Type":"application/json",
        "X-XSRF-TOKEN":csrfToken},
        credentials:"include",
        body: JSON.stringify({ review: reviewText })
    })
    .then(res => {
        if (!res.ok) throw new Error("Failed to save review");
        return res.json();
    })
    .then(data => {
        alert('Review saved successfully!');
        fetchMovieDetails();  // Refresh movie details with updated review
    })
    .catch(err => {
        console.error('Error saving review:', err);
        alert('Error saving review.');
    });
    });
}

function deleteReview() {
    // Show a confirmation before deletion
    if (!confirm('Are you sure you want to delete your review?')) return;

    getCsrfToken().then(csrfToken=>{
    fetch(`/watchlist/review/${movieId}`, {
        method: 'DELETE',
        headers: {"X-XSRF-TOKEN":csrfToken},
        credentials:"include",
    })
    .then(res => {
        // Log the response to understand what is returned
        console.log('Response Status:', res.status);
        console.log('Response Body:', res);

        if (!res.ok) {
            return res.text().then(errorText => {
                console.error('Error text from API:', errorText);
                throw new Error(`Failed to delete review: ${errorText}`);
            });
        }

        // Check if the response is in JSON format or empty
        return res.json().catch(() => {
            console.log('No content or unexpected response format');
            return {}; // Return an empty object to avoid issues with undefined responses
        });
    })
    .then(data => {
        // Handle successful deletion
//        alert('Review deleted successfully!');

        // Optionally reload the page or update UI
        location.reload();  // Or use UI updates as shown earlier
    })
    .catch(err => {
        console.error('Error deleting review:', err);
        alert(`Error deleting review: ${err.message}`);
    });
    });
}


// Show review box when "Add Review" is clicked
function showReviewBox() {
    const reviewBox = `
        <label><strong>Your Review:</strong></label><br>
        <textarea id="user-review" cols="40" rows="5"></textarea><br>
        <button id="save-review-btn">Save Review</button>
    `;
    document.getElementById('movie-details').insertAdjacentHTML('beforeend', reviewBox);

    // Attach event listener to save review
    document.getElementById('save-review-btn').addEventListener('click', saveReview);
    document.getElementById('add-review-btn').remove(); // Remove "Add Review" button
}
