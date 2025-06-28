// watchlistselect.js
function setupWatchlistUI(movieId, currentStatus) {
    const container = document.getElementById('watchlist-container');
    const inList = currentStatus === "TO_WATCH" || currentStatus === "WATCHED";

    if (!inList) {
        container.innerHTML = `
            <div style="position:relative;margin:15px 0">
                <button id="add-btn" style="padding:8px 15px;background:#3498db;color:#fff;border:none;border-radius:4px;cursor:pointer;">
                    Add to Watchlist
                </button>
                <div id="dropdown" style="display:none;position:absolute;background:#fff;border:1px solid #ddd;border-radius:4px;box-shadow:0 2px 5px rgba(0,0,0,0.2);z-index:1000;">
                    <div style="padding:8px 15px;cursor:pointer" onclick="addToWatchlist(${movieId},'TO_WATCH')">To Watch</div>
                    <div style="padding:8px 15px;cursor:pointer" onclick="addToWatchlist(${movieId},'WATCHED')">Watched</div>
                </div>
            </div>
        `;
        document.getElementById('add-btn').addEventListener('click', e => {
            e.stopPropagation();
            const dd = document.getElementById('dropdown');
            dd.style.display = dd.style.display==='none'?'block':'none';
        });
        document.addEventListener('click', () => {
            const dd = document.getElementById('dropdown');
            if (dd) dd.style.display = 'none';
        });
    } else {
        container.innerHTML = `
            <div style="margin:15px 0">
                <label><strong>Watchlist Status:</strong></label>
                <select id="status-select" style="margin-left:5px;padding:6px;border:1px solid #ccc;border-radius:4px;">
                    <option value="TO_WATCH" ${currentStatus==='TO_WATCH'?'selected':''}>To Watch</option>
                    <option value="WATCHED" ${currentStatus==='WATCHED'?'selected':''}>Watched</option>
                </select>
                <button id="remove-btn" style="margin-left:10px;padding:6px 10px;background:#e74c3c;color:#fff;border:none;border-radius:4px;cursor:pointer;">
                    Remove
                </button>
            </div>
        `;
        document.getElementById('status-select').addEventListener('change', () => {
            updateWatchlistStatus(movieId, document.getElementById('status-select').value);
        });
        document.getElementById('remove-btn').addEventListener('click', () => {
            if (confirm('Remove this movie from your watchlist?')) {
                removeFromWatchlist(movieId);
            }
        });
    }
}

function addToWatchlist(movieId, status) {
    document.getElementById('dropdown').style.display = 'none';
    getCsrfToken().then(csrfToken=>{
    fetch('/watchlist/add', {
        method: 'POST',
        headers: {
        "Content-Type": "application/json",
        "X-XSRF-TOKEN":csrfToken},
        credentials:"include",
        body: JSON.stringify({ movieId, watchStatus: status })
    })
    .then(async res => {
        if (!res.ok) throw new Error(await res.text());
//        alert(`Added as "${status.replace('_',' ')}"!`);
        location.reload();
    })
    .catch(err => alert(`Add failed: ${err.message}`));
    });
}

function updateWatchlistStatus(movieId, status) {
getCsrfToken().then(csrfToken=>{
    fetch(`/watchlist/update/${movieId}`, {
        method: 'PUT',
        headers: {
        "Content-Type": "application/json",
                "X-XSRF-TOKEN":csrfToken},
        credentials:"include",
        body: JSON.stringify({ watchStatus: status })
    })
    .then(async res => {
        if (!res.ok) throw new Error(await res.text());
//        alert(`Status updated to "${status.replace('_',' ')}"`);
        location.reload();
    })
    .catch(err => alert(`Update failed: ${err.message}`));
    });
}

function removeFromWatchlist(movieId) {
    getCsrfToken().then(csrfToken=>{
    fetch(`/watchlist/delete/${movieId}`, {
        method: 'DELETE',
        headers: {"X-XSRF-TOKEN":csrfToken},
        credentials:"include"
    })
    .then(async res => {
        if (!res.ok) throw new Error(await res.text());
//        alert('Removed from watchlist.');
        location.reload();
    })
    .catch(err => alert(`Remove failed: ${err.message}`));
    });
}
