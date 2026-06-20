<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.fashionstore.model.Product" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/images/favicon.png">
    <title>Products - Fashion Store</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css?v=13">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/products.css">
</head>
<body>

<jsp:include page="/WEB-INF/views/partials/navbar.jsp" />

<div class="container page-container">
    <div class="products-page-layout">
        
        <!-- SIDEBAR -->
        <aside class="filter-sidebar" style="width: 220px;">
            <div class="filter-section">
                <h3>Filters</h3>
                <form action="products" method="get" class="filter-form">
                    
                    <div class="input-group">
                        <label>Product</label>
                        <select name="productId" style="width: 100%; padding: 10px 14px; background: var(--bg-dark); border: 1px solid var(--border); border-radius: var(--radius-sm); color: var(--text-primary); font-family: inherit; font-size: 14px; cursor: pointer; appearance: none; -webkit-appearance: none;">
                            <option value="">All Products</option>
                            <%
                                List<Product> allProductsForFilter = (List<Product>) request.getAttribute("allProductsForFilter");
                                String selectedProductId = request.getParameter("productId");
                                if (allProductsForFilter != null) {
                                    for (Product p : allProductsForFilter) {
                                        boolean isSelected = selectedProductId != null && selectedProductId.equals(String.valueOf(p.getProductId()));
                            %>
                                <option value="<%= p.getProductId() %>" <%= isSelected ? "selected" : "" %>><%= p.getProductName() %></option>
                            <%
                                    }
                                }
                            %>
                        </select>
                    </div>

                    <div class="input-group">
                        <label>Min Price</label>
                        <input type="number" name="minPrice" placeholder="Min" value="${param.minPrice}">
                    </div>
                    <div class="input-group">
                        <label>Max Price</label>
                        <input type="number" name="maxPrice" placeholder="Max" value="${param.maxPrice}">
                    </div>
                    
                    <!-- Preserve other params -->
                    <input type="hidden" name="category" value="${param.category}">
                    <input type="hidden" name="keyword" value="${param.keyword}">
                    
                    <button type="submit" class="btn btn-filter">Apply Filters</button>
                    <a href="products" class="clear-filter">Clear All</a>
                </form>
            </div>
        </aside>

        <!-- PRODUCTS CONTENT -->
        <div class="products-content">
            <!-- HEADER ROW: title + sort -->
            <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:20px; flex-wrap:wrap; gap:12px;">
                <h2 style="margin:0;">Collection</h2>


            </div>

            <div class="product-grid">
                <%
                    com.fashionstore.dao.ProductDAO pDaoForStock = new com.fashionstore.dao.impl.ProductDAOImpl();
                    List<Product> products = (List<Product>) request.getAttribute("products");
                    if (products != null && !products.isEmpty()) {
                        for (Product p : products) {
                            List<com.fashionstore.model.ProductVariant> variants = pDaoForStock.getVariantsByProductId(p.getProductId());
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
                    <div class="product-card-img-wrapper">
                        <img src="${pageContext.request.contextPath}/<%= p.getImageUrl() %>" alt="<%= p.getProductName() %>" class="product-img">
                    </div>
                    <div class="product-info-row">
                        <div>
                            <h3><%= p.getProductName() %></h3>
                            <p><%= p.getBrand() %></p>
                            <p class="price">₹<%= p.getPrice() %></p>
                            <span class="stock-badge <%= stockClass %>"><%= stockText %></span>
                        </div>
                        <a href="product?id=<%= p.getProductId() %>" class="btn btn-small">View</a>
                    </div>
                </div>
                <%
                        }
                    } else {
                %>
                <p>No products available</p>
                <% } %>
            </div>
        </div>

    </div>
</div>

<jsp:include page="/WEB-INF/views/partials/footer.jsp" />

</body>
</html>