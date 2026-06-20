<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.fashionstore.model.Product" %>

<!DOCTYPE html>
<html>
<head>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/images/favicon.png">
    <title>Home - Fashion Store</title>

    <!-- CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css?v=13">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/home.css?v=11">
</head>
<body>

<!-- NAVBAR -->
<jsp:include page="/WEB-INF/views/partials/navbar.jsp" />

<div class="container">
<div class="page-container">

    <!-- ===== HERO SLIDER ===== -->
    <div class="hero-slider" id="heroSlider">
        <!-- Slide 1 (Men) -->
        <div class="hero-slide active" style="background-image: url('${pageContext.request.contextPath}/assets/images/slide-men.png');">
            <div class="hero-overlay"></div>
            <div class="hero-content">
                <h1>Streetwear Essentials</h1>
                <p>Discover the latest urban styles, built for the modern edge.</p>
                <a href="${pageContext.request.contextPath}/products?keyword=Men" class="btn">Shop Men's</a>
            </div>
        </div>
        
        <!-- Slide 2 (Watches/Accessories) -->
        <div class="hero-slide" style="background-image: url('${pageContext.request.contextPath}/assets/images/slide-watches.png');">
            <div class="hero-overlay"></div>
            <div class="hero-content">
                <h1>Timeless Luxury</h1>
                <p>Premium watches and accessories to elevate every look.</p>
                <a href="${pageContext.request.contextPath}/products?collection=Accessories" class="btn">Shop Accessories</a>
            </div>
        </div>
        
        <!-- Slide 3 (Women) -->
        <div class="hero-slide" style="background-image: url('${pageContext.request.contextPath}/assets/images/slide-women.png');">
            <div class="hero-overlay"></div>
            <div class="hero-content">
                <h1>Modern Elegance</h1>
                <p>Sleek, minimalist, and uncompromisingly chic.</p>
                <a href="${pageContext.request.contextPath}/products?keyword=Women" class="btn">Shop Women's</a>
            </div>
        </div>

        <!-- Slider Controls -->
        <div class="slider-controls">
            <span class="dot active" onclick="currentSlide(0)"></span>
            <span class="dot" onclick="currentSlide(1)"></span>
            <span class="dot" onclick="currentSlide(2)"></span>
        </div>
    </div>

    <!-- CATEGORY -->
    <h2 class="section-title">Shop by Category</h2>

    <div class="category-grid">

        <div class="category-card">
            <h3>Shoes</h3>
            <p>All kinds of shoes</p>
            <a href="${pageContext.request.contextPath}/products?category=Shoes">Explore</a>
        </div>

        <div class="category-card">
            <h3>Watches</h3>
            <p>Latest watches</p>
            <a href="${pageContext.request.contextPath}/products?category=Watches">Explore</a>
        </div>



        <div class="category-card">
            <h3>Caps</h3>
            <p>Stylish caps</p>
            <a href="${pageContext.request.contextPath}/products?category=Caps">Explore</a>
        </div>

        <div class="category-card">
            <h3>Clothes</h3>
            <p>Men & Women's Fashion</p>
            <a href="${pageContext.request.contextPath}/products?collection=Clothes">Explore</a>
        </div>

    </div>

    <!-- PRODUCTS -->
    <h2 class="section-title">Featured Products</h2>

    <div class="product-grid">

    <%
        com.fashionstore.dao.ProductDAO pDaoForStock = new com.fashionstore.dao.impl.ProductDAOImpl();
        java.util.List<com.fashionstore.model.Product> products =
            (java.util.List<com.fashionstore.model.Product>) request.getAttribute("products");

        if (products != null && !products.isEmpty()) {

            for (com.fashionstore.model.Product p : products) {
                java.util.List<com.fashionstore.model.ProductVariant> variants = pDaoForStock.getVariantsByProductId(p.getProductId());
                int totalStock = 0;
                if (variants != null) {
                    for (com.fashionstore.model.ProductVariant v : variants) {
                        totalStock += v.getStockQuantity();
                    }
                }
                String stockClass = "in-stock";
                String stockText = "In Stock";
                if (totalStock == 0) {
                    stockClass = "out-of-stock";
                    stockText = "Out of Stock";
                } else if (totalStock < 10) {
                    stockClass = "low-stock";
                    stockText = "Low Stock (" + totalStock + ")";
                }
    %>

    <div class="card product-card">

        <!-- IMAGE -->
        <div class="product-card-img-wrapper">
            <img src="${pageContext.request.contextPath}/<%= p.getImageUrl() %>" 
                 alt="product" 
                 class="product-img">
        </div>
        
        <!-- CONTENT -->
        <div class="product-info-row">
            <div>
                <h3><%= p.getProductName() %></h3>
                <p><%= p.getBrand() %></p>
                <p class="price">₹<%= p.getPrice() %></p>
                <span class="stock-badge <%= stockClass %>"><%= stockText %></span>
            </div>

            <a href="${pageContext.request.contextPath}/product?id=<%= p.getProductId() %>" 
               class="btn btn-small">
                View
            </a>
        </div>

    </div>

    <%
            }
        } else {
    %>

    <p>No products available</p>

    <%
        }
    %>

    </div>

</div>
</div>

<!-- FOOTER -->
<jsp:include page="/WEB-INF/views/partials/footer.jsp" />


<script>
    // Hero Slider JS
    let slideIndex = 0;
    const slides = document.querySelectorAll(".hero-slide");
    const dots = document.querySelectorAll(".dot");
    let sliderInterval;

    function showSlide(index) {
        slides.forEach(slide => slide.classList.remove("active"));
        dots.forEach(dot => dot.classList.remove("active"));
        
        slideIndex = index;
        if (slideIndex >= slides.length) slideIndex = 0;
        if (slideIndex < 0) slideIndex = slides.length - 1;
        
        if (slides.length > 0 && dots.length > 0) {
            slides[slideIndex].classList.add("active");
            dots[slideIndex].classList.add("active");
        }
    }

    function currentSlide(index) {
        showSlide(index);
        resetInterval();
    }

    function nextSlide() {
        showSlide(slideIndex + 1);
    }

    function resetInterval() {
        if (slides.length === 0) return;
        clearInterval(sliderInterval);
        sliderInterval = setInterval(nextSlide, 5000); // Change slide every 5 seconds
    }

    // Initialize
    if (slides.length > 0) {
        resetInterval();
    }
</script>

</body>
</html>