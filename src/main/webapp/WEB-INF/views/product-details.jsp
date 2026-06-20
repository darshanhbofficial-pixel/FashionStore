<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*, com.fashionstore.model.*" %>

<!DOCTYPE html>
<html>
<head>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/images/favicon.png">
    <title>Product Details</title>

    <link rel="stylesheet" href="/FashionStore/assets/css/style.css?v=13">
    <link rel="stylesheet" href="/FashionStore/assets/css/product-details.css">
    <link rel="stylesheet" href="/FashionStore/assets/css/wishlist.css">
</head>
<body>

<jsp:include page="/WEB-INF/views/partials/navbar.jsp" />

<div class="container">
    <div class="page-container">

        <%
            Product p = (Product) request.getAttribute("product");
            List<ProductVariant> variants =
                (List<ProductVariant>) request.getAttribute("variants");

            String productName = p.getProductName().toLowerCase();

            boolean isPhone = productName.contains("iphone") ||
                              productName.contains("galaxy") ||
                              productName.contains("redmi");
        %>

        <div class="product-details-container">

            <!-- LEFT IMAGE -->
            <div class="product-image">
                <img src="/FashionStore/<%= p.getImageUrl() %>" alt="Product">
            </div>

            <!-- RIGHT DETAILS -->
            <div class="product-info">

                <div class="product-title-row">
                    <h2><%= p.getProductName() %></h2>
                    
                    <%
                        List<Integer> watchlist = (List<Integer>) session.getAttribute("watchlist");
                        boolean inWatchlist = (watchlist != null && watchlist.contains(p.getProductId()));
                    %>
                    <form action="<%= request.getContextPath() %>/wishlist/toggle" method="post" style="display:inline;">
                        <input type="hidden" name="productId" value="<%= p.getProductId() %>">
                        <button type="submit" class="btn-wishlist <%= inWatchlist ? "active" : "" %>" title="<%= inWatchlist ? "Remove from Watchlist" : "Add to Watchlist" %>">
                            <svg width="24" height="24" viewBox="0 0 24 24" fill="<%= inWatchlist ? "currentColor" : "none" %>" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"></path>
                            </svg>
                        </button>
                    </form>
                </div>

                <p class="product-brand">
                    Brand: <%= p.getBrand() %>
                </p>

                <p class="product-price">
                    ₹ <%= p.getPrice() %>
                </p>

                <p class="product-description">
                    <%= p.getDescription() %>
                </p>

                <!-- ✅ DYNAMIC TITLE -->
                <h4>
                    <%= isPhone ? "Available Series" : "Available Sizes" %>
                </h4>

                <!-- ✅ VARIANTS DISPLAY -->
                <form action="cart" method="get">

                    <input type="hidden" name="action" value="add">
                    <input type="hidden" name="quantity" value="1">

                    <div class="sizes">

                        <%
                            if (variants != null && !variants.isEmpty()) {

                                for (ProductVariant v : variants) {
                        %>

                        <label class="size-box">
                            <input type="radio"
                                   name="variantId"
                                   value="<%= v.getVariantId() %>" required>

                            <span><%= v.getSize() %></span>
                        </label>

                        <%
                                }

                            } else {
                        %>

                        <p style="color:red;">No variants available</p>

                        <%
                            }
                        %>

                    </div>

                    <!-- ✅ TEXT FIX -->
                    <p class="size-note">
                        <%= isPhone ? "Select a series to continue"
                                    : "Select a size to continue" %>
                    </p>
                    <div class="rating-box">
    <span class="rating">
        <%= (Math.random() > 0.5) ? "4 ★" : "5 ★" %>
    </span>
    <span class="rating-count">
        | <%= (int)(Math.random() * 500000 + 10000) %>
    </span>
</div>

                    <button type="submit" class="btn-cart">
                        Add to Cart
                    </button>

                </form> 

            </div>

        </div>

    </div>
</div>

<!-- ===== RELATED PRODUCTS ===== -->
<%
    java.util.List<com.fashionstore.model.Product> relatedProducts =
        (java.util.List<com.fashionstore.model.Product>) request.getAttribute("relatedProducts");
    if (relatedProducts != null && !relatedProducts.isEmpty()) {
%>
<div class="container">
  <div class="related-section">
    <h3>You May Also Like</h3>
    <div class="related-grid">
      <% for (com.fashionstore.model.Product rp : relatedProducts) { %>
      <div class="card product-card fade-in">
        <div class="product-card-img-wrapper">
          <img src="/FashionStore/<%= rp.getImageUrl() %>" alt="<%= rp.getProductName() %>" class="product-img">
        </div>
        <div class="product-info-row">
          <div>
            <h3><%= rp.getProductName() %></h3>
            <p><%= rp.getBrand() %></p>
            <p class="price">₹<%= rp.getPrice() %></p>
            <span class="stock-badge in-stock">In Stock</span>
          </div>
          <a href="/FashionStore/product?id=<%= rp.getProductId() %>" class="btn btn-small">View</a>
        </div>
      </div>
      <% } %>
    </div>
  </div>
</div>
<% } %>

<jsp:include page="/WEB-INF/views/partials/footer.jsp" />

</body>
</html>