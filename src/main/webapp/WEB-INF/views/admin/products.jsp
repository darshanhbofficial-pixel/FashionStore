<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.fashionstore.model.Product" %>
<%@ page import="com.fashionstore.model.Category" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/images/favicon.png">
    <meta charset="UTF-8">
    <title>Manage Products | Admin Panel</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css?v=13">
    <style>
        .admin-layout {
            display: grid;
            grid-template-columns: 240px 1fr;
            min-height: 100vh;
        }
        .admin-sidebar {
            background: var(--bg-card);
            border-right: 1px solid var(--border);
            padding: 32px 20px;
        }
        .admin-main {
            padding: 40px;
            background: var(--bg-dark);
        }
        .admin-nav {
            margin-top: 32px;
        }
        .admin-nav-link {
            display: flex;
            align-items: center;
            gap: 12px;
            padding: 12px;
            color: var(--text-secondary);
            text-decoration: none;
            border-radius: var(--radius-md);
            margin-bottom: 8px;
            transition: var(--transition-fast);
        }
        .admin-nav-link:hover, .admin-nav-link.active {
            background: var(--bg-pill);
            color: var(--primary);
        }
        
        /* Table Styling */
        .products-table-card {
            background: var(--bg-card);
            border: 1px solid var(--border);
            border-radius: var(--radius-lg);
            padding: 24px;
            margin-top: 24px;
            overflow-x: auto;
            box-shadow: var(--shadow-sm);
        }
        table {
            width: 100%;
            border-collapse: collapse;
            text-align: left;
        }
        th, td {
            padding: 14px 16px;
            border-bottom: 1px solid var(--border);
            font-size: 14px;
        }
        th {
            font-weight: 700;
            color: var(--text-secondary);
            text-transform: uppercase;
            font-size: 12px;
            letter-spacing: 0.5px;
        }
        td {
            color: var(--text-primary);
        }
        tr:last-child td {
            border-bottom: none;
        }
        .product-thumb {
            width: 40px;
            height: 40px;
            object-fit: cover;
            border-radius: var(--radius-sm);
            border: 1px solid var(--border);
        }
        
        /* Badges */
        .status-badge {
            display: inline-block;
            padding: 4px 10px;
            border-radius: var(--radius-pill);
            font-size: 11px;
            font-weight: 700;
            text-transform: uppercase;
        }
        .status-active {
            background: rgba(46, 213, 115, 0.15);
            color: #2ed573;
            border: 1px solid rgba(46, 213, 115, 0.3);
        }
        .status-inactive {
            background: rgba(255, 71, 87, 0.12);
            color: #ff4757;
            border: 1px solid rgba(255, 71, 87, 0.3);
        }

        /* Modals / Forms */
        .modal {
            display: none;
            position: fixed;
            top: 0; left: 0; width: 100%; height: 100%;
            background: rgba(0, 0, 0, 0.6);
            backdrop-filter: blur(8px);
            z-index: 2000;
            align-items: center;
            justify-content: center;
        }
        .modal.open {
            display: flex;
        }
        .modal-content {
            background: var(--bg-card);
            border: 1px solid var(--border);
            border-radius: var(--radius-lg);
            width: 90%;
            max-width: 500px;
            padding: 32px;
            box-shadow: var(--shadow-lg);
            position: relative;
            animation: modalSlideUp 0.4s cubic-bezier(0.16, 1, 0.3, 1) both;
        }
        @keyframes modalSlideUp {
            from { transform: translateY(40px); opacity: 0; }
            to { transform: translateY(0); opacity: 1; }
        }
        .modal-close {
            position: absolute;
            top: 20px;
            right: 20px;
            background: none;
            border: none;
            color: var(--text-muted);
            font-size: 20px;
            cursor: pointer;
            transition: var(--transition-fast);
        }
        .modal-close:hover {
            color: var(--primary);
        }
        
        .form-group {
            margin-bottom: 16px;
        }
        .form-group label {
            display: block;
            font-size: 13px;
            color: var(--text-secondary);
            margin-bottom: 6px;
            font-weight: 600;
        }
        .form-group input, .form-group select, .form-group textarea {
            width: 100%;
            padding: 10px 14px;
            background: var(--bg-dark);
            border: 1px solid var(--border);
            border-radius: var(--radius-sm);
            color: var(--text-primary);
            font-family: inherit;
            font-size: 14px;
        }
        .form-group input:focus, .form-group select:focus, .form-group textarea:focus {
            outline: none;
            border-color: var(--primary);
        }
    </style>
