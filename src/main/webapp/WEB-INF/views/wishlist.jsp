<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.fashionstore.model.Product" %>

<!DOCTYPE html>
<html>
<head>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/images/favicon.png">
    <title>My Watchlist - Fashion Store</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css?v=13">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/home.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/wishlist.css">
</head>
<body>

<jsp:include page="/WEB-INF/views/partials/navbar.jsp" />

<div class="container">
<div class="page-container" style="border-radius: var(--radius-lg); overflow: hidden; position: relative; background: linear-gradient(rgba(18, 18, 20, 0.7), rgba(18, 18, 20, 0.7)), url('${pageContext.request.contextPath}/assets/images/wishlist_bg.png'); background-size: cover; background-position: center; border: 1px solid var(--border); padding: 40px;">

    <div style="position: relative; z-index: 10;">
        <div class="wishlist-header">
            <h1 style="margin-bottom: 8px;">My Watchlist</h1>
            <p style="opacity: 0.8;">Products you're keeping an eye on</p>
        </div>

        <%
            List<Product> watchlistedProducts = (List<Product>) request.getAttribute("watchlistedProducts");

            if (watchlistedProducts == null || watchlistedProducts.isEmpty()) {
        %>
            <div class="empty-state fade-in" style="text-align: center; padding: 80px 20px; margin-top: 40px; background: rgba(255,255,255,0.05); backdrop-filter: blur(15px); border: 1px solid rgba(255,255,255,0.1); border-radius: var(--radius-xl);">
                <div style="width: 80px; height: 80px; background: rgba(0,0,0,0.3); border-radius: 50%; display: flex; align-items: center; justify-content: center; margin: 0 auto 24px; color: white; border: 1px solid rgba(255,255,255,0.1);">
                    <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
                        <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"></path>
                    </svg>
                </div>
                <h2 style="font-size: 28px; font-weight: 800; margin-bottom: 12px; color: white;">Your Wishlist is Empty</h2>
                <p style="color: rgba(255,255,255,0.8); max-width: 400px; margin: 0 auto 32px;">Start adding products you love by clicking the heart icon on any product card!</p>
                <a href="${pageContext.request.contextPath}/products" class="btn" style="padding: 14px 40px; background: white; color: black; border-radius: var(--radius-pill); font-weight: 800; box-shadow: 0 10px 30px rgba(0,0,0,0.3);">Explore Collection</a>
            </div>
        <%
            } else {
        %>
            <div class="product-grid" style="display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 24px; margin-top: 40px;">
                <% for (Product p : watchlistedProducts) { %>
                    <div class="card product-card fade-in" style="background: rgba(255,255,255,0.05); backdrop-filter: blur(10px); border: 1px solid rgba(255,255,255,0.1);">
                        
                        <!-- REMOVE BUTTON (Heart filled) -->
                        <form action="${pageContext.request.contextPath}/wishlist/toggle" method="POST" style="margin: 0;">
                            <input type="hidden" name="productId" value="<%= p.getProductId() %>">
                            <button type="submit" class="card-wishlist-btn active" title="Remove from Wishlist" style="background: white; color: #ff4757; opacity: 1; transform: translateY(0);">
                                <svg width="20" height="20" viewBox="0 0 24 24" fill="#ff4757" stroke="#ff4757" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                    <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"></path>
                                </svg>
                            </button>
                        </form>
    
                        <!-- IMAGE -->
                        <div class="product-card-img-wrapper">
                            <a href="${pageContext.request.contextPath}/product?id=<%= p.getProductId() %>">
                                <img src="<%= request.getContextPath() + "/" + p.getImageUrl() %>" alt="<%= p.getProductName() %>" class="product-img">
                            </a>
                        </div>
                        
                        <!-- INFO -->
                        <div class="product-info-row">
                            <div>
                                <h3 style="margin: 0;"><a href="${pageContext.request.contextPath}/product?id=<%= p.getProductId() %>" style="text-decoration:none; color:inherit;"><%= p.getProductName() %></a></h3>
                                <p style="margin: 2px 0 0; color: var(--text-secondary); font-size: 13px;"><%= p.getBrand() %></p>
                                <p class="price" style="margin-top: 8px;">₹ <%= String.format("%.2f", p.getPrice()) %></p>
                            </div>
                            <a href="${pageContext.request.contextPath}/product?id=<%= p.getProductId() %>" class="btn btn-small">View</a>
                        </div>
                    </div>
                <% } %>
            </div>
        <%
            }
        %>
    </div>
</div>
</div>
</div>

<jsp:include page="/WEB-INF/views/partials/footer.jsp" />

</body>
</html>
