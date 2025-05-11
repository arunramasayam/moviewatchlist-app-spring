   function triggerSearch() {
      const keyword = document.getElementById("searchBox").value.trim();
      if (keyword) {
        window.location.href = `/index.html?keyword=${encodeURIComponent(keyword)}`;
      }
    }

    // Enable Enter key for search input
    document.getElementById("searchBox").addEventListener("keypress", function(event) {
      if (event.key === "Enter") {
        triggerSearch();
      }
    });