</head>
<body>
    <div class="admin-layout">
        <!-- SIDEBAR -->
        <div class="admin-sidebar">
            <h2 style="font-size: 18px; font-weight: 800; color: var(--primary); margin-bottom: 32px;">CORE PANEL</h2>
            <nav class="admin-nav">
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="admin-nav-link">Dashboard</a>
                <a href="#" class="admin-nav-link active">Manage Products</a>
                <a href="${pageContext.request.contextPath}/admin/orders" class="admin-nav-link">Manage Orders</a>
                <a href="#" class="admin-nav-link">User Directory</a>
                <div style="margin-top: 40px; border-top: 1px solid var(--border); padding-top: 20px;">
                    <a href="${pageContext.request.contextPath}/home" class="admin-nav-link">Back to Store</a>
                </div>
            </nav>
        </div>

        <!-- MAIN CONTENT -->
        <div class="admin-main">
            <header style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px;">
                <div>
                    <h1 style="font-size: 32px; font-weight: 800; margin-bottom: 4px;">Products</h1>
                    <p style="color: var(--text-secondary); font-size: 14px;">Manage catalog inventory, update prices, or add new items.</p>
                </div>
                <button class="btn btn-small" onclick="openAddModal()">➕ Add Product</button>
            </header>

            <!-- PRODUCTS TABLE -->
            <div class="products-table-card fade-in">
                <table>
                    <thead>
                        <tr>
                            <th>Image</th>
                            <th>ID</th>
                            <th>Product Name</th>
                            <th>Category</th>
                            <th>Brand</th>
                            <th>Price</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<Product> products = (List<Product>) request.getAttribute("products");
                            List<Category> categories = (List<Category>) request.getAttribute("categories");
                            if (products != null && !products.isEmpty()) {
                                for (Product p : products) {
                                    // Find category name
                                    String catName = "Unknown";
                                    if (categories != null) {
                                        for (Category c : categories) {
                                            if (c.getCategoryId() == p.getCategoryId()) {
                                                catName = c.getCategoryName();
                                                break;
                                            }
                                        }
                                    }
                        %>
                        <tr>
                            <td>
                                <img src="${pageContext.request.contextPath}/<%= p.getImageUrl() %>" alt="thumb" class="product-thumb" onerror="this.src='${pageContext.request.contextPath}/assets/images/placeholder.png';">
                            </td>
                            <td><%= p.getProductId() %></td>
                            <td style="font-weight: 600;"><%= p.getProductName() %></td>
                            <td><%= catName %></td>
                            <td><%= p.getBrand() %></td>
                            <td style="font-weight: 700; color: var(--primary);">₹<%= p.getPrice() %></td>
                            <td>
                                <span class="status-badge <%= p.isActive() ? "status-active" : "status-inactive" %>">
                                    <%= p.isActive() ? "Active" : "Inactive" %>
                                </span>
                            </td>
                            <td>
                                <div style="display: flex; gap: 8px;">
                                    <button class="btn btn-small" style="padding: 6px 12px; background: #341f97; border-color: #341f97;" 
                                        onclick="openEditModal(<%= p.getProductId() %>, <%= p.getCategoryId() %>, '<%= p.getProductName().replace("'", "\\'") %>', '<%= p.getBrand().replace("'", "\\'") %>', '<%= p.getDescription().replace("'", "\\'").replace("\n", " ").replace("\r", " ") %>', <%= p.getPrice() %>, '<%= p.getImageUrl() %>', <%= p.isActive() %>)">
                                        ✏️ Edit
                                    </button>
                                    <form action="${pageContext.request.contextPath}/admin/products" method="post" onsubmit="return confirm('Are you sure you want to deactivate this product?');" style="display:inline;">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="productId" value="<%= p.getProductId() %>">
                                        <button type="submit" class="btn btn-small btn-danger" style="padding: 6px 12px;">🗑️ Delete</button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                        <%
                                }
                            } else {
                        %>
                        <tr>
                            <td colspan="8" style="text-align: center; color: var(--text-muted); padding: 32px;">No products available</td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- ADD MODAL -->
    <div class="modal" id="addModal">
        <div class="modal-content">
            <button class="modal-close" onclick="closeAddModal()">✖</button>
            <h3 style="margin-bottom: 24px; font-weight: 800; font-size: 20px;">Add New Product</h3>
            <form action="${pageContext.request.contextPath}/admin/products" method="post">
                <input type="hidden" name="action" value="add">
                
                <div class="form-group">
                    <label>Product Name</label>
                    <input type="text" name="productName" required>
                </div>
                
                <div class="form-group">
                    <label>Category</label>
                    <select name="categoryId" required>
                        <% if (categories != null) {
                            for (Category c : categories) { %>
                                <option value="<%= c.getCategoryId() %>"><%= c.getCategoryName() %></option>
                        <% } } %>
                    </select>
                </div>

                <div class="form-group">
                    <label>Brand</label>
                    <input type="text" name="brand" required>
                </div>

                <div class="form-group">
                    <label>Price (₹)</label>
                    <input type="number" step="0.01" name="price" required>
                </div>

                <div class="form-group">
                    <label>Image Path</label>
                    <input type="text" name="imageUrl" placeholder="e.g. assets/images/products/item.png">
                </div>

                <div class="form-group">
                    <label>Description</label>
                    <textarea name="description" rows="3" required></textarea>
                </div>

                <button type="submit" class="btn btn-small" style="width: 100%; padding: 12px; margin-top: 10px;">Create Product</button>
            </form>
        </div>
    </div>

    <!-- EDIT MODAL -->
    <div class="modal" id="editModal">
        <div class="modal-content">
            <button class="modal-close" onclick="closeEditModal()">✖</button>
            <h3 style="margin-bottom: 24px; font-weight: 800; font-size: 20px;">Edit Product</h3>
            <form action="${pageContext.request.contextPath}/admin/products" method="post">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="productId" id="editProductId">
                
                <div class="form-group">
                    <label>Product Name</label>
                    <input type="text" name="productName" id="editProductName" required>
                </div>
                
                <div class="form-group">
                    <label>Category</label>
                    <select name="categoryId" id="editCategoryId" required>
                        <% if (categories != null) {
                            for (Category c : categories) { %>
                                <option value="<%= c.getCategoryId() %>"><%= c.getCategoryName() %></option>
                        <% } } %>
                    </select>
                </div>

                <div class="form-group">
                    <label>Brand</label>
                    <input type="text" name="brand" id="editBrand" required>
                </div>

                <div class="form-group">
                    <label>Price (₹)</label>
                    <input type="number" step="0.01" name="price" id="editPrice" required>
                </div>

                <div class="form-group">
                    <label>Image Path</label>
                    <input type="text" name="imageUrl" id="editImageUrl">
                </div>

                <div class="form-group">
                    <label>Description</label>
                    <textarea name="description" id="editDescription" rows="3" required></textarea>
                </div>

                <div class="form-group">
                    <label>Status</label>
                    <select name="isActive" id="editIsActive">
                        <option value="true">Active</option>
                        <option value="false">Inactive</option>
                    </select>
                </div>

                <button type="submit" class="btn btn-small" style="width: 100%; padding: 12px; margin-top: 10px;">Save Changes</button>
            </form>
        </div>
    </div>

    <!-- JS FOR MODALS -->
    <script>
        function openAddModal() {
            document.getElementById("addModal").classList.add("open");
        }
        function closeAddModal() {
            document.getElementById("addModal").classList.remove("open");
        }
        function openEditModal(id, categoryId, name, brand, desc, price, img, active) {
            document.getElementById("editProductId").value = id;
            document.getElementById("editCategoryId").value = categoryId;
            document.getElementById("editProductName").value = name;
            document.getElementById("editBrand").value = brand;
            document.getElementById("editDescription").value = desc;
            document.getElementById("editPrice").value = price;
            document.getElementById("editImageUrl").value = img;
            document.getElementById("editIsActive").value = active ? "true" : "false";
            document.getElementById("editModal").classList.add("open");
        }
        function closeEditModal() {
            document.getElementById("editModal").classList.remove("open");
        }

        // Close on backdrop click
        window.onclick = function(e) {
            if (e.target.classList.contains("modal")) {
                e.target.classList.remove("open");
            }
        }
    </script>
</body>
</html>
