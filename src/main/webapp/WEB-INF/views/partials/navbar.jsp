<%@ page contentType="text/html; charset=UTF-8" %>

<header class="navbar">
    
    <div class="container navbar-container">

    <!-- LOGO (SEPARATE) -->
    <div class="logo">
        <a href="<%= request.getContextPath() %>/home">Fashion Store</a>
    </div>

    <!-- SEARCH (SEPARATE PILL) with Autocomplete -->
    <div class="search-wrapper" id="searchWrapper">
        <form class="search-bar" action="<%= request.getContextPath() %>/products" method="get" autocomplete="off">
            <input type="text" name="keyword" id="searchInput" placeholder="Search..." />
            <button type="submit">Search</button>
        </form>
        <!-- Autocomplete Dropdown -->
        <div class="search-autocomplete" id="searchDropdown"></div>
    </div>

    <!-- MENU -->
    <%
    String path = request.getRequestURI();
    Object loggedInUser = session.getAttribute("user");
%>

<div class="nav-menu">

    <a href="home" class="nav-link <%= path.contains("home") ? "active" : "" %>">Home</a>

    <a href="products" class="nav-link <%= path.contains("products") ? "active" : "" %>">Products</a>

    <a href="cart" class="nav-link <%= path.contains("cart") ? "active" : "" %>" title="Cart">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" style="vertical-align: sub;">
            <circle cx="9" cy="21" r="1"></circle>
            <circle cx="20" cy="21" r="1"></circle>
            <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"></path>
        </svg>
    </a>

    <% if (loggedInUser != null) { %>
        <a href="wishlist" class="nav-link <%= path.contains("wishlist") ? "active" : "" %>" title="Watchlist">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" style="vertical-align: sub;">
                <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"></path>
            </svg>
        </a>
        <a href="profile" class="nav-link <%= path.contains("profile") ? "active" : "" %>" title="Profile">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" style="vertical-align: sub;">
                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                <circle cx="12" cy="7" r="4"></circle>
            </svg>
        </a>
    <% } else { %>
        <a href="login" class="nav-link <%= path.contains("login") ? "active" : "" %>">Login</a>
    <% } %>

    <span class="nav-indicator"></span>

</div>

    <!-- DARK/LIGHT TOGGLE -->
    <button class="theme-toggle" id="themeToggle" title="Toggle Dark/Light Mode" aria-label="Toggle theme">
        <span id="themeIcon">🌙</span>
    </button>

</div>

</header>

<!-- GLOBAL: Toast Container -->
<div id="toast-container"></div>

<!-- GLOBAL: Back to Top Button -->
<button id="back-to-top" title="Back to top" aria-label="Back to top">
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
        <polyline points="18 15 12 9 6 15"></polyline>
    </svg>
</button>

<!-- 🔥 JS FOR ANIMATION + GLOBAL FEATURES -->
<script>
/* ===== NAV INDICATOR ===== */
document.addEventListener("DOMContentLoaded", function () {
    const links = document.querySelectorAll(".nav-link");
    const indicator = document.querySelector(".nav-indicator");

    function moveIndicator(element) {
        const rect = element.getBoundingClientRect();
        const parentRect = element.parentElement.getBoundingClientRect();
        indicator.style.width = rect.width + "px";
        indicator.style.left = (rect.left - parentRect.left) + "px";
    }

    const active = document.querySelector(".nav-link.active");
    if (active) moveIndicator(active);

    links.forEach((link) => {
        link.addEventListener("mouseenter", () => moveIndicator(link));   	
    });
});

/* ===== DARK / LIGHT MODE ===== */
(function() {
    const savedTheme = localStorage.getItem("fs-theme") || "dark";
    document.documentElement.setAttribute("data-theme", savedTheme);
    const icon = document.getElementById("themeIcon");
    if (icon) icon.textContent = savedTheme === "light" ? "☀️" : "🌙";
})();

document.getElementById("themeToggle").addEventListener("click", function() {
    const current = document.documentElement.getAttribute("data-theme") || "dark";
    const next = current === "dark" ? "light" : "dark";
    document.documentElement.setAttribute("data-theme", next);
    localStorage.setItem("fs-theme", next);
    document.getElementById("themeIcon").textContent = next === "light" ? "☀️" : "🌙";
});

/* ===== TOAST SYSTEM ===== */
window.showToast = function(message, type = "info", duration = 3000) {
    const icons = { success: "✅", error: "❌", info: "ℹ️" };
    const container = document.getElementById("toast-container");
    const toast = document.createElement("div");
    toast.className = "toast toast-" + type;
    toast.innerHTML = '<span class="toast-icon">' + (icons[type] || "ℹ️") + '</span><span>' + message + '</span>';
    container.appendChild(toast);
    setTimeout(() => {
        toast.classList.add("toast-out");
        toast.addEventListener("animationend", () => toast.remove());
    }, duration);
};

/* Check for flash messages from session (passed via query params) */
(function() {
    const params = new URLSearchParams(window.location.search);
    if (params.get("success")) showToast(decodeURIComponent(params.get("success")), "success");
    if (params.get("error")) showToast(decodeURIComponent(params.get("error")), "error");
    if (params.get("info")) showToast(decodeURIComponent(params.get("info")), "info");
})();

/* ===== BACK TO TOP ===== */
const backBtn = document.getElementById("back-to-top");
window.addEventListener("scroll", function() {
    if (window.scrollY > 400) {
        backBtn.classList.add("visible");
    } else {
        backBtn.classList.remove("visible");
    }
});
backBtn.addEventListener("click", function() {
    window.scrollTo({ top: 0, behavior: "smooth" });
});

/* ===== SEARCH AUTOCOMPLETE ===== */
(function() {
    const input = document.getElementById("searchInput");
    const dropdown = document.getElementById("searchDropdown");
    const contextPath = "<%= request.getContextPath() %>";
    let debounceTimer;

    if (!input || !dropdown) return;

    input.addEventListener("input", function() {
        clearTimeout(debounceTimer);
        const q = input.value.trim();
        if (q.length < 2) {
            dropdown.classList.remove("open");
            dropdown.innerHTML = "";
            return;
        }
        debounceTimer = setTimeout(function() {
            fetch(contextPath + "/api/search-suggestions?q=" + encodeURIComponent(q))
                .then(r => r.json())
                .then(function(results) {
                    if (!results || results.length === 0) {
                        dropdown.classList.remove("open");
                        dropdown.innerHTML = "";
                        return;
                    }
                    dropdown.innerHTML = results.map(p =>
                        '<a class="autocomplete-item" href="' + contextPath + '/product?id=' + p.id + '">' +
                        '<img src="' + contextPath + '/' + p.image + '" alt="' + p.name + '" onerror="this.style.display=\'none\'">' +
                        '<div class="autocomplete-item-info">' +
                        '<div class="autocomplete-item-name">' + p.name + '</div>' +
                        '<div class="autocomplete-item-brand">' + p.brand + '</div>' +
                        '</div></a>'
                    ).join("");
                    dropdown.classList.add("open");
                })
                .catch(() => {
                    dropdown.classList.remove("open");
                });
        }, 280);
    });

    document.addEventListener("click", function(e) {
        if (!document.getElementById("searchWrapper").contains(e.target)) {
            dropdown.classList.remove("open");
        }
    });

    input.addEventListener("keydown", function(e) {
        if (e.key === "Escape") dropdown.classList.remove("open");
    });
})();
</script>